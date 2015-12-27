package com.walkthenight.googleapi.worksheets;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;

public class EventWorksheet {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
		
	public String getWtnManagedEventUrlName(String eventId) {
		ListFeed listFeed = listFeed();
		  
		for (ListEntry row : listFeed.getEntries()) {
			String url= mapRowToUrl(row, eventId);
			if (null != url) {
				return url;
			}
		}

		return null; 
	}

	private ListFeed listFeed() {
		return spreadsheetGateway.listFeed("oc0u3ey");
	}

	private String mapRowToUrl(ListEntry row, String eventId) {
		CustomElementCollection cec= row.getCustomElements();
		
		String sheetEventIdOrNull= cec.getValue("fbeventid");
		if (null != sheetEventIdOrNull && !"".equals(sheetEventIdOrNull) && eventId.equals(sheetEventIdOrNull)) {
			String url= cec.getValue("eventurlname");
			if (null != url && !"".equals(url)) {
				return url;
			}
		} 
		
		return null;
	}
}
