package com.walkthenight.data;

import java.util.List;

public interface SeriesRepository {

	public Series getSeries(String id);
	
	public List<Event> getEvents(String seriesId, String timeframe);
	
	public List<String> getSeriesPhotos(String seriesId);

}
