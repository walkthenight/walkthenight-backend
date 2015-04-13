package com.walkthenight.login;

import org.json.JSONObject;
import org.json.JSONException;

import com.walkthenight.facebook.FacebookConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacebookLoginServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {            
        String code = req.getParameter("code");
        if (code == null || code.equals("")) {
            // an error occurred, handle this
        }

        String token = null;
        try {
            String g = "https://graph.facebook.com/oauth/access_token?client_id="+FacebookConfig.FB_APP_ID
            			+"&redirect_uri=" +URLEncoder.encode("http://alpha.walkthenight.com/connect/facebook", "UTF-8") 
            			+ "&client_secret="+FacebookConfig.FB_APP_SECRET
            			+"&code=" + code;
            
            token = responseFrom(g);
            if (token.startsWith("{"))
                throw new Exception("error on requesting token: " + token + " with code: " + code);
        } catch (Exception e) {
                // an error occurred, handle this
        }

        String graph = null;
        try {
            graph = responseFrom("https://graph.facebook.com/me?" + token);
        } catch (Exception e) {
                // an error occurred, handle this
        }

        String facebookId;
        String firstName;
        String middleNames;
        String lastName;
        String email;
        String  gender;
        try {
            JSONObject json = new JSONObject(graph);
            facebookId = json.getString("id");
            firstName = json.getString("first_name");
            if (json.has("middle_name"))
               middleNames = json.getString("middle_name");
            else
                middleNames = null;
            if (middleNames != null && middleNames.equals(""))
                middleNames = null;
            lastName = json.getString("last_name");
            email = json.getString("email");
            if (json.has("gender")) {
                gender = json.getString("gender");
            } else {
                gender = null;
            }
        } catch (JSONException e) {
            // an error occurred, handle this
        }
    }

	private String responseFrom(String g) throws MalformedURLException, IOException {
		URLConnection c = new URL(g).openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		StringBuffer b = read(in);            
		in.close();
		return b.toString();
	}

	private StringBuffer read(BufferedReader in) throws IOException {
		String inputLine;
		StringBuffer b = new StringBuffer();
		while ((inputLine = in.readLine()) != null)
		    b.append(inputLine + "\n");
		return b;
	}
}