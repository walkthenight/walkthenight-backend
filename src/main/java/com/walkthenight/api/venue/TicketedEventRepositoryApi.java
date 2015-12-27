package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Ticket;
import com.walkthenight.data.TicketRepository;
import com.walkthenight.data.TicketedEvent;
import com.walkthenight.data.TicketedEventRepository;
import com.walkthenight.repository.TickeraTicketRepository;

@Path("/ticketed-events")
public class TicketedEventRepositoryApi {
	
	private TicketedEventRepository repository= new TickeraTicketRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public List<TicketedEvent> getTicketedEvents() {
		return repository.getTicketedEvents();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public TicketedEvent getTicketedEvent(@PathParam("id") String id) {
		return repository.getTicketedEvent(id);
	}
}
