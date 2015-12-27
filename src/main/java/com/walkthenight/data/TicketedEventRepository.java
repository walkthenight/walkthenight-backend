package com.walkthenight.data;

import java.util.List;

public interface TicketedEventRepository {

	List<TicketedEvent> getTicketedEvents();

	TicketedEvent getTicketedEvent(String id);

}
