package com.walkthenight.googleapi.worksheets;

import static com.restfb.Parameter.with;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import com.walkthenight.data.Promoter;
import com.walkthenight.googleapi.GoogleDriveSpreadsheetGateway;

public class PromoterWorksheet {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
		
	public Promoter getPromoter(String accessToken) {
		FacebookClient fbClient= new DefaultFacebookClient(accessToken, Version.VERSION_2_4);

		User user = fbClient.fetchObject(
				"me", 
				User.class, 
				with("fields", "id,email"));
		
		
		ListFeed listFeed = listFeed();
		  
		for (ListEntry row : listFeed.getEntries()) {
			Promoter promoter= mapRowToPromoter(row, user.getEmail());
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
			promoter.pageName= cec.getValue("name");
			
			return promoter;
		} 
		
		return null;
	}

	private static boolean optionalEquals(String optionalLeft, String right) {
		return null != optionalLeft && !"".equals(optionalLeft) && right.equals(optionalLeft);
	}
}
