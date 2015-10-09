package com.walkthenight.googleapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GoogleDriveSpreadsheetGateway {
	
	private static final String GDRIVE_SPREADSHEET_URL= "https://spreadsheets.google.com/feeds/list/";
	private static final String VENUE_SPREADSHEET_KEY = "1AtrLQVPlnmTC21e-LEUmUgNbIbsT1zkE2lq2_k4do3Q";
	
	private SpreadsheetService spreadsheetService;
	
	/**
	 * Run to list spreadsheets
	 */
	public static void main(String[] args) throws Exception {
		SpreadsheetService service= SpreadsheetIntegration.setUpSpreadsheetService();
		// Make a request to the API and get all spreadsheets.
	    SpreadsheetFeed feed = service.getFeed(new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full"), SpreadsheetFeed.class);
	    List<SpreadsheetEntry> spreadsheets = feed.getEntries();

	    // Iterate through all of the spreadsheets returned
	    for (SpreadsheetEntry spreadsheet : spreadsheets) {
	      // Print the title of this spreadsheet to the screen
	      System.out.println(spreadsheet.getId() + ":" + spreadsheet.getTitle().getPlainText());
	      
	      List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

	      // Iterate through each worksheet in the spreadsheet.
	      for (WorksheetEntry worksheet : worksheets) {
	        // Get the worksheet's title, row count, and column count.
	        String id= worksheet.getId();
	        String title = worksheet.getTitle().getPlainText();
	        int rowCount = worksheet.getRowCount();
	        int colCount = worksheet.getColCount();

	        // Print the fetched information to the screen for this worksheet.
	        System.out.println("\t" + id + " - " + title + "- rows:" + rowCount + " cols: " + colCount);
	      }
	    }
	}
	
	public ListFeed listFeed(final String worksheetKey) {
		setUpSpreadsheetService();
		
		try {
			return spreadsheetService.getFeed(feedUrl(VENUE_SPREADSHEET_KEY, worksheetKey), ListFeed.class);
		} catch (IOException | ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	private void setUpSpreadsheetService() {
		if (null == spreadsheetService) {
			try {
				spreadsheetService= SpreadsheetIntegration.setUpSpreadsheetService();
			} catch (GeneralSecurityException | IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static final URL feedUrl(final String spreadsheetKey, final String worksheetKey) throws MalformedURLException {
		return new URL(GDRIVE_SPREADSHEET_URL+spreadsheetKey+"/"+worksheetKey+"/private/full");
	}

	public ListEntry insert(String worksheetKey, ListEntry row) {
		setUpSpreadsheetService();
		
		try {
			return spreadsheetService.insert(feedUrl(VENUE_SPREADSHEET_KEY, worksheetKey), row);
		} catch (IOException | ServiceException e) {
			throw new RuntimeException(e);
		}
	}

}
