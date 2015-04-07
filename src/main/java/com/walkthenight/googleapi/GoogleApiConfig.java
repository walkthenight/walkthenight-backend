package com.walkthenight.googleapi;


public interface GoogleApiConfig {
	public String getConsumerKey();
	public String getConsumerSecret();
	public String getApiKey();
	public String getApplicationName();
	public String getApiEmail();
	public String getClientId();
	public String getSpreadsheetFileId(String name);
	public String getPrivateKeyFilePath();
}
