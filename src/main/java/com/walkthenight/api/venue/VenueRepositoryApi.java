package com.walkthenight.api.venue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.api.client.util.IOUtils;
import com.walkthenight.data.Event;
import com.walkthenight.data.Link;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueInfo;
import com.walkthenight.data.VenueRepository;
import com.walkthenight.repository.MashUpVenueRepository;

@Path("/venues")
public class VenueRepositoryApi {
	private VenueRepository repository= new MashUpVenueRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Venue> getVenues() {
		return repository.getVenues();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Venue getVenue(@PathParam("id") String id) {
		return repository.getVenue(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}/events")
	public List<Event> getEvents(@PathParam("id") String venueId) {
		return repository.getEvents(venueId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}/links")
	public List<Link> getLinks(@PathParam("id") String venueId) {
		return repository.getLinks(venueId);
	}
	
	@GET
	@Produces({"image/jpeg"})
	@Path("/{id}/picture")
	public Response getPicture(@PathParam("id") String venueId) {

	    final InputStream in = repository.getPicture(venueId);

	    if (null == in) {
	    	return Response.noContent().build();
	    } else {
		    return Response.ok().entity(new StreamingOutput(){
		        @Override
		        public void write(OutputStream output)
		           throws IOException, WebApplicationException {
		        	IOUtils.copy(in,output);
		        	in.close();
		           output.flush();
		        }
		    }).build();
	    }
	}
} 
