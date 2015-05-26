package com.walkthenight.data;


public class Venue {
	
	private final String id;
	private final String name;
	private final String googlePlaceId;
	private final String foursquareVenueId;
	private final String instagramPlaceId;
	private final double latitude;
	private final double longitude;
	private final String twitterHandle;
	private final String facebookUrl;
	private final String instagramHandle;
	
	public static class Builder {
		private final String id;
		private final String name;
		private String googlePlaceId;
		private String foursquareVenueId;
		private String instagramPlaceId;
		private double latitude;
		private double longitude;
		private String twitterHandle;
		private String facebookUrl;
		private String instagramHandle;
		
		public Builder(String id, String name) {
			this.id= id;
			this.name= name;
		}
		
		public Venue build() {
			return new Venue(this);
		}

		public Builder googlePlaceId(String value) {
			this.googlePlaceId= value;
			return this;
		}
		
		public Builder foursquareVenueId(String value) {
			this.foursquareVenueId= value;
			return this;
		}

		public Builder latitude(double value) {
			this.latitude= value;
			return this;
		}
	
		public Builder longitude(double value) {
			this.longitude= value;
			return this;
		}	
		
		public Builder instagramHandle(String value) {
			this.instagramHandle= value;
			return this;
		}	
		
		public Builder instagramPlaceId(String value) {
			this.instagramPlaceId= value;
			return this;
		}	
		
		public Builder twitterHandle(String value) {
			this.twitterHandle= value;
			return this;
		}	
	}
		
	private Venue(Builder b) {
		this.id= b.id;
		this.name= b.name;
		this.googlePlaceId= b.googlePlaceId;
		this.foursquareVenueId= b.foursquareVenueId;
		this.instagramHandle= b.instagramHandle;
		this.instagramPlaceId= b.instagramPlaceId;
		this.facebookUrl= b.facebookUrl;
		this.latitude= b.latitude;
		this.longitude= b.longitude;
		this.twitterHandle= b.twitterHandle;
	}
	
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getGooglePlaceId() {
		return this.googlePlaceId;
	}

	public String getFoursquareVenueId() {
		return this.foursquareVenueId;
	}
	
	public String getInstagramHandle() {
		return this.instagramHandle;
	}
	
	public String getInstagramPlaceId() {
		return this.instagramPlaceId;
	}
	
	public String getFacebookUrl() {
		return this.facebookUrl;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public String twitterHandle() {
		return this.twitterHandle;
	}
}
