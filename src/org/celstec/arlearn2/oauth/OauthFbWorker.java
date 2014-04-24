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

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;
import org.codehaus.jettison.json.JSONObject;

public class OauthFbWorker extends OauthWorker {

	private static  String client_id; 
	private static  String redirect_uri;
	private static  String client_secret;
    private static final Logger log = Logger.getLogger(OauthFbWorker.class.getName());


    static {
		OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.FBCLIENT);
		client_id = jdo.getClient_id();
		redirect_uri = jdo.getRedirect_uri();
		client_secret = jdo.getClient_secret();
	}
	public String getAuthUrl(String authCode) {
		return "https://graph.facebook.com/oauth/access_token?client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&client_secret=" + client_secret + "&code=" + authCode;
	}

	@Override
	public void exchangeCodeForAccessToken()  {
		String authURL = getAuthUrl(code);
		try {
            log.log(Level.SEVERE, "url "+authURL);
			URL url = new URL(authURL);
			String result = readURL(url);
			String accessToken = null;
			Integer expires = null;
			String[] pairs = result.split("&");
			for (String pair : pairs) {
				String[] kv = pair.split("=");
				if (kv.length != 2) {
					throw new RuntimeException("Unexpected auth response");
				} else {
					if (kv[0].equals("access_token")) {
						accessToken = kv[1];
					}
					if (kv[0].equals("expires")) {
						expires = Integer.valueOf(kv[1]);
					}
				}
			}
			if (accessToken != null && expires != null) {
				saveAccount(accessToken);
				sendRedirect(accessToken, ""+expires, AccountJDO.FBCLIENT);
			} else {
				throw new RuntimeException("Access token and expires not found");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	 public void saveAccount(String accessToken) {
		 JSONObject profileJson;
			try {
				profileJson = new JSONObject(readURL(new URL("https://graph.facebook.com/me?access_token=" + accessToken)));
                String picture = "";
                if (profileJson.has("username")){
                    picture = "http://graph.facebook.com/"+profileJson.getString("username")+"/picture";
                }
				AccountJDO account = AccountManager.addAccount(
						 profileJson.getString("id"), 
						 AccountJDO.FBCLIENT, 
						 profileJson.getString("email"), 
						 profileJson.getString("first_name"), 
						 profileJson.getString("last_name"),
						 profileJson.getString("first_name")+" "+profileJson.getString("last_name"),
						 picture, false);
				 saveAccessToken(account.getUniqueId(), accessToken);
			} catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
			}
	    }
}
