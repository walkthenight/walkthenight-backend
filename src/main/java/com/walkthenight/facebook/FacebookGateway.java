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

import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Photo;
import com.restfb.types.User;
import com.walkthenight.data.Event;
import com.walkthenight.data.Event.Place;
import com.walkthenight.data.Venue;

import static com.restfb.Parameter.with;

public class FacebookGateway {
	private static final Logger LOG= Logger.getLogger("WalkTheNightApplication");
	
	private static final Version API_VERSION= Version.VERSION_2_4;
	
	private final FacebookClient fbClient;
	
	public FacebookGateway() {
		this.fbClient= new DefaultFacebookClient(FacebookConfig.FB_ACCESS_TOKEN, API_VERSION);
	}
	
	public FacebookGateway(String accessToken) {
		this.fbClient= new DefaultFacebookClient(accessToken, API_VERSION);
	}
	
	public void checkAccess(String email) {
		User user= fbClient.fetchObject("me", User.class, with("fields","email"));
		if (!user.getEmail().equals(email)) {
			throw new SecurityException("Access token for " + email + "not valid");
		}
	}
	
	  public List<Event> getEvents(String id, String timeframe) {
			JsonObject connection = fbClient.fetchObject(
					id+"/events", 
					JsonObject.class, 
					with("fields", "start_time, end_time, timezone, name, place, description"));
			
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

	private static boolean shouldAddEvent(Event e, String timeframe) {
		boolean shouldAddEvent= true;
		if ("past".equals(timeframe))
			shouldAddEvent= e.startDate().before(new Date());
		else if ("future".equals(timeframe))
			shouldAddEvent= e.startDate().after(new Date());
		return shouldAddEvent;
	}


	public boolean enrichVenue(Venue venue) {
		try {
			
			JsonObject venuesConnection= fbClient.fetchObject(
					venue.id, 
					JsonObject.class,
					with("fields", "hours,phone,website,location"));
			
			venue.phoneNumber= string(venuesConnection, "phone");
			venue.website= string(venuesConnection, "website");
			venue.streetAddress= streetAddressFrom(object(venuesConnection, "location"));
			venue.hours= FacebookGraphHoursParser.buildHours(object(venuesConnection, "hours"));
			return true;
		} catch (FacebookGraphException fge) {
			LOG.log(Level.WARNING, "Facebook Graph Exception: ", fge);
			return false;
		}
		
	}
	
	public List<String> getPhotos(String pageId) {
		final List<String> photos= new ArrayList<String>();
		
		Connection<Photo> cn= fbClient.fetchConnection(
				pageId+"/photos", 
				Photo.class,
				with("fields", "source"));
		
		for (List<Photo> photoPage : cn)
			  for (Photo photo : photoPage)
				  photos.add(photo.getSource());
		
		return photos;
	}


	public Event getEvent(String id) {
		JsonObject e= fbClient.fetchObject(
				id, 
				JsonObject.class,
				with("fields", "start_time,end_time,timezone,name,place,cover,ticket_uri,picture,description"));
		
		Event event= eventFrom(e);
		
	// event.ticketUri= e.getTicketUri();
	    event.picture= pictureFrom(object(e, "cover"));
		
		return event;
	}


	public List<String> getEventSeriesLinks(String eventId) {
		final List<String> eventSeriesLinks= new ArrayList<String>();
		
		JsonObject result= fbClient.fetchObject(
				eventId+"/admins", 
				JsonObject.class);
		
		JsonArray admins= array(result, "data");
		
		for (int i= 0; i < admins.length(); i++) {
			String id= string(admins.getJsonObject(i), "id");
			if (isPage(id)) {
				eventSeriesLinks.add(id);
			}
		}
	
		
		return eventSeriesLinks;
	}

	private boolean isPage(String id) {
		JsonObject result= fbClient.fetchObject(
				id, 
				JsonObject.class,
				with("metadata", "1"));
		
		JsonObject metadata= object(result, "metadata");
		
		String type= string(metadata, "type");
		
		return "page".equals(type);
	}
	
	private static Event eventFrom(JsonObject o) {
		Event e= new Event();
		
		e.id= o.getString("id");
		e.startTime= o.getString("start_time");
		e.endTime= string(o, "end_time");
		e.place= placeFrom(object(o,"place")); 
		e.name= o.getString("name");
		e.timezone= string(o, "timezone");
		e.description= string(o, "description");
		return e;
	}
	
	private static Place placeFrom(JsonObject o) {
		if (o == null) {
			return null;
		}
		
		Place p= new Place();
		p.id= string(o, "id");
		p.name= string(o, "name");
		
		JsonObject location= object(o, "location");
		if (null != location) {
			p.latitude= string(location, "latitude");
			p.longitude= string(location, "longitude");
			p.streetAddress= streetAddressFrom(location);
		}
		
		
		
		return p;
	}

	
	private static String streetAddressFrom(JsonObject location) {
		if (null == location) return null;
		
		if (string(location, "street") == null) {
			return string(location, "city") + comma(string(location, "zip"));
		} else {
			return string(location, "street") + comma(string(location, "city")) + comma(string(location, "zip"));
		}
		
		//:TODO doesn't feel optimal...
	}

	private static String comma(String s) {
		return s == null ? "" : ", " + s;
	}

	private static String string(JsonObject jo, String key) {
		return jo.has(key) ? jo.getString(key) : null;
	}
	
	private static JsonObject object(JsonObject jo, String key) {
		return jo.has(key) ? jo.getJsonObject(key) : null;
	}
	
	private static JsonArray array(JsonObject jo, String key) {
		return jo.has(key) ? jo.getJsonArray(key) : null;
	}
	
	private static String pictureFrom(JsonObject o) {
	
		return o.getString("source");
	}

}
