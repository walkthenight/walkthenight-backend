package com.walkthenight.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class MashUpRepositoryTest {
	

	private final MashUpVenueRepository repository= new MashUpVenueRepository();
	
	private static final String STANDARD_DOWNTOWN_LA_VENUE_ID= "412592498769848";
	
	private static final String CATNIP_CLUB_SERIES_ID="1559619647628601";
	
	private static final String CATNIP_CLUB_EVENT_ID = "1699631813606133";
	
	@Test
	public void shouldReturnAListOfVenues() {
		testExistenceAndContentsOfList("Venue", repository.getVenues());
	}
	
//	@Test
//	public void shouldReturnPhotosForVenue() {
//		testExistenceAndContentsOfList("Photo", repository.getPhotos(STANDARD_DOWNTOWN_LA_VENUE_ID));
//	}
	
	@Test
	public void shouldReturnEventsForVenue() {
		testExistenceAndContentsOfList("Event", repository.getEvents(STANDARD_DOWNTOWN_LA_VENUE_ID, "all"));
	}
	
	@Test
	public void shouldReturnPhotosForSeries() {
		testExistenceAndContentsOfList("Photo", repository.getSeriesPhotos(CATNIP_CLUB_SERIES_ID));
	}
	
	@Test
	public void shouldReturnSeriesForEvent() {
		testExistenceAndContentsOfList("Event/Series", repository.getEventSeriesLinks(CATNIP_CLUB_EVENT_ID));
	}
	
	
	
	private <T>  void  testExistenceAndContentsOfList(String entityType, List<T> ts) {
		assertNotNull(entityType + " list should not be null", ts);
		assertTrue(entityType + " list should not be empty", !ts.isEmpty());
	
		for (T t : ts) {
			assertNotNull(entityType + " in list should not be null", t);
		}
	}
	
}
