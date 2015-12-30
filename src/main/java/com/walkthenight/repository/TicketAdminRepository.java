package com.walkthenight.repository;

import com.walkthenight.data.Promoter;
import com.walkthenight.data.PromotorRepository;
import com.walkthenight.googleapi.worksheets.PromoterWorksheet;

public class TicketAdminRepository implements PromotorRepository {
	private PromoterWorksheet promoterWorksheet= new PromoterWorksheet();
	
	@Override
	public Promoter getPromoter(String accessToken) {
		Promoter promoter= promoterWorksheet.getPromoter(accessToken);
		return promoter;
	}

}
