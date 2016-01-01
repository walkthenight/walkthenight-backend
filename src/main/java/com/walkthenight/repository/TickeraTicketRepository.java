package com.walkthenight.repository;

import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.EventRepository;
import com.walkthenight.data.Ticket;
import com.walkthenight.data.TicketRepository;
import com.walkthenight.data.TicketedEvent;
import com.walkthenight.data.TicketedEventRepository;
import com.walkthenight.data.TicketedEventSpec;
import com.walkthenight.facebook.FacebookGateway;
import com.walkthenight.wordpress.WordPressPageGateway;
import com.walkthenight.wordpress.tickera.TickeraEventGateway;
import com.walkthenight.wordpress.tickera.TickeraTicketGateway;

public class TickeraTicketRepository implements TicketRepository, TicketedEventRepository {

	private EventRepository eventRepository= new CachedVenueRepository(new MashUpVenueRepository());
	private TickeraEventGateway eventGateway= new TickeraEventGateway();
	private TickeraTicketGateway ticketGateway= new TickeraTicketGateway();
	private WordPressPageGateway pageGateway= new WordPressPageGateway();
	private FacebookGateway facebookGateway= new FacebookGateway();
	

	public void createTicketedEvent(String fbAccessToken, TicketedEventSpec spec) {
		checkAccessToken(fbAccessToken);
		Event event= eventRepository.getEvent(spec.facebookEventId);
		String ticketId= ticketGateway.createTicket(spec.ticketDescription, spec.price, spec.quantity);
		String tickeraEventId= eventGateway.createTicketedEvent(event, ticketId);
		ticketGateway.updateTicketWithEventId(ticketId, tickeraEventId);
		String pageSlug= pageGateway.createEventPage(event, ticketId); 
		eventRepository.updateEvent(spec.facebookEventId, pageSlug, tickeraEventId, ticketId);
	}


	@Override
	public List<TicketedEvent> getTicketedEvents(String fbAccessToken) {
		checkAccessToken(fbAccessToken);
		return eventGateway.getTicketedEvents();
	}

	private static void checkAccessToken(String fbAccessToken) {
		//:TODO
	}

	@Override
	public TicketedEvent getTicketedEvent(String fbAccessToken, String id) {
		checkAccessToken(fbAccessToken);
		return eventGateway.getTicketedEvent(id);
	}


	@Override
	public List<Ticket> getTickets(String fbAccessToken) {
		checkAccessToken(fbAccessToken);
		return ticketGateway.getTickets();
	}


	@Override
	public Ticket getTicket(String fbAccessToken, String ticketId) {
		checkAccessToken(fbAccessToken);
		return ticketGateway.getTicket(ticketId);
	}
	
	
	

}
