package com.walkthenight.googleapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;

import se.walkercrou.places.RequestHandler;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GoogleUrlFetchRequestHandler implements RequestHandler {

    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	
	public GoogleUrlFetchRequestHandler() {
        this(DEFAULT_CHARACTER_ENCODING);
    }
	
	public GoogleUrlFetchRequestHandler(String characterEncoding) {
		this.characterEncoding= characterEncoding;
	}
	
	private String characterEncoding;

	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	@Override
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	@Override
	public InputStream getInputStream(String uri) throws IOException {
		URL url = new URL(uri);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
		HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
		return new ByteArrayInputStream(response.getContent());
	}
	
    private String readString(HTTPResponse response) throws IOException {
        String str = new String(response.getContent(), characterEncoding);
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }

	@Override
	public String get(String uri) throws IOException {
		URL url = new URL(uri);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
		HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
		return readString(response);
	}

	@Override
	public String post(HttpPost data) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
