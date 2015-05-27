package com.walkthenight.googleapi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Photo;
import se.walkercrou.places.Place;

import com.walkthenight.data.Venue;
import com.walkthenight.data.Venue.Period;



public class GooglePlacesVenueGateway {
	private static final String API_KEY= "AIzaSyCFBPZip3ZwcXGOOzvSBV6-f42dOpoEKtI";
	
	private GooglePlaces googlePlacesService= new GooglePlaces(API_KEY, new GoogleUrlFetchRequestHandler());
	
	public InputStream getPlaceImage(String googlePlaceId) {
		
		if (null == googlePlaceId) {
			return null;
		} else {
			Place place= googlePlacesService.getPlaceById(googlePlaceId);
			List<Photo> photos = place.getPhotos();
			if (photos.size() > 0) {
				Photo photo = photos.get(new Random().nextInt(photos.size()));
				return photo.download().getInputStream();
			} else {
				return null;
			}
		}
		
	}

	public void enrichVenue(Venue venue) {
		Place place= googlePlacesService.getPlaceById(venue.googlePlaceId);
		venue.streetAddress= place.getAddress();
		venue.phoneNumber= place.getPhoneNumber();
		venue.website= place.getWebsite();
		venue.hours= buildHours(place.getHours());
	}

	private List<Period> buildHours(
			se.walkercrou.places.Hours gh) {
		
		List<Period> periods= new ArrayList<Period>();
		
		for (se.walkercrou.places.Hours.Period gPeriod : gh.getPeriods())
			periods.add(buildPeriod(gPeriod));
		
		return periods;
	}

	private Period buildPeriod(se.walkercrou.places.Hours.Period gPeriod) {
		Period p= new Period();
	    p.openingDay= dayOf(gPeriod.getOpeningDay().toString());
	    p.openingTime= gPeriod.getOpeningTime();
	    p.closingTime= gPeriod.getClosingTime();
		return p;
	}

	private String dayOf(String day) {
		return StringUtils.capitalize(day.toLowerCase());
	}
}
