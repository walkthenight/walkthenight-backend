package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.TicketedEvent;
import com.walkthenight.data.TicketedEventRepository;
import com.walkthenight.data.TicketedEventSpec;
import com.walkthenight.repository.TickeraTicketRepository;

@Path("/ticketed-events")
public class TicketedEventRepositoryApi {
	
	private TicketedEventRepository repository= new TickeraTicketRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public List<TicketedEvent> getTicketedEvents(@QueryParam("access_token") String accessToken) {
		return repository.getTicketedEvents(accessToken);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public TicketedEvent getTicketedEvent(@PathParam("id") String id, @QueryParam("access_token") String accessToken) {
		return repository.getTicketedEvent(id, accessToken);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void createTicketedEvent(TicketedEventSpec spec) {
		repository.createTicketedEvent(spec);
	}
}
