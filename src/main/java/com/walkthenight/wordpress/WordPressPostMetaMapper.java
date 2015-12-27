package com.walkthenight.wordpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.restfb.WebRequestor;
import com.restfb.WebRequestor.Response;

public class WordPressPostMetaMapper {
	
	public static Map<String, String> ticketPostMeta(WebRequestor client, String baseUrl, String postId) {
		Map<String, String> postMetaMap= new HashMap<>();
		
		Response response= null;
		
		try 
		{
			 response = client.executeGet(baseUrl+"/wp-json/wp/v2/posts/"+postId+"/meta");
	         
	         if (response.getStatusCode() == 200) {
		         String json = response.getBody();
		         ObjectMapper mapper = new ObjectMapper();
		         PostMeta[] postMeta = mapper.readValue(json, TypeFactory.defaultInstance().constructArrayType(PostMeta.class));
		         
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
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class PostMeta {
		public String key;
		public String value;
	}
}
