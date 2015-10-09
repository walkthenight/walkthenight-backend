package com.walkthenight.googleapi;

import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.walkthenight.data.Series;

public class SeriesWorksheet {

	private GoogleDriveSpreadsheetGateway spreadsheetGateway= new GoogleDriveSpreadsheetGateway();
		
	
	public List<Series> getSeriesList() {
		ListFeed listFeed = listFeed();
		  
		List<Series> seriesList= new ArrayList<Series>();
	  
		for (ListEntry row : listFeed.getEntries()) 
			mapRowToSeries(row, seriesList);
			 
		return seriesList; 
	}

	private ListFeed listFeed() {
		return spreadsheetGateway.listFeed("oy51l90");
	}

	private void mapRowToSeries(ListEntry row, List<Series> seriesList) {
		CustomElementCollection cec= row.getCustomElements();
		
		// for now, if no Facebook id, no venue...
		
		String facebookId= facebookId(cec);
		if (null != facebookId && !"".equals(facebookId)) {
			seriesList.add(mapRowToSeries(cec, facebookId));
		} 
	}
	
	private Series mapRowToSeries(CustomElementCollection cec, String facebookId) {
		Series series= new Series();
		
		series.id= facebookId;
		series.name= cec.getValue("name");
		series.facebookPage= cec.getValue("facebookpage");
		series.instagramHandle= cec.getValue("instagramhandle");
		series.twitterHandle= cec.getValue("twitterHandle");
		series.website= cec.getValue("website");
		
		return series;
	}

	private String facebookId(CustomElementCollection cec) {
		return cec.getValue("fbid");
	}

	public Series getSeries(String seriesId) {
		ListFeed listFeed = listFeed();
		for (ListEntry row : listFeed.getEntries()) 
			if (seriesId.equals(facebookId(row.getCustomElements())))
					return mapRowToSeries(row.getCustomElements(), seriesId);
		
		return null;
	}

}
