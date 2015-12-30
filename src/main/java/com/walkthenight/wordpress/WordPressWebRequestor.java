package com.walkthenight.wordpress;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

public class WordPressWebRequestor {
	private static final String WP_JSON_V2 = "/wp-json/wp/v2/";
	private static final String BASE_URL = "http://walkthenight.com/la";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public static class WordPressUrl extends GenericUrl {

		public WordPressUrl(String encodedUrl) {
			super(BASE_URL+WP_JSON_V2+encodedUrl);
		}

		@Key
		public String fields;
	}

	public static HttpResponse executeGet(String url) throws IOException {
		HttpRequest request = requestFactory().buildGetRequest(new WordPressUrl(url));
		setBasicAuth(request);

		HttpResponse response = request.execute();
		return response;
	}

	private static void setBasicAuth(HttpRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuthentication(WordPressApiConfig.API_USER, WordPressApiConfig.API_PASSWORD);
		request.setHeaders(headers);
	}
	
	public static HttpResponse executePost(String url, HttpContent content) throws IOException {
		HttpRequest request= requestFactory().buildPostRequest(new WordPressUrl(url), content);
		setBasicAuth(request);
		HttpResponse response= request.execute();
		return response;
	}

	private static HttpRequestFactory requestFactory() {
		return HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
	}
}
