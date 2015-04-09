package com.walkthenight.repository;

import java.io.InputStream;
import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.facebook.FacebookVenueGateway;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;
import com.walkthenight.googleapi.GooglePlacesVenueGateway;

public class MashUpVenueRepository implements VenueRepository {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
	private GooglePlacesVenueGateway googlePlacesVenueGateway= new GooglePlacesVenueGateway();
	private FacebookVenueGateway facebookVenueGateway= new FacebookVenueGateway();
	
	@Override
	public List<Venue> getVenues() {
		return spreadsheetGateway.getVenues();
	}

	@Override
	public Venue getVenue(String id) {
		return null;
	}

	@Override
	public List<Event> getEvents(String venueId) {
		return facebookVenueGateway.getEvents(venueId);
	}

	@Override
	public InputStream getPicture(String venueId) {
		Venue venue= spreadsheetGateway.getVenue(venueId);
		if (null != venue && null != venue.getGooglePlaceId()) {
			return googlePlacesVenueGateway.getPlaceImage(venue.getGooglePlaceId());
		}
		return null;
	}

}
