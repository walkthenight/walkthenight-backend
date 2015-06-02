package com.walkthenight.googleapi;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

public abstract class AbstractGoogleDriveSpreadsheetGateway {
	
	private static final String GDRIVE_VENUE_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/list/1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q/";
	
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
			return spreadsheetService.getFeed(new URL(GDRIVE_VENUE_SPREADSHEET_URL+worksheetKey()+"/private/full"), ListFeed.class);
		} catch (IOException | ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	 protected abstract String worksheetKey();
}
