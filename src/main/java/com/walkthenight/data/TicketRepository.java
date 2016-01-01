package com.walkthenight.data;

import java.util.List;

public interface TicketRepository {
	List<Ticket> getTickets(String fbAccessToken);
	Ticket getTicket(String fbAccessToken, String ticketId);
}
