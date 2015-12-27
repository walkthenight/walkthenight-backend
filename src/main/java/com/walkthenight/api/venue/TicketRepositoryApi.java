package com.walkthenight.api.venue;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Ticket;
import com.walkthenight.data.TicketRepository;
import com.walkthenight.repository.TickeraTicketRepository;

@Path("/tickets")
public class TicketRepositoryApi {
	
	private TicketRepository repository= new TickeraTicketRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public List<Ticket> getTickets() {
		return repository.getTickets();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Ticket getTicket(@PathParam("id") String id) {
		return repository.getTicket(id);
	}
}
