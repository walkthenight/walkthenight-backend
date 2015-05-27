package com.walkthenight.data;

import java.util.List;


public class Venue {
	
	public static class Period {
		
		public static final String[] DAYS= new String[] {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

		public String openingDay;
		public String openingTime;
		public String closingTime;
		
	}
	
	public String id;
	public String name;
	public String googlePlaceId;
	public String foursquareVenueId;
	public String instagramPlaceId;
	public double latitude;
	public double longitude;
	public String twitterHandle;
	public String facebookUrl;
	public String instagramHandle;
	public String streetAddress;
	public String phoneNumber;
	public String website;
	public List<Period> hours;

	
}
