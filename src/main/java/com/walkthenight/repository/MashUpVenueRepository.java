package com.walkthenight.repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.EventRepository;
import com.walkthenight.data.Link;
import com.walkthenight.data.Series;
import com.walkthenight.data.SeriesRepository;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.facebook.FacebookGateway;
import com.walkthenight.foursquare.FoursquareVenueGateway;
import com.walkthenight.googleapi.GooglePlacesVenueGateway;
import com.walkthenight.googleapi.worksheets.EventWorksheet;
import com.walkthenight.googleapi.worksheets.SeriesWorksheet;
import com.walkthenight.googleapi.worksheets.VenueWorksheet;
import com.walkthenight.instagram.InstagramGateway;

public class MashUpVenueRepository implements VenueRepository, SeriesRepository, EventRepository {

	private VenueWorksheet venueWorksheet= new VenueWorksheet();
	private SeriesWorksheet seriesWorksheet= new SeriesWorksheet();
	private EventWorksheet eventWorksheet= new EventWorksheet();
	
	private GooglePlacesVenueGateway googlePlacesVenueGateway= new GooglePlacesVenueGateway();
	private FacebookGateway facebookVenueGateway= new FacebookGateway();
	private FoursquareVenueGateway foursquareVenueGateway= new FoursquareVenueGateway();
	private InstagramGateway instagramGateway= new InstagramGateway();
	
	@Override
	public List<Venue> getVenues() {
		return venueWorksheet.getVenues();
	}

	@Override
	public Venue getVenue(String id) {
		Venue venue= venueWorksheet.getVenue(id);
		if (null != venue && !facebookVenueGateway.enrichVenue(venue))
			if (venue.googlePlaceId != null) {
				googlePlacesVenueGateway.enrichVenue(venue);
		}
		return venue;
	}

	@Override
	public List<Event> getEvents(String id, String timeframe) {
		if (null == timeframe) 
			timeframe="all";
		
		List<Event> events= facebookVenueGateway.getEvents(id, timeframe);
		
		for (Event event : events) {
			eventWorksheet.augmentEvent(event);
		}
		
		return events;
	}

	@Override
	public InputStream getPicture(String venueId) {
		Venue venue= venueWorksheet.getVenue(venueId);
		if (null != venue && null != venue.googlePlaceId) {
			return googlePlacesVenueGateway.getPlaceImage(venue.googlePlaceId);
		}
		return null;
	}

	@Override
	public List<Link> getLinks(String venueId) {
		Venue venue= venueWorksheet.getVenue(venueId);
		if (null != venue && null != venue.foursquareVenueId) {
			return foursquareVenueGateway.getLinks(venue.foursquareVenueId);
		}
		return new ArrayList<Link>();
	}

	@Override
	public List<String> getPhotos(String venueId) {
		Venue venue= venueWorksheet.getVenue(venueId);
		List<String> photos= new ArrayList<String>();
		if (null != venue && null != venue.instagramPlaceId) {
			photos.addAll(instagramGateway.getPhotosFromLocation(venue.instagramPlaceId));
		}
		if (null != venue && null != venue.instagramHandle && !"-".equals(venue.instagramHandle)) {
			//:TODO why isn't this working? photos.addAll(instagramGateway.getPhotosFromHandle(venue.instagramHandle));
		}
		//:TODO facebook photos
		return photos;
	}

	@Override
	public Series getSeries(String id) {
		return seriesWorksheet.getSeries(id);
	}

	@Override
	public List<String> getSeriesPhotos(String seriesId) {
		Series series= seriesWorksheet.getSeries(seriesId);
		List<String> photos= new ArrayList<String>();
		
		if (null != series) {
			photos.addAll(facebookVenueGateway.getPhotos(seriesId));
		}
		
		return photos;
	}

	public Event getEvent(String id) {
		Event event= facebookVenueGateway.getEvent(id);
		if (null != event) {
			eventWorksheet.augmentEvent(event);
		}
		return event;
	}

	@Override
	public List<String> getEventSeriesLinks(String eventId) {
		return facebookVenueGateway.getEventSeriesLinks(eventId);
	}


}
