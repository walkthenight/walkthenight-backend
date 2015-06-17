package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Event;
import com.walkthenight.data.Series;
import com.walkthenight.data.SeriesRepository;
import com.walkthenight.repository.CachedVenueRepository;
import com.walkthenight.repository.MashUpVenueRepository;

@Path("/series")
public class SeriesRepositoryApi {
	private SeriesRepository repository= new CachedVenueRepository(new MashUpVenueRepository());

	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Series getSeries(@PathParam("id") String id) {
		return repository.getSeries(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}/events")
	public List<Event> getEvents(@PathParam("id") String seriesId, @QueryParam("timeframe") String timeframe) {
		return repository.getEvents(seriesId, timeframe);
	}
}

