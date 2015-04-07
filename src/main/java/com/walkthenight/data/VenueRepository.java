package com.walkthenight.data;

import java.util.List;


public interface VenueRepository {

	List<Venue> getVenues();

	Venue getVenue(String id);

	List<Event> getEvents(String venueId);

}
