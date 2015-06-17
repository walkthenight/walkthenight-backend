package com.walkthenight.data;

import java.io.Serializable;
import java.util.List;


public class Venue implements Serializable {
	
	private static final long serialVersionUID = 5371510401441710916L;
	public static class Period implements Serializable {
		
		private static final long serialVersionUID = 1444288565162786709L;

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
	public String wtnUrl;

	
}
