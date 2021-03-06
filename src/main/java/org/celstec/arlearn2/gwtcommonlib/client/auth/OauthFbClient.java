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

public class OauthFbClient extends OauthClient {

	private static  String client_id;  
	private static  String redirect_uri; 
	   
	public static void init(String clientId, String redirectUri) {
		client_id = clientId;
		redirect_uri = redirectUri;
	}
	public String getLoginRedirectURL() {

		return "https://graph.facebook.com/oauth/authorize?client_id=" + client_id + "&display=page&redirect_uri=" + redirect_uri + "&scope=publish_stream,email";

	}
}
