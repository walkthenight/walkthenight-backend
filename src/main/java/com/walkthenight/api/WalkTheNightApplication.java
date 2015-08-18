package com.walkthenight.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import com.walkthenight.api.venue.EventRepositoryApi2;
import com.walkthenight.api.venue.SeriesRepositoryApi;
import com.walkthenight.api.venue.VenueRepositoryApi;

public class WalkTheNightApplication extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        
        s.add(VenueRepositoryApi.class);
        s.add(SeriesRepositoryApi.class);
        s.add(EventRepositoryApi2.class);
        s.add(JsonExceptionHandler.class);
        s.add(JacksonConfig.class);
        return s;
    }
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> s= new HashSet<Object>();
		s.add(corsFilter());
		return s;
	}

	private CorsFilter corsFilter() {
		CorsFilter filter = new CorsFilter();
		filter.getAllowedOrigins().add("http://localhost");
		filter.getAllowedOrigins().add("http://walkthenight.com");
		filter.getAllowedOrigins().add("https://walkthenight.com");
		filter.getAllowedOrigins().add("http://wtnall.wpengine.com");
		filter.getAllowedOrigins().add("http://wtnall.staging.wpengine.com");
		filter.getAllowedOrigins().add("http://walkthenight.wpengine.com");
		filter.getAllowedOrigins().add("http://walkthenight.staging.wpengine.com");
		return filter;
	}
	
}
