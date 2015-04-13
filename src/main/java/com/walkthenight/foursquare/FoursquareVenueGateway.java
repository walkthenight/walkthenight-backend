package com.walkthenight.foursquare;

import java.util.ArrayList;
import java.util.List;

import com.walkthenight.data.Link;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.LinkGroup;

public class FoursquareVenueGateway {
	private static final String API_KEY="UPZPXZIJZH20JO2VDZVFWMOZER5DK1AAES1DN4U1XIANETJY";
	private static final String API_SECRET="LNZHNC4TNVMUORB4DUU4NE5LCNFDFKBAS2D0VE41M5XCONO0";
	
    private FoursquareApi foursquareApi = new FoursquareApi(API_KEY, API_SECRET, "");

	
	public List<Link> getLinks(String foursquareVenueId) {
		try {
			Result<LinkGroup> result= foursquareApi.venuesLinks(foursquareVenueId);
			
			if (result.getResult() != null) {
				return mapLinks(result);
			} else {
				return null;
			}
			
		} catch (FoursquareApiException fae) {
			throw new RuntimeException(fae);
		}
		
	}


	private List<Link> mapLinks(Result<LinkGroup> result) {
		LinkGroup linkGroup= result.getResult();
		List<Link> links= new ArrayList<Link>();
		for (fi.foyt.foursquare.api.entities.Link foursquareLink : linkGroup.getItems())
			links.add(makeLink(foursquareLink));
		
		return links;
	}


	private Link makeLink(fi.foyt.foursquare.api.entities.Link foursquareLink) {
		Link l= new Link();
		l.provider= foursquareLink.getProvider().getId();
		l.link= foursquareLink.getUrl();
		l.id= foursquareLink.getLinkedId();
		return l;
	}
}
