/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.oauth;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.FetchOptions;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public abstract class OauthWorker {

    private static final Logger log = Logger.getLogger(OauthWorker.class.getName());
	
	protected String code;
	private HttpServletResponse resp;
	protected String baseUrl;

	public void setCode(String code) {
		this.code = code;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public abstract void exchangeCodeForAccessToken();

	protected abstract String getAuthUrl(String authCode);

	protected void sendRedirect(String accessToken, String expires, int type) {
		long expiresLong = 3600*24*7l; 
		try {
			resp.sendRedirect(baseUrl+"/oauth.html?accessToken=" + accessToken + "&type=" + type + "&exp=" + expiresLong);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	protected void error(String error) {
		try {
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write("<h2>Error</h2><br>\n"+error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void saveAccessToken(String id, String authToken) {
		if (authToken != null) {
			UserLoggedInManager.submitOauthUser(id, authToken);
		}
	}

	protected String readURL(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}

	protected String postToURL(URL url, String data) throws IOException {
		try {
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			wr.close();
			rd.close();
			return result;
		} catch (Exception e) {
		}
		return "{}";
	}

	public void setResponse(HttpServletResponse resp) {
		this.resp = resp;

	}

	public class RequestAccessToken {

		private String accessToken;
		private long expires_in;
	    

		public void getUrl() {

		}

		public void getUrl(String url) {
			try {
				parseResult(readURL(new URL(url)));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		public void postUrl(String url, String data) {
			try {
				log.log(Level.INFO, "about to open url for code "+url + " *** "+data);
				URLConnection conn = new URL(url).openConnection();
//				conn.setConnectTimeout(30);
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String result = "";
				String line;
				while ((line = rd.readLine()) != null) {
					result += line;
				}
				log.log(Level.INFO, "oauth result"+result);
				wr.close();
				rd.close();
				parseResult(result);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		private void parseResult(String result) throws JSONException {
			log.log(Level.INFO, "parseResult " + result);
			JSONObject resultJson = new JSONObject(result);
			accessToken = resultJson.getString("access_token");
			expires_in = resultJson.getLong("expires_in");
		}

		public String getAccessToken() {
			return accessToken;
		}

		public long getExpires_in() {
			return expires_in;
		}

	}

}
