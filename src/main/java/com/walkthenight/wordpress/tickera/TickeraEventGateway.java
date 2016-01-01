package com.walkthenight.wordpress.tickera;

import static com.walkthenight.wordpress.WordPressWebRequestor.executeGet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.api.client.http.HttpResponse;
import com.walkthenight.data.Event;
import com.walkthenight.data.TicketedEvent;
import com.walkthenight.repository.TickeraDateFormat;
import com.walkthenight.wordpress.Iso8601DateParser;
import com.walkthenight.wordpress.RawAndRendered;
import com.walkthenight.wordpress.WordPressPostGateway;
import com.walkthenight.wordpress.WordPressPostGateway.Status;
import com.walkthenight.wordpress.WordPressPostMetaGateway;

public class TickeraEventGateway {

	public String createTicketedEvent(Event event, String ticketId) {
		HttpResponse response;
		
		try {
			response = createPost(event, ticketId);
			final String tickeraEventId = eventId(response);
			writePostMeta(event, tickeraEventId);
			return tickeraEventId;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private void writePostMeta(Event event, final String tickeraEventId) {
		Map<String, String> postMetaMap= new HashMap<>();
		postMetaMap.put("event_location", event.place.name);
		postMetaMap.put("event_date_time", toTickeraDate(event.startTime));
		if (event.endTime != null)
			postMetaMap.put("event_end_date_time", toTickeraDate(event.endTime));
		postMetaMap.put("event_logo_file_url", event.picture);
		
		
		WordPressPostMetaGateway.writePostMeta(tickeraEventId, postMetaMap);
	}

	private String eventId(HttpResponse response) throws IOException, JsonParseException, JsonMappingException {
		String json= response.parseAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		TicketedEventJson eventJson = mapper.readValue(json, typeFactory().constructType(TicketedEventJson.class));
		final String tickeraEventId = eventJson.id;
		return tickeraEventId;
	}
	
	private HttpResponse createPost(Event event, String ticketId) throws IOException {
		final String postContent = "[ticket id=\""+ticketId+"\"] " + event.description;
		return WordPressPostGateway.createPost("tc_events", event.name, postContent, null, Status.PUBLISH);
	}
	
	private String toTickeraDate(String startTime) {
		try {
			Date date= Iso8601DateParser.parse(startTime);
			
			String value= TickeraDateFormat.toString(date);
			System.out.println(value);
			
			return value;
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		} 
	}
	
	public List<TicketedEvent> getTicketedEvents() {
		HttpResponse response;
		try {
			response = executeGet("tc_events");
			return mapTicketedEvents(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private List<TicketedEvent> mapTicketedEvents(HttpResponse response) throws IOException {
		String json = response.parseAsString();
        ObjectMapper mapper = new ObjectMapper();
        try {
       	 TicketedEventJson[] events = mapper.readValue(json, typeFactory().constructArrayType(TicketedEventJson.class));
       	 return mapTicketedEvents(events);
        } catch (IOException e) {
       	 return null;
        }
	}

	private List<TicketedEvent> mapTicketedEvents(TicketedEventJson[] tjs) {
		List<TicketedEvent> events= new ArrayList<>();
		for (TicketedEventJson tj : tjs) {
			TicketedEvent event= mapTicketedEvent(tj);
			events.add(event);
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

	public TicketedEvent getTicketedEvent(String eventId) {
		try {
			TicketedEvent event = getBaseEventData(eventId);
			addPostMetaData(event);
			return event;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private TicketedEvent getBaseEventData(String eventId) throws IOException {
		HttpResponse response= executeGet("tc_events/"+eventId);
		return mapTicketedEvent(response);
	}

	private void addPostMetaData(TicketedEvent event) {
		Map<String, String> postMetaMap = WordPressPostMetaGateway.readPostMeta(event.id);	
		if (null != postMetaMap && postMetaMap.size() > 0)
			mapPostMetaToTicketedEvent(event, postMetaMap);
	}
	
	private TicketedEvent mapTicketedEvent(HttpResponse response) throws IOException {
		String json = response.parseAsString();
        ObjectMapper mapper = new ObjectMapper();
		try {
       	 	TicketedEventJson event = mapper.readValue(json, typeFactory().constructType(TicketedEventJson.class));
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
	
	private TypeFactory typeFactory() {
		return TypeFactory.defaultInstance();
	}

}
