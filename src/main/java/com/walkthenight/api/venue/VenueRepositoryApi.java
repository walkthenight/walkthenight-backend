package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;

@Path("/venues")
public class VenueRepositoryApi {
	private VenueRepository repository= null;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Venue> getVenues() {
		return repository.getVenues();
	}
} 
