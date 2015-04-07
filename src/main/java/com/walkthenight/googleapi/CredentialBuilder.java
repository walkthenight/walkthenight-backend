package com.walkthenight.googleapi;


import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.common.collect.ImmutableList;

public class CredentialBuilder {
	
	private static final Collection<String> SERVICE_ACCOUNT_SCOPES= ImmutableList.of(
			"https://docs.google.com/feeds/",
				"https://docs.googleusercontent.com/",
				"https://spreadsheets.google.com/feeds/");
	
	public static GoogleCredential buildCredential(
			HttpTransport httpTransport,
			JsonFactory jsonFactory,
			GoogleApiConfig config, 
			String userEmail) throws GeneralSecurityException, IOException {
		
		  
		  GoogleCredential credential = new GoogleCredential.Builder()
	      .setTransport(httpTransport)
	      .setJsonFactory(jsonFactory)
	      .setServiceAccountId(config.getApiEmail())
	      .setServiceAccountScopes(SERVICE_ACCOUNT_SCOPES)
	      .setServiceAccountUser("si@fadedglamour.com")
	      .setServiceAccountPrivateKeyFromP12File(new File(config.getPrivateKeyFilePath()))
	      .build();
		  
		  return credential;
	}
}
