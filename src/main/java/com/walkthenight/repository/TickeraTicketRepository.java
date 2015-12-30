package com.walkthenight.repository;

import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.EventRepository;
import com.walkthenight.data.Ticket;
import com.walkthenight.data.TicketRepository;
import com.walkthenight.data.TicketedEvent;
import com.walkthenight.data.TicketedEventRepository;
import com.walkthenight.googleapi.worksheets.EventWorksheet;
import com.walkthenight.wordpress.WordPressPageGateway;
import com.walkthenight.wordpress.tickera.TickeraEventGateway;
import com.walkthenight.wordpress.tickera.TickeraTicketGateway;

public class TickeraTicketRepository implements TicketRepository, TicketedEventRepository {

	private EventRepository eventRepository= new MashUpVenueRepository();
	private TickeraEventGateway eventGateway= new TickeraEventGateway();
	private TickeraTicketGateway ticketGateway= new TickeraTicketGateway();
	private WordPressPageGateway pageGateway= new WordPressPageGateway();
	private EventWorksheet eventWorksheet= new EventWorksheet();
	
	public static void main(String[] args) throws Exception {
		TickeraTicketRepository repository= new TickeraTicketRepository();
		repository.createTicketedEvent("925950214165994", "Admission", 10, 100);
	}

	public void createTicketedEvent(String facebookEventId, String ticketDescription, int price, int quantity) {
		Event event= eventRepository.getEvent(facebookEventId);
		String ticketId= ticketGateway.createTicket(ticketDescription, price, quantity);
		String tickeraEventId= eventGateway.createTicketedEvent(event, ticketId);
		ticketGateway.updateTicketWithEventId(ticketId, tickeraEventId);
		String pageSlug= pageGateway.createEventPage(event, ticketId); 
		eventWorksheet.addWtnManagedEvent(facebookEventId, pageSlug, tickeraEventId, ticketId);
	}


	@Override
	public List<TicketedEvent> getTicketedEvents(String fbAccessToken) {
		return eventGateway.getTicketedEvents(fbAccessToken);
	}

	@Override
	public TicketedEvent getTicketedEvent(String fbAccessToken, String id) {
		return eventGateway.getTicketedEvent(id, fbAccessToken);
	}


	@Override
	public List<Ticket> getTickets() {
		return ticketGateway.getTickets();
	}


	@Override
	public Ticket getTicket(String ticketId) {
		return ticketGateway.getTicket(ticketId);
	}
	
	
	

}
