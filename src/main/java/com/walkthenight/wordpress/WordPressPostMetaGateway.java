package com.walkthenight.wordpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

public class WordPressPostMetaGateway {
	
	public static Map<String, String> readPostMeta(String postId) {
		Map<String, String> postMetaMap= new HashMap<>();
		
		HttpResponse response= null;
		
		try 
		{
			 response = WordPressWebRequestor.executeGet("posts/"+postId+"/meta");
	         
	         if (response.getStatusCode() == 200) {
		         String json = response.parseAsString();
		         ObjectMapper mapper = new ObjectMapper();
		         PostMeta[] postMeta = mapper.readValue(json, 
		        		 TypeFactory.defaultInstance().constructArrayType(PostMeta.class));
		         
		         for (PostMeta p : postMeta) 
		        	 postMetaMap.put(p.key, p.value);
		         
	         } else {
	        	 throw new RuntimeException();
	         }
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return postMetaMap;
	}
	
	public static void writePostMeta(String postId, Map<String, String> map) {
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			GenericData data = new GenericData();
			data.put("key", entry.getKey());
			data.put("value", entry.getValue());
			
			JsonHttpContent content = new JsonHttpContent(new JacksonFactory(), data);
			
			HttpResponse response= null;
			
			try  
			{
			   response = WordPressWebRequestor.executePost("posts/"+postId+"/meta", content);
			   if (response.getStatusCode() != 201) {
				   throw new RuntimeException(
						   "Problem writing post meta data - " 
								   + entry.getKey() + ":"  + entry.getValue() 
								   + ": " + response.parseAsString());
			   }
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class PostMeta {
		public String key;
		public String value;
	}
}
