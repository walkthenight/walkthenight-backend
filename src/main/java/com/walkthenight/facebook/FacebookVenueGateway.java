package com.walkthenight.facebook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.json.JsonObject;
import com.walkthenight.data.Event;
import com.walkthenight.data.Venue;

public class FacebookVenueGateway {
	private final FacebookClient fbClient= new DefaultFacebookClient(FacebookConfig.FB_ACCESS_TOKEN, Version.VERSION_2_2);
	
	  public List<Event> getEvents(String id) {
			Connection<com.restfb.types.Event> connection = fbClient.fetchConnection(id+"/events", com.restfb.types.Event.class);
			
			List<Event> events= new ArrayList<Event>();
			
			for (com.restfb.types.Event e : connection.getData()) 
				events.add(eventFrom(e));
			
			return events;
		}

	private Event eventFrom(com.restfb.types.Event fbEvent) {
		Event e= new Event();
		
		e.id= fbEvent.getId();
		e.startTime= getISO8601StringForDate(fbEvent.getStartTime());
		e.location= fbEvent.getLocation();
		e.name= fbEvent.getName();
		return e;
	}
	
	private static String getISO8601StringForDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	public boolean enrichVenue(Venue venue) {
		try {
			JsonObject venuesConnection= fbClient.fetchObject(venue.id, JsonObject.class);
		
			venue.phoneNumber= venuesConnection.getString("phone");
			venue.website= venuesConnection.getString("website");
			venue.streetAddress= buildStreetAddress(venuesConnection.getJsonObject("location"));
			venue.hours= FacebookGraphHoursParser.buildHours(venuesConnection.getJsonObject("hours"));
			return true;
		} catch (FacebookGraphException fge) {
			//:FIXME LOG.warn("Facebook Graph Exception: ", fge);
			return false;
		}
		
	}

	

	private String buildStreetAddress(JsonObject location) {
		return location.getString("street") + ", " + location.getString("city") + ", " + location.getString("zip");
	}
}
