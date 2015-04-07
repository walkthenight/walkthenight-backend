package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Event;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.googleapi.GoogleDriveVenueRepository;

@Path("/venues")
public class VenueRepositoryApi {
	private VenueRepository repository= new GoogleDriveVenueRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Venue> getVenues() {
		return repository.getVenues();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Venue getVenue(@PathParam("id") String id) {
		return repository.getVenue(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}/events")
	public List<Event> getEvents(@PathParam("id") String venueId) {
		return repository.getEvents(venueId);
	}
} 
