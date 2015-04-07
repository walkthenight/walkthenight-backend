package com.walkthenight.data;

public class Venue {
	
	private final String id;
	private final String name;
	
	public static class Builder {
		private final String id;
		private final String name;
		
		public Builder(String id, String name) {
			this.id= id;
			this.name= name;
		}
		
		public Venue build() {
			return new Venue(this);
		}
		
	}
	
	private Venue(Builder b) {
		this.id= b.id;
		this.name= b.name;
	}
	
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}
