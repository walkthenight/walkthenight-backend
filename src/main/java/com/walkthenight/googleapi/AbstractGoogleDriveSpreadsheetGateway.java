package com.walkthenight.googleapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

public abstract class AbstractGoogleDriveSpreadsheetGateway {
	
	private static final String GDRIVE_SPREADSHEET_URL= "https://spreadsheets.google.com/feeds/list/";
	private static final String VENUE_SPREADSHEET_KEY = "1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q";
	
	private SpreadsheetService spreadsheetService;
	
	protected ListFeed listFeed() {
		if (null == spreadsheetService) {
			try {
				spreadsheetService= SpreadsheetIntegration.setUpSpreadsheetService();
			} catch (GeneralSecurityException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			return spreadsheetService.getFeed(feedUrl(VENUE_SPREADSHEET_KEY, worksheetKey()), ListFeed.class);
		} catch (IOException | ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	private static final URL feedUrl(final String spreadsheetKey, final String worksheetKey) throws MalformedURLException {
		return new URL(GDRIVE_SPREADSHEET_URL+spreadsheetKey+"/"+worksheetKey+"/private/full");
	}

	 protected abstract String worksheetKey();
}
