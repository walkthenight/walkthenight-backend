package com.walkthenight.instagram;

import java.util.ArrayList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

public class InstagramGateway {
	
	private static final String INSTAGRAM_CLIENT_ID="0850af4fea414bb9a2e3fb7ad343d24e";

	private Instagram instagram= new Instagram(INSTAGRAM_CLIENT_ID);


	public List<String> getPhotosFromLocation(String instagramPlaceId) {
		try {
			return links(instagram.getRecentMediaByLocation(instagramPlaceId));
		} catch (InstagramException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getPhotosFromHandle(String instagramHandle) {
		try {
			return links(instagram.getRecentMediaFeed(instagramHandle));
		} catch (InstagramException e) {
			throw new RuntimeException(e);
		}
	}
	
	private List<String> links(MediaFeed mediaFeed) {
		List<String> links= new ArrayList<String>();
		List<MediaFeedData> mediaFeeds = mediaFeed.getData();
		for (MediaFeedData mf : mediaFeeds)
			if (mf.getType().equals("image"))
				links.add(mf.getImages().getStandardResolution().getImageUrl());
		return links;
	}

}
