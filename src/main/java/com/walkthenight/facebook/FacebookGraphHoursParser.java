package com.walkthenight.facebook;

import java.util.ArrayList;
import java.util.List;

import com.restfb.json.JsonObject;
import com.walkthenight.data.Venue.Period;

public class FacebookGraphHoursParser {

	private static String[] days= new String[] {"mon","tue","wed","thu","fri","sat","sun"};
	
	public static List<Period> buildHours(JsonObject fbHours) {
		List<Period> periods= new ArrayList<Period>();
		
		for (int day= 0; day < days.length; day++) 
			for (int i= 1; i <= 4; i++)  // NOTE - assumption of up to four opening hours
				if (fbHours.has(openKey(day, i))) 
					periods.add(buildPeriod(fbHours, day, i));
				
		return periods;
	}

	private static String openKey(int day, int index) {
		return keyPrefix(day, index) + "_open";
	}
	
	private static String closeKey(int day, int index) {
		return keyPrefix(day, index) + "_close";
	}

	private static String keyPrefix(int day, int index) {
		return days[day]+"_"+index;
	}

	private static Period buildPeriod(JsonObject fbHours, int day, int index) {
		Period p= new Period();
		
		p.openingDay= Period.DAYS[day];
		p.openingTime= fbHours.getString(openKey(day, index));
		p.closingTime= fbHours.getString(closeKey(day, index));
		
		return p;
	}

}
