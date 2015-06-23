package com.walkthenight.facebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Photo;
import com.walkthenight.data.Event;
import com.walkthenight.data.Event.Place;
import com.walkthenight.data.Venue;

public class FacebookGateway {
	private static final Logger LOG= Logger.getLogger("WalkTheNightApplication");
	private final FacebookClient fbClient= new DefaultFacebookClient(FacebookConfig.FB_ACCESS_TOKEN, Version.VERSION_2_3);
	
	  public List<Event> getEvents(String id, String timeframe) {
			JsonObject connection = fbClient.fetchObject(id+"/events", JsonObject.class,Parameter.with("fields", "start_time, end_time, timezone, name, place"));
			
			List<Event> events= new ArrayList<Event>();
			
			JsonArray jsonEvents= connection.getJsonArray("data");
			
			for (int i= 0; i < jsonEvents.length(); i++) {
				Event e = eventFrom(jsonEvents.getJsonObject(i));
				if (shouldAddEvent(e, timeframe))
					events.add(e);
			}
			
			Collections.sort(events);
			
			return events;
		}

	private boolean shouldAddEvent(Event e, String timeframe) {
		boolean shouldAddEvent= true;
		if ("past".equals(timeframe))
			shouldAddEvent= e.startDate().before(new Date());
		else if ("future".equals(timeframe))
			shouldAddEvent= e.startDate().after(new Date());
		return shouldAddEvent;
	}

	private Event eventFrom(JsonObject o) {
		Event e= new Event();
		
		e.id= o.getString("id");
		e.startTime= o.getString("start_time");
		e.endTime= getString(o, "end_time");
		e.place= getPlace(getJsonObject(o,"place")); 
		e.name= o.getString("name");
		e.timezone= getString(o, "timezone");
		return e;
	}
	
	private Place getPlace(JsonObject o) {
		if (o == null) {
			return null;
		}
		
		Place p= new Place();
		p.id= getString(o, "id");
		p.name= getString(o, "name");
		
		JsonObject location= getJsonObject(o, "location");
		if (null != location) {
			p.latitude= getString(location, "latitude");
			p.longitude= getString(location, "longitude");
		}
		return p;
	}

	public boolean enrichVenue(Venue venue) {
		try {
			
			JsonObject venuesConnection= fbClient.fetchObject(venue.id, JsonObject.class);
		
			venue.phoneNumber= getString(venuesConnection, "phone");
			venue.website= getString(venuesConnection, "website");
			venue.streetAddress= buildStreetAddress(getJsonObject(venuesConnection, "location"));
			venue.hours= FacebookGraphHoursParser.buildHours(getJsonObject(venuesConnection, "hours"));
			return true;
		} catch (FacebookGraphException fge) {
			LOG.log(Level.WARNING, "Facebook Graph Exception: ", fge);
			return false;
		}
		
	}
	
	public List<String> getPhotos(String pageId) {
		final List<String> photos= new ArrayList<String>();
		
		Connection<Photo> cn= fbClient.fetchConnection(pageId+"/photos", Photo.class);
		
		for (List<Photo> photoPage : cn)
			  for (Photo photo : photoPage)
				  photos.add(photo.getSource());
		
		return photos;
	}

	private String buildStreetAddress(JsonObject location) {
		if (null == location) return null;
		return getString(location, "street") + comma(getString(location, "city")) + comma(getString(location, "zip"));
	}

	private String comma(String s) {
		return s == null ? "" : ", " + s;
	}

	private String getString(JsonObject jo, String key) {
		return jo.has(key) ? jo.getString(key) : null;
	}
	
	private JsonObject getJsonObject(JsonObject jo, String key) {
		return jo.has(key) ? jo.getJsonObject(key) : null;
	}
}
