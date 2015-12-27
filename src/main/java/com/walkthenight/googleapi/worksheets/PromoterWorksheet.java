package com.walkthenight.googleapi.worksheets;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.walkthenight.data.Promoter;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;

public class PromoterWorksheet {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
		
	public Promoter getPromoter(String emailAddress) {
		ListFeed listFeed = listFeed();
		  
		for (ListEntry row : listFeed.getEntries()) {
			Promoter promoter= mapRowToPromoter(row, emailAddress);
			if (null != promoter) {
				return promoter;
			}
		}

		return null; 
	}


	private ListFeed listFeed() {
		return spreadsheetGateway.listFeed("ourhf8y");
	}

	private Promoter mapRowToPromoter(ListEntry row, String emailAddress) {
		CustomElementCollection cec= row.getCustomElements();
		
		String promoterEmailOrNull= cec.getValue("promoteremail");
		if (optionalEquals(promoterEmailOrNull, emailAddress)) {
			Promoter promoter= new Promoter();
			
			promoter.email= emailAddress;
			promoter.pageId= cec.getValue("fbpageid");
		} 
		
		return null;
	}

	private static boolean optionalEquals(String optionalLeft, String right) {
		return null != optionalLeft && !"".equals(optionalLeft) && right.equals(optionalLeft);
	}
}
