package com.walkthenight.data;

import java.util.List;

public interface EventRepository {

	Event getEvent(String id);

	List<String> getEventSeriesLinks(String id);

}
