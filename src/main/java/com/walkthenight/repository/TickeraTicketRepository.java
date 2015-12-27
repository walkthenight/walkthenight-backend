package com.walkthenight.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.restfb.DefaultWebRequestor;
import com.restfb.WebRequestor;
import com.restfb.WebRequestor.Response;
import com.walkthenight.data.Ticket;
import com.walkthenight.data.TicketRepository;
import com.walkthenight.data.TicketedEvent;
import com.walkthenight.data.TicketedEventRepository;
import com.walkthenight.wordpress.WordPressPostMetaMapper;

public class TickeraTicketRepository implements TicketRepository, TicketedEventRepository {

	private static final String WP_JSON_V2 = "/wp-json/wp/v2/";
	private static final String BASE_URL= "http://walkthenight.com/la";
	
	@Override
	public List<Ticket> getTickets() {
		WebRequestor client = new DefaultWebRequestor();
		
		Response response;
		try {
			response = client.executeGet(BASE_URL+WP_JSON_V2+"tc_tickets");
			return mapTickets(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
        
	}

	private List<Ticket> mapTickets(Response response) {
		 String json = response.getBody();
         ObjectMapper mapper = new ObjectMapper();
         try {
        	 TicketJson[] tickets = mapper.readValue(json, TypeFactory.defaultInstance().constructArrayType(TicketJson.class));
        	 return mapTickets(tickets);
         } catch (IOException e) {
        	 return null;
         }
	}

	private List<Ticket> mapTickets(TicketJson[] tjs) {
		List<Ticket> tickets= new ArrayList<>();
		for (TicketJson tj : tjs) {
			tickets.add(mapTicket(tj));
		}
		return tickets;
	}

	private Ticket mapTicket(TicketJson tj) {
		Ticket ticket= new Ticket();
		ticket.id= tj.id;
		ticket.description= tj.title.rendered;
		return ticket;
	}

	@Override
	public Ticket getTicket(String ticketId) {
		WebRequestor client = new DefaultWebRequestor();
		Map<String, String> postMetaMap = WordPressPostMetaMapper.ticketPostMeta(client, BASE_URL, ticketId);	
		if (null != postMetaMap && postMetaMap.size() > 0);
		return mapPostMetaToTicket(postMetaMap);
	}
	
	private static Ticket mapPostMetaToTicket(Map<String, String> postMetaMap) {
		Ticket ticket= new Ticket();
		ticket.pricePerTicket= postMetaMap.get("price_per_ticket");
		ticket.quantityAvailable= postMetaMap.get("quantity_available");
		ticket.eventId= postMetaMap.get("event_name");
		return ticket;
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class TicketJson {
		public String id;
		public RawAndRendered title;

	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class RawAndRendered {
		public String raw;
		public String rendered;
	}

	@Override
	public List<TicketedEvent> getTicketedEvents() {
		WebRequestor client = new DefaultWebRequestor();
		
		Response response;
		try {
			response = client.executeGet(BASE_URL+WP_JSON_V2+"tc_events");
			return mapTicketedEvents(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private List<TicketedEvent> mapTicketedEvents(Response response) {
		String json = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
       	 TicketedEventJson[] events = mapper.readValue(json, TypeFactory.defaultInstance().constructArrayType(TicketedEventJson.class));
       	 return mapTicketedEvents(events);
        } catch (IOException e) {
       	 return null;
        }
	}

	private List<TicketedEvent> mapTicketedEvents(TicketedEventJson[] tjs) {
		List<TicketedEvent> events= new ArrayList<>();
		for (TicketedEventJson tj : tjs) {
			events.add(mapTicketedEvent(tj));
		}
		return events;
	}

	private TicketedEvent mapTicketedEvent(TicketedEventJson tj) {
		TicketedEvent event= new TicketedEvent();
		event.id= tj.id;
		event.title= tj.title.rendered;
		event.description= tj.content.rendered;
		return event;
	}

	@Override
	public TicketedEvent getTicketedEvent(String eventId) {
		WebRequestor client = new DefaultWebRequestor();

		try {
			TicketedEvent event = getBaseEventData(client);
			addPostMetaData(eventId, client, event);
			
			return event;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		
	}

	private TicketedEvent getBaseEventData(WebRequestor client) throws IOException {
		Response response= client.executeGet(BASE_URL+WP_JSON_V2+"tc_events");
		TicketedEvent event= mapTicketedEvent(response);
		return event;
	}

	private void addPostMetaData(String eventId, WebRequestor client, TicketedEvent event) {
		Map<String, String> postMetaMap = WordPressPostMetaMapper.ticketPostMeta(client, BASE_URL, eventId);	
		if (null != postMetaMap && postMetaMap.size() > 0)
			mapPostMetaToTicketedEvent(event, postMetaMap);
	}
	
	private TicketedEvent mapTicketedEvent(Response response) {
		String json = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
		try {
       	 	TicketedEventJson event = mapper.readValue(json, TypeFactory.defaultInstance().constructType(TicketedEventJson.class));
       	 	return mapTicketedEvent(event);
        } catch (IOException e) {
       	 return null;
        }
	}

	private void mapPostMetaToTicketedEvent(TicketedEvent event, Map<String, String> postMetaMap) {
		event.location= postMetaMap.get("event_location");
		event.dateTime= postMetaMap.get("event_date_time");
		event.endDateTime= postMetaMap.get("event_end_date_time");
		event.logoUrl= postMetaMap.get("event_logo_file_url");
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class TicketedEventJson {
		public String id;
		public RawAndRendered title;
		public RawAndRendered content;
	}
	

}
