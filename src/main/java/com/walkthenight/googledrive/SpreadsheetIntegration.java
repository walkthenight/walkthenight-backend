package com.walkthenight.googledrive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;

public class SpreadsheetIntegration {

	public final static SpreadsheetService setUpSpreadsheetService() throws GeneralSecurityException, IOException {
	
		    SpreadsheetService service =
		        new SpreadsheetService("WTN-spreasheet-integration-v1");
		        
		    service.setOAuth2Credentials(credential());
		    
		    return service;
	  }
	
	  private static GoogleCredential credential() throws GeneralSecurityException,
			IOException {
		
		final HttpTransport httpTransport = new NetHttpTransport();
		final JsonFactory jsonFactory =  new JacksonFactory();;
		final GoogleApiConfig config= new WalkTheNightGoogleConfig();
		final String userEmail= "hello@possiblestorms.com";
	
		final GoogleCredential credential= CredentialBuilder.buildCredential(httpTransport, jsonFactory, config, userEmail);
		
		credential.refreshToken();
		
		return credential;
	 }


}
