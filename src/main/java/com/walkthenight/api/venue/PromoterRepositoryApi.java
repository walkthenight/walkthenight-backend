package com.walkthenight.api.venue;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.walkthenight.data.Promoter;
import com.walkthenight.data.PromotorRepository;
import com.walkthenight.repository.TicketAdminRepository;

@Path("/promoters")
public class PromoterRepositoryApi {
	private PromotorRepository repository= new TicketAdminRepository();

	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/me")
	public Promoter getPromoter(@QueryParam("access_token") String accessToken) {
		return repository.getPromoter(accessToken);
	}
	
}
