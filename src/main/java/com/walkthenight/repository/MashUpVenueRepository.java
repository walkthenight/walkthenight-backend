package com.walkthenight.repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.walkthenight.data.Event;
import com.walkthenight.data.Link;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.facebook.FacebookVenueGateway;
import com.walkthenight.foursquare.FoursquareVenueGateway;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;
import com.walkthenight.googleapi.GooglePlacesVenueGateway;

public class MashUpVenueRepository implements VenueRepository {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
	private GooglePlacesVenueGateway googlePlacesVenueGateway= new GooglePlacesVenueGateway();
	private FacebookVenueGateway facebookVenueGateway= new FacebookVenueGateway();
	private FoursquareVenueGateway foursquareVenueGateway= new FoursquareVenueGateway();
	
	@Override
	public List<Venue> getVenues() {
		return spreadsheetGateway.getVenues();
	}

	@Override
	public Venue getVenue(String id) {
		Venue venue= spreadsheetGateway.getVenue(id);
		if (!facebookVenueGateway.enrichVenue(venue))
			if (venue.googlePlaceId != null) {
				googlePlacesVenueGateway.enrichVenue(venue);
		}
		return venue;
	}

	@Override
	public List<Event> getEvents(String venueId) {
		return facebookVenueGateway.getEvents(venueId);
	}

	@Override
	public InputStream getPicture(String venueId) {
		Venue venue= spreadsheetGateway.getVenue(venueId);
		if (null != venue && null != venue.googlePlaceId) {
			return googlePlacesVenueGateway.getPlaceImage(venue.googlePlaceId);
		}
		return null;
	}

	@Override
	public List<Link> getLinks(String venueId) {
		Venue venue= spreadsheetGateway.getVenue(venueId);
		if (null != venue && null != venue.foursquareVenueId) {
			return foursquareVenueGateway.getLinks(venue.foursquareVenueId);
		}
		return new ArrayList<Link>();
	}
	

}
