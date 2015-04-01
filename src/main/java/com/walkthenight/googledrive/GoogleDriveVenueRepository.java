package com.walkthenight.googledrive;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;

public class GoogleDriveVenueRepository implements VenueRepository {

	private static final String GDRIVE_VENUE_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/list/1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q/oobkxpm/private/full";
	private SpreadsheetService spreadsheetService;
	
	@Override
	public List<Venue> getVenues() {
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
			 venues.add(mapRowToVenue(row));

		return venues; 
	}

	private Venue mapRowToVenue(ListEntry row) {
		CustomElementCollection cec= row.getCustomElements();
		Venue v= new Venue();
		return v;
	}

}
