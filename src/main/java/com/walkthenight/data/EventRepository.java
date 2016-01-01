package com.walkthenight.data;

import java.util.List;

public interface EventRepository {

	Event getEvent(String id);

	List<String> getEventSeriesLinks(String id);

	void updateEvent(String facebookEventId, String pageSlug, String tickeraEventId, String ticketId);

}
