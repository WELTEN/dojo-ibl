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

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;
import org.codehaus.jettison.json.JSONObject;

public class OauthLinkedInWorker extends OauthWorker {

	private static String client_id;
	private static String redirect_uri;
	private static String client_secret;
    private static final Logger log = Logger.getLogger(OauthLinkedInWorker.class.getName());

    static {
		OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.LINKEDINCLIENT);
		client_id = jdo.getClient_id();
		redirect_uri = jdo.getRedirect_uri();
		client_secret = jdo.getClient_secret();
	}

	@Override
	public void exchangeCodeForAccessToken() {
		RequestAccessToken request = new RequestAccessToken();
		request.postUrl(getAuthUrl(code), "");
		saveAccount(request.getAccessToken());
		sendRedirect(request.getAccessToken(), "" + request.getExpires_in(), AccountJDO.LINKEDINCLIENT);

	}

	@Override
	protected String getAuthUrl(String authCode) {
		return "https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code&code=" + authCode + "&redirect_uri=" + redirect_uri + "&client_id=" + client_id + "&client_secret=" + client_secret;
	}

	public void saveAccount(String accessToken) {		JSONObject profileJson;
		try {
			profileJson = new JSONObject(readURL(new URL("https://api.linkedin.com/v1/people/~:(id,firstName,lastName,pictureUrl,emailAddress)?format=json&oauth2_access_token=" + accessToken)));
			AccountJDO account = AccountManager.addAccount(profileJson.getString("id"), AccountJDO.LINKEDINCLIENT, profileJson.getString("emailAddress"), profileJson.getString("firstName"), profileJson.getString("lastName"), profileJson.getString("firstName") + " " + profileJson.getString("lastName"),
					profileJson.getString("pictureUrl"), false);
    		 saveAccessToken(account.getUniqueId(), accessToken);
		} catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
