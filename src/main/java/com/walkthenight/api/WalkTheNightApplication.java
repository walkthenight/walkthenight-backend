package com.walkthenight.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.walkthenight.api.venue.VenueRepositoryApi;

public class WalkTheNightApplication extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(VenueRepositoryApi.class);
        return s;
    }
}
