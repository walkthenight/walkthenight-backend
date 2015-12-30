package com.walkthenight.wordpress;

import static com.walkthenight.wordpress.WordPressWebRequestor.executePost;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

public class WordPressPostGateway {
	
	public static class Status {
		public static final String PUBLISH="publish";
		public static final String DRAFT="draft";
	}
	
	public static HttpResponse createPost(final String postType, 
			final String title, 
			final String postContent, 
			final String parent,
			final String status)
			throws IOException {
		HttpResponse response;
		
		GenericData data = new GenericData();
		data.put("title", title);
		data.put("content", postContent);
		data.put("status", status);
		if (null != parent) 
			data.put("parent", parent);
		JsonHttpContent content = new JsonHttpContent(new JacksonFactory(), data);
		
		
		response= executePost(postType, content);
		
		if (response.getStatusCode() != 201) {
			throw new RuntimeException("Status was: " + response.getStatusCode() + ":" +response.parseAsString());
		}
		return response;
	}
	
	public static HttpResponse updatePost(final String postType, 
			final String id,
			final String postContent, 
			final String status)
			throws IOException {
		HttpResponse response;
		
		GenericData data = new GenericData();
		if (null != postContent)
			data.put("content", postContent);
		
		data.put("status", status);
		JsonHttpContent content = new JsonHttpContent(new JacksonFactory(), data);
		
		
		response= executePost(postType+"/"+id, content);
		
		if (response.getStatusCode() != 201) {
			throw new RuntimeException("Status was: " + response.getStatusCode() + ":" +response.parseAsString());
		}
		return response;
	}

}
