package com.walkthenight.googleapi;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Photo;
import se.walkercrou.places.Place;



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
}
