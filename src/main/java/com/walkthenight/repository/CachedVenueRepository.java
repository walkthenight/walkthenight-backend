package com.walkthenight.repository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.walkthenight.data.Event;
import com.walkthenight.data.EventRepository;
import com.walkthenight.data.Link;
import com.walkthenight.data.Series;
import com.walkthenight.data.SeriesRepository;
import com.walkthenight.data.Venue;
import com.walkthenight.data.VenueRepository;

public class CachedVenueRepository implements VenueRepository, SeriesRepository, EventRepository {

	private Cache cache;
	private final MashUpVenueRepository underlyingRepository;
    
    public CachedVenueRepository(MashUpVenueRepository underlyingRepository) {
    	this.underlyingRepository= underlyingRepository;
    	 try {
    	        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
                HashMap<Object, Object> properties = new HashMap<>();
                properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(1));
    	        cache = cacheFactory.createCache(properties);
    	    } catch (CacheException e) {
    	        throw new RuntimeException(e);
    	    }
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Venue> getVenues() {
		List<Venue> venues;
		
		if (cache.containsKey("venues")) {
			venues=  (List<Venue>)cache.get("venues");
		} else {
			venues= underlyingRepository.getVenues();
			cache.put("venues", venues);
		}
		
		return venues;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Venue getVenue(String id) {
		Venue venue;
		
		if (cache.containsKey(id)) {
			venue=  (Venue)cache.get(id);
		} else {
			venue= underlyingRepository.getVenue(id);
			cache.put(id, venue);
		}
		
		return venue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEvents(String id, String timeframe) {
		if (null == timeframe) 
			timeframe="all";
		
		List<Event> events;
		String key= id+"-"+timeframe+"-events";
		
		if (cache.containsKey(key)) {
			events=  (List<Event>)cache.get(key);
		} else {
			events= underlyingRepository.getEvents(id, timeframe);
			cache.put(key, events);
		}
		
		return events;
	}

	@Override
	public InputStream getPicture(String venueId) {
		return underlyingRepository.getPicture(venueId); //:TODO how to cache pic
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Link> getLinks(String venueId) {
		List<Link> venueLinks;
		String key= venueId+"-links";
		
		if (cache.containsKey(key)) {
			venueLinks=  (List<Link>)cache.get(key);
		} else {
			venueLinks= underlyingRepository.getLinks(venueId);
			cache.put(key, venueLinks);
		}
		
		return venueLinks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPhotos(String venueId) {
		List<String> venuePhotos;
		String key= venueId+"-photos";
		
		if (cache.containsKey(key)) {
			venuePhotos=  (List<String>)cache.get(key);
		} else {
			venuePhotos= underlyingRepository.getPhotos(venueId);
			cache.put(key, venuePhotos);
		}
		
		return venuePhotos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Series getSeries(String id) {
		Series series;
		
		if (cache.containsKey(id)) {
			series=  (Series)cache.get(id);
		} else {
			series= underlyingRepository.getSeries(id);
			cache.put(id, series);
		}
		
		return series;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSeriesPhotos(String seriesId) {
		List<String> seriesPhotos;
		String key= seriesId+"-seriesPhotos";
		
		if (cache.containsKey(key)) {
			seriesPhotos=  (List<String>)cache.get(key);
		} else {
			seriesPhotos= underlyingRepository.getSeriesPhotos(seriesId);
			cache.put(key, seriesPhotos);
		}
		
		return seriesPhotos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Event getEvent(String id) {
		Event event;
		
		if (cache.containsKey(id)) {
			event=  (Event)cache.get(id);
		} else {
			event= underlyingRepository.getEvent(id);
			cache.put(id, event);
		}
		
		return event;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getEventSeriesLinks(String eventId) {
		List<String> eventSeriesLinks;
		String key= eventId+"-seriesLinks";
		
		if (cache.containsKey(key)) {
			eventSeriesLinks= (List<String>)cache.get(key);
		} else {
			eventSeriesLinks= underlyingRepository.getEventSeriesLinks(eventId);
			cache.put(key, eventSeriesLinks);
		}
		return eventSeriesLinks;
	}

}
