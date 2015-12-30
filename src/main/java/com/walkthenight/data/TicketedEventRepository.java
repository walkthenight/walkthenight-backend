package com.walkthenight.data;

import java.util.List;

public interface TicketedEventRepository {

	List<TicketedEvent> getTicketedEvents(String fbAccessToken);

	TicketedEvent getTicketedEvent(String fbAccessToken, String id);

}
