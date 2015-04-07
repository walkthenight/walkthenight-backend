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
import com.walkthenight.data.*;
import com.walkthenight.facebook.FacebookVenueGateway;

public class GoogleDriveVenueRepository implements VenueRepository {

	private static final String GDRIVE_VENUE_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/list/1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q/oobkxpm/private/full";
	private SpreadsheetService spreadsheetService= null;
	
	private FacebookVenueGateway venueGateway= new FacebookVenueGateway();
	
	@Override
	public List<Venue> getVenues() {
		
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
		  
		List<Venue> venues= new ArrayList<Venue>();
	  
		for (ListEntry row : listFeed.getEntries()) 
			mapRowToVenue(row, venues);
			 
		return venues; 
	}

	private void mapRowToVenue(ListEntry row, List<Venue> venues) {
		CustomElementCollection cec= row.getCustomElements();
		
		// for now, if no Facebook id, no venue...
		
		String facebookId= cec.getValue("fbid");
		if (null != facebookId && !"".equals(facebookId)) {
			String venueName= cec.getValue("venuename");
			venues.add(new Venue.Builder(facebookId, venueName).build());
		} 
	}

	@Override
	public Venue getVenue(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Event> getEvents(String venueId) {
		return venueGateway.getEvents(venueId);
	}

}
