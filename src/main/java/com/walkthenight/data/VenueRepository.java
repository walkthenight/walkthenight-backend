package com.walkthenight.data;

import java.io.InputStream;
import java.util.List;


public interface VenueRepository {

	List<Venue> getVenues();

	Venue getVenue(String id);

	List<Event> getEvents(String venueId);

	InputStream getPicture(String venueId);

	List<Link> getLinks(String venueId);

	List<String> getPhotos(String venueId);

}
