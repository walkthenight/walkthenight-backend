package com.walkthenight.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import com.walkthenight.api.venue.SeriesRepositoryApi;
import com.walkthenight.api.venue.VenueRepositoryApi;

public class WalkTheNightApplication extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(VenueRepositoryApi.class);
        s.add(SeriesRepositoryApi.class);
        s.add(JsonExceptionHandler.class);
        s.add(JacksonConfig.class);
        return s;
    }
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> s= new HashSet<Object>();
		CorsFilter filter = new CorsFilter();
		filter.getAllowedOrigins().add("http://localhost");
		filter.getAllowedOrigins().add("http://walkthenight.com");
		filter.getAllowedOrigins().add("https://walkthenight.com");
		s.add(filter);
		return s;
	}
	
}
