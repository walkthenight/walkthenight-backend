package com.walkthenight.data;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat; 

public class Event implements Serializable, Comparable<Event> {
	private static final long serialVersionUID = -4378271723402753852L;
	public String id;
	public String startTime;
	public String name;
	public Place place;
	public String price;
	public String endTime;
	public String url;
	public String timezone;
	public String ticketUri;
	public String picture;
	
	public static class Place implements Serializable {
		
		private static final long serialVersionUID = 27885340488550384L;
		public String id;
		public String name;
		public String latitude;
		public String longitude;
	}
	
	@Override
	public int compareTo(Event e) {
		return dateOf(this.startTime).compareTo(dateOf(e.startTime));
	}
	
	public Date startDate() {
		return dateOf(startTime);
	}
	
	private Date dateOf(String dateString) {
		DateTimeFormatter parser;
		if (isDateOnly(dateString)) {
			 parser= DateTimeFormat.forPattern("yyyy-MM-dd");
		} else {
			 parser = ISODateTimeFormat.dateTimeNoMillis();
		}
		 return parser.parseDateTime(dateString).toDate();
	}

	private boolean isDateOnly(String dateString) {
		return dateString.length() == 10;
	}
}
