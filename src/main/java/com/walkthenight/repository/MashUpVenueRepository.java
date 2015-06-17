package com.walkthenight.repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.Link;
import com.walkthenight.data.Series;
import com.walkthenight.data.SeriesRepository;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.facebook.FacebookGateway;
import com.walkthenight.foursquare.FoursquareVenueGateway;
import com.walkthenight.googleapi.GoogleDriveSeriesSpreadsheetGateway;
import com.walkthenight.googleapi.GoogleDriveVenueSpreadsheetGateway;
import com.walkthenight.googleapi.GooglePlacesVenueGateway;
import com.walkthenight.instagram.InstagramGateway;

public class MashUpVenueRepository implements VenueRepository, SeriesRepository {

	private GoogleDriveVenueSpreadsheetGateway venueSpreadsheetGateway= new GoogleDriveVenueSpreadsheetGateway();
	private GoogleDriveSeriesSpreadsheetGateway seriesSpreadsheetGateway= new GoogleDriveSeriesSpreadsheetGateway();

	private GooglePlacesVenueGateway googlePlacesVenueGateway= new GooglePlacesVenueGateway();
	private FacebookGateway facebookVenueGateway= new FacebookGateway();
	private FoursquareVenueGateway foursquareVenueGateway= new FoursquareVenueGateway();
	private InstagramGateway instagramGateway= new InstagramGateway();
	
	@Override
	public List<Venue> getVenues() {
		return venueSpreadsheetGateway.getVenues();
	}

	@Override
	public Venue getVenue(String id) {
		Venue venue= venueSpreadsheetGateway.getVenue(id);
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
		
		return facebookVenueGateway.getEvents(id, timeframe);
		//:TODO enrich events with link/URI from WordPress
	}

	@Override
	public InputStream getPicture(String venueId) {
		Venue venue= venueSpreadsheetGateway.getVenue(venueId);
		if (null != venue && null != venue.googlePlaceId) {
			return googlePlacesVenueGateway.getPlaceImage(venue.googlePlaceId);
		}
		return null;
	}

	@Override
	public List<Link> getLinks(String venueId) {
		Venue venue= venueSpreadsheetGateway.getVenue(venueId);
		if (null != venue && null != venue.foursquareVenueId) {
			return foursquareVenueGateway.getLinks(venue.foursquareVenueId);
		}
		return new ArrayList<Link>();
	}

	@Override
	public List<String> getPhotos(String venueId) {
		Venue venue= venueSpreadsheetGateway.getVenue(venueId);
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
		return seriesSpreadsheetGateway.getSeries(id);
	}


}
