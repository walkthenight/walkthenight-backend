package com.walkthenight.data;

import java.util.List;

public interface TicketRepository {
	List<Ticket> getTickets();
	Ticket getTicket(String ticketId);
}
