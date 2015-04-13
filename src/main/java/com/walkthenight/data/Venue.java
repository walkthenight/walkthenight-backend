package com.walkthenight.data;


public class Venue {
	
	private final String id;
	private final String name;
	private String googlePlaceId;
	private String foursquareVenueId;
	
	public static class Builder {
		private final String id;
		private final String name;
		private String googlePlaceId;
		public String foursquareVenueId;
		
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
		
	}
	
	private Venue(Builder b) {
		this.id= b.id;
		this.name= b.name;
		this.googlePlaceId= b.googlePlaceId;
		this.foursquareVenueId= b.foursquareVenueId;
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
}
