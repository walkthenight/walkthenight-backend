package com.walkthenight.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonExceptionHandler implements ExceptionMapper<Exception> {     
	
	private static final Logger LOG= Logger.getLogger("WalkTheNightApplication");
	
	@Override     
	public Response toResponse(Exception e) {               
		
			LOG.log(Level.SEVERE, e.getMessage(), e);
		
			StringBuilder response = new StringBuilder("{");         
			response.append("\"error\":");         
			response.append("\"" + e.getMessage() + "\"");         
			response.append("}");         
			return Response.serverError().entity(response.toString()).type(MediaType.APPLICATION_JSON).build();     
			
		} 
}