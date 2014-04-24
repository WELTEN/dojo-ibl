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
package org.celstec.arlearn2.gwtcommonlib.client.auth;

import java.util.Date;

import org.celstec.arlearn2.jdo.classes.AccountJDO;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

public class OauthClient {
	
	public final static int FBCLIENT = 1;
	public final static int GOOGLECLIENT = 2;
	public final static int LINKEDINCLIENT = 3;
    public final static int WESPOTCLIENT = 5;
	
	private String accessToken;
	private int type;
	
	private static OauthClient oauthInstance;
	
	public final static String COOKIE_TOKEN_NAME = "arlearn.AccessToken";
	public final static String COOKIE_OAUTH_TYPE = "arlearn.OauthType";
	
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public static OauthClient checkAuthentication() {
		if (oauthInstance != null) return oauthInstance;
		if (Window.Location.getParameter("accessToken") != null) {
			oauthInstance = readFromUrl();
		} else 	if (Cookies.getCookie(COOKIE_TOKEN_NAME) != null) {
			oauthInstance =  readFromCookie();
		}
		return oauthInstance;
	}
	
	public static void disAuthenticate() {
		oauthInstance = null;
		Cookies.removeCookie(COOKIE_TOKEN_NAME);
		Cookies.removeCookie(COOKIE_OAUTH_TYPE);
	}
	
	private static OauthClient readFromCookie() {
		String accessToken = Cookies.getCookie(COOKIE_TOKEN_NAME);
		String typeString = Cookies.getCookie(COOKIE_OAUTH_TYPE);
		
		if (typeString == null || accessToken == null) {
			Cookies.removeCookie(COOKIE_TOKEN_NAME);
			Cookies.removeCookie(COOKIE_OAUTH_TYPE);
			return null;
		}
		Integer type = Integer.parseInt(typeString);
		OauthClient client = null;
		switch (type) {
		case AccountJDO.FBCLIENT:
			client = new OauthFbClient();
			break;
		case AccountJDO.GOOGLECLIENT:
			client = new OauthGoogleClient();
			break;
		case AccountJDO.LINKEDINCLIENT:
			client = new OauthLinkedIn();
			break;

		default:
			break;
		}
		client.accessToken = accessToken;
		return client;
		
	}

	private static OauthClient readFromUrl() {
		String accessToken = null;
		int type = 0;
		int exp=0;
		if (Window.Location.getParameter("accessToken") != null) {
			accessToken = Window.Location.getParameter("accessToken");
		}
		if (Window.Location.getParameter("type") != null) {
			type = Integer.parseInt(Window.Location.getParameter("type"));
		}
		
		if (Window.Location.getParameter("exp") != null) {
			exp = Integer.parseInt(Window.Location.getParameter("exp"));
		}
		OauthClient client = null;
		switch (type) {
		case AccountJDO.FBCLIENT:
			client = new OauthFbClient();
			break;
		case AccountJDO.GOOGLECLIENT:
			client = new OauthGoogleClient();
			break;
		case AccountJDO.LINKEDINCLIENT:
			client = new OauthLinkedIn();
			break;
		default:
			break;
		}
		
		client.accessToken = accessToken;
		long expires = System.currentTimeMillis() + (exp*1000);
		Date d = new Date(expires);
		System.out.println("expires at"+d);
		
		Cookies.setCookie(COOKIE_TOKEN_NAME, accessToken, d);
		Cookies.setCookie(COOKIE_OAUTH_TYPE, type+"");
		return client;
	}

}
