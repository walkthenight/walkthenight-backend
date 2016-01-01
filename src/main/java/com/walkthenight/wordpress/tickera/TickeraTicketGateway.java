package com.walkthenight.wordpress.tickera;

import static com.walkthenight.wordpress.WordPressWebRequestor.executeGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.api.client.http.HttpResponse;
import com.walkthenight.data.Ticket;
import com.walkthenight.wordpress.RawAndRendered;
import com.walkthenight.wordpress.WordPressPostGateway;
import com.walkthenight.wordpress.WordPressPostGateway.Status;
import com.walkthenight.wordpress.WordPressPostMetaGateway;

public class TickeraTicketGateway {

	public String createTicket(String name, int price, int quantity) {
		HttpResponse response;
		
		try {
			
			response = createPost(name);

			String json= response.parseAsString();
			
			ObjectMapper mapper = new ObjectMapper();
			TicketJson ticketJson = mapper.readValue(json, typeFactory().constructType(TicketJson.class));
			
			Map<String, String> postMetaMap= new HashMap<>();
			postMetaMap.put("price_per_ticket", String.valueOf(price));
			postMetaMap.put("quantity_available", String.valueOf(quantity));
			postMetaMap.put("min_tickets_per_order", "1");
			postMetaMap.put("ticket_fee_type", "fixed");
			
			WordPressPostMetaGateway.writePostMeta(ticketJson.id, postMetaMap);
			
			return ticketJson.id;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public void updateTicketWithEventId(String ticketId, String eventId) {
		Map<String, String> postMetaMap= new HashMap<>();
		postMetaMap.put("event_name", eventId);
		WordPressPostMetaGateway.writePostMeta(ticketId, postMetaMap);
	}

	private HttpResponse createPost(String name) throws IOException {
		return WordPressPostGateway.createPost("tc_tickets", name, "", null, Status.PUBLISH);
	}
	
	public List<Ticket> getTickets() {
		HttpResponse response;
		try {
			response = executeGet("tc_tickets");
			return mapTickets(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private TypeFactory typeFactory() {
		return TypeFactory.defaultInstance();
	}

	private List<Ticket> mapTickets(HttpResponse response) throws IOException {
		 String json = response.parseAsString();
         ObjectMapper mapper = new ObjectMapper();
         try {
        	 TicketJson[] tickets = mapper.readValue(json, typeFactory().constructArrayType(TicketJson.class));
        	 return mapTickets(tickets);
         } catch (IOException e) {
        	 throw new RuntimeException(e);
         }
	}
	
	private Ticket mapTicket(HttpResponse response) throws IOException {
		String json = response.parseAsString();
        ObjectMapper mapper = new ObjectMapper();
        try {
       	 TicketJson[] tickets = mapper.readValue(json, typeFactory().constructArrayType(TicketJson.class));
       	 return mapTicket(tickets[0]);
        } catch (IOException e) {
        	throw new RuntimeException(e);
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
		ticket.ticketsSold= tj.tickets_sold;
		return ticket;
	}

	public Ticket getTicket(String ticketId) {
		HttpResponse response;
		try {
			response = executeGet("tc_tickets/"+ticketId);
			Ticket ticket= mapTicket(response);
			Map<String, String> postMetaMap = WordPressPostMetaGateway.readPostMeta(ticketId);	
			if (null != postMetaMap && postMetaMap.size() > 0)
				mapPostMetaToTicket(ticket, postMetaMap);
			return ticket;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private static void mapPostMetaToTicket(Ticket ticket, Map<String, String> postMetaMap) {
		ticket.pricePerTicket= postMetaMap.get("price_per_ticket");
		ticket.quantityAvailable= postMetaMap.get("quantity_available");
		ticket.eventId= postMetaMap.get("event_name");
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class TicketJson {
		public String id;
		public RawAndRendered title;
		public int tickets_sold;
	}


}
