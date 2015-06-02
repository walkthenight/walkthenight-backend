package com.walkthenight.googleapi;

import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.walkthenight.data.Venue;

public class GoogleDriveVenueSpreadsheetGateway extends AbstractGoogleDriveSpreadsheetGateway {

	protected String worksheetKey() {
		return "oobkxpm";
	}
	
	public List<Venue> getVenues() {
		ListFeed listFeed = listFeed();
		  
		List<Venue> venues= new ArrayList<Venue>();
	  
		for (ListEntry row : listFeed.getEntries()) 
			mapRowToVenue(row, venues);
			 
		return venues; 
	}

	public Venue getVenue(String venueId) {
		ListFeed listFeed = listFeed();
		for (ListEntry row : listFeed.getEntries()) 
			if (venueId.equals(facebookId(row.getCustomElements())))
					return mapRowToVenue(row.getCustomElements(), venueId);
		
		return null;
	}
	
	private void mapRowToVenue(ListEntry row, List<Venue> venues) {
		CustomElementCollection cec= row.getCustomElements();
		
		// for now, if no Facebook id, no venue...
		
		String facebookId= facebookId(cec);
		if (null != facebookId && !"".equals(facebookId)) {
			venues.add(mapRowToVenue(cec, facebookId));
		} 
	}

	private String facebookId(CustomElementCollection cec) {
		return cec.getValue("fbid");
	}

	private Venue mapRowToVenue(CustomElementCollection cec, String facebookId) {
		String longitudeString= cec.getValue("longitude");
		String latitudeString= cec.getValue("latitude");
		
		Venue venue= new Venue();
		venue.id= facebookId;
		venue.name= cec.getValue("venuename");
		venue.googlePlaceId= cec.getValue("googleplaceid");
		venue.foursquareVenueId= cec.getValue("foursquarevenueid");
		venue.latitude= latitudeString == null ? 0 : Double.parseDouble(latitudeString);
		venue.longitude= longitudeString == null ? 0 : Double.parseDouble(latitudeString);
		venue.instagramPlaceId= cec.getValue("instagramplaceid");
		venue.facebookUrl= cec.getValue("facebookurl");
		venue.instagramHandle= cec.getValue("instagramhandle");
		venue.twitterHandle= cec.getValue("twitterhandle");
		
		return venue;
	}
}
