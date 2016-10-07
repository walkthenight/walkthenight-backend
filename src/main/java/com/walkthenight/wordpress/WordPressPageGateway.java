package com.walkthenight.wordpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.api.client.http.HttpResponse;
import com.walkthenight.data.Event;
import com.walkthenight.wordpress.WordPressPostGateway.Status;

public class WordPressPageGateway {
	private static final String EVENT_PARENT_PAGE_ID="818";
	private static final String ADD_TO_HEAD="<script type='text/javascript' src='/la/wp-content/themes/15zine-child/wtn-event.js'></script>";

	
	public String createEventPage(Event event, String ticketId) {
		try {
			HttpResponse response= WordPressPostGateway.createPost("pages", event.name, content(event, ticketId), EVENT_PARENT_PAGE_ID, Status.PUBLISH);

			final PageJson page = page(response);
			addPostMeta(page.id);
			return page.slug;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void addPostMeta(final String pageId) {
		Map<String, String> postMetaMap= new HashMap<>();
		postMetaMap.put("per-page-ath-content", ADD_TO_HEAD);
		
		postMetaMap.put("cb_featured_image_style","full-background");
		postMetaMap.put("cb_post_fis_header","on");
		postMetaMap.put("cb_full_width_post","nosidebar-fw");
		postMetaMap.put("cb_page_title","on");
		postMetaMap.put("cb_page_custom_sidebar","off");
		postMetaMap.put("cb_pb_onoff","off");
		postMetaMap.put("cb_pb_bs","a");
		
		postMetaMap.put("cb_page_custom_sidebar_type","cb_unique_sidebar");
		
		WordPressPostMetaGateway.writePostMeta(pageId, postMetaMap);
	}

	private PageJson page(HttpResponse response) throws IOException, JsonParseException, JsonMappingException {
		String json= response.parseAsString();
		ObjectMapper mapper = new ObjectMapper();
		PageJson pageJson = mapper.readValue(json, TypeFactory.defaultInstance().constructType(PageJson.class));
		return pageJson;
	}
	
	private String content(Event event, String ticketId) {
		StringBuilder sb= new StringBuilder();
		
		startFullWidthRow(sb);
		buildColumn1(sb, event, ticketId);
		buildColumn2(sb);
		endRow(sb);
		
		return sb.toString();
	}

	private void buildColumn2(StringBuilder sb) {
		startHalfWidthColumn(sb);
		startColumnText(sb);
		sb.append("[templatera id=\"819\"]");
		endColumnText(sb);
		endColumn(sb);
	}

	private void buildColumn1(StringBuilder sb, Event event, String ticketId) {
		startHalfWidthColumn(sb);
		startColumnText(sb);
		
		addTicketLink(ticketId, sb);
		
		endColumnText(sb);
		
		sb.append("[vc_column_text el_class=\"wtn-event-blurb\"]");
		endColumnText(sb);
		
		sb.append("[vc_raw_html]"+rawHtml(event)+"[/vc_raw_html]");
		
		endColumn(sb);
	}

	private void addTicketLink(String ticketId, StringBuilder sb) {
		sb.append("[ticket id=\""+ticketId+"\" type=\"buynow\" title=\"Buy Tickets\"]");
	}

	private void startFullWidthRow(StringBuilder sb) {
		sb.append("[vc_row full_width=\"\" parallax=\"\" parallax_image=\"\"]");
	}

	private void startHalfWidthColumn(StringBuilder sb) {
		sb.append("[vc_column width=\"1/2\"]");
	}

	private void startColumnText(StringBuilder sb) {
		sb.append("[vc_column_text]");
	}

	private void endColumnText(StringBuilder sb) {
		sb.append("[/vc_column_text]");
	}

	private void endRow(StringBuilder sb) {
		sb.append("[/vc_row]");
	}

	private void endColumn(StringBuilder sb) {
		sb.append("[/vc_column]");
	}

	private String rawHtml(Event event) {
		return Base64.encodeBase64String(html(event).getBytes());
	}
	
	private String html(Event event) {
		StringBuilder sb= new StringBuilder();
		sb.append("<img src='#' alt='' class='wtn-event-cover-image' />");
		sb.append("<var class='hidden event-id-data' data-eventid='"+event.id+"'></var>");
		return sb.toString();
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class PageJson {
		public String id;
		public String slug;
	}
	
	
}
