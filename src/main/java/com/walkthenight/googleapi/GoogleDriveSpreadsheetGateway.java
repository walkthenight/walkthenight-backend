package com.walkthenight.googleapi;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;
import com.walkthenight.data.Venue;

public class GoogleDriveSpreadsheetGateway {
	private static final String GDRIVE_VENUE_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/list/1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q/oobkxpm/private/full";
	private SpreadsheetService spreadsheetService= null;
	
	public List<Venue> getVenues() {
		ListFeed listFeed = listFeed();
		  
		List<Venue> venues= new ArrayList<Venue>();
	  
		for (ListEntry row : listFeed.getEntries()) 
			mapRowToVenue(row, venues);
			 
		return venues; 
	}

	private ListFeed listFeed() {
		if (null == spreadsheetService) {
			try {
				spreadsheetService= SpreadsheetIntegration.setUpSpreadsheetService();
			} catch (GeneralSecurityException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		 ListFeed listFeed;
		try {
			listFeed = spreadsheetService.getFeed(
					 new URL(GDRIVE_VENUE_SPREADSHEET_URL),
						ListFeed.class);
		} catch (IOException | ServiceException e) {
			throw new RuntimeException(e);
		}
		return listFeed;
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
		return new Venue.Builder(facebookId, cec.getValue("venuename")).googlePlaceId(cec.getValue("googleplaceid")).build();
	}

	
	
}
