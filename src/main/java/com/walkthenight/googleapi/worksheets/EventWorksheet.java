package com.walkthenight.googleapi.worksheets;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.walkthenight.data.Event;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;

public class EventWorksheet {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
		
	private static final String WORKSHEET_KEY="oc0u3ey";
	
	public void augmentEvent(Event event) {
		ListFeed listFeed = listFeed();
		  
		for (ListEntry row : listFeed.getEntries()) {
			boolean mapped= mapRowToEvent(row, event);
			if (mapped) {
				return;
			}
		}

		return; 
	}

	private ListFeed listFeed() {
		return spreadsheetGateway.listFeed(WORKSHEET_KEY);
	}

	private boolean mapRowToEvent(ListEntry row, Event event) {
		CustomElementCollection cec= row.getCustomElements();
		
		String sheetEventIdOrNull= cec.getValue("fbeventid");
		if (null != sheetEventIdOrNull && !"".equals(sheetEventIdOrNull) && event.id.equals(sheetEventIdOrNull)) {
			event.wtnManagedEventUrlName= cec.getValue("eventurlname");
			event.tickeraEventId= cec.getValue("tickeraeventid");
			event.tickeraPrimaryTicketId= cec.getValue("tickeraprimaryticketid");
			return true;
		} 
		
		return false;
	}
	
	public void addWtnManagedEvent(String fbEventId, String pageUrl, String tickeraEventId, String tickeraTicketId) {
		ListFeed feed= listFeed();
		
		ListEntry entry= feed.createEntry();
		
		CustomElementCollection cec= entry.getCustomElements();
		
		cec.setValueLocal("fbeventid", fbEventId);
		cec.setValueLocal("eventurlname", pageUrl);
		cec.setValueLocal("tickeraeventid", tickeraEventId);
		cec.setValueLocal("tickeraprimaryticketid", tickeraTicketId);
		
		spreadsheetGateway.insert(WORKSHEET_KEY, entry);
		
	}
}
