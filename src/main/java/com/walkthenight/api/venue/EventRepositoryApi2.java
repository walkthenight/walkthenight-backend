package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Event;
import com.walkthenight.data.EventRepository;
import com.walkthenight.repository.CachedVenueRepository;
import com.walkthenight.repository.MashUpVenueRepository;

@Path("/events")
public class EventRepositoryApi2 {
	private EventRepository repository= new CachedVenueRepository(new MashUpVenueRepository());

	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Event getEvent(@PathParam("id") String id) {
		return repository.getEvent(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}/series")
	public List<String> getEventSeries(@PathParam("id") String id) {
		return repository.getEventSeriesLinks(id);
	}
}

