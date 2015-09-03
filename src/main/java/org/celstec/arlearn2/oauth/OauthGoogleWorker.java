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
import org.celstec.arlearn2.tasks.beans.migrate.MigrateGamesTask;
import org.celstec.arlearn2.tasks.beans.migrate.MigrateRunsTask;
import org.celstec.arlearn2.tasks.beans.migrate.MigrateUserTask;
import org.codehaus.jettison.json.JSONObject;

public class OauthGoogleWorker extends OauthWorker {

	private static String client_secret; 
	private static String client_id; 
	private static String redirect_uri;

	static {
		OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.GOOGLECLIENT);
		client_id = jdo.getClient_id();
		redirect_uri = jdo.getRedirect_uri();
		client_secret = jdo.getClient_secret();
	}

	@Override
	protected String getAuthUrl(String authCode) {
		return "https://accounts.google.com/o/oauth2/token?code=" + authCode + "&redirect_uri=" + redirect_uri + "&client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=authorization_code";
	}

	public void exchangeCodeForAccessToken() {
		RequestAccessToken request = new RequestAccessToken();
		request.postUrl(getAuthUrl(code), "code=" + code + "&" + "client_id=" + client_id + "&" + "client_secret=" + client_secret + "&" + "redirect_uri=" + redirect_uri + "&" + "grant_type=authorization_code");

		if (request.getAccessToken() !=  null) {
			saveAccount(request.getAccessToken());
			sendRedirect(request.getAccessToken(), ""+request.getExpires_in(), AccountJDO.GOOGLECLIENT);
		} else {
			error("The google authentication servers are currently not functional. Please retry later. <br> The service usually works again after 15:00 CEST. Find more (technical) information about this problem on. <ul> " +
					"<li ><a href=\"https://code.google.com/p/google-glass-api/issues/detail?id=99\">oauth2 java.net.SocketTimeoutException on AppEngine</a>" +
					"<li ><a href=\"https://groups.google.com/forum/?fromgroups#!topic/google-appengine-downtime-notify/TqKVL9TNq2A\">Google groups downtime</a></ul> ");
		}
	}

	public void saveAccount(String accessToken) {
		try {
			JSONObject profileJson = new JSONObject(readURL(new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken)));
			String id = "";
			String picture = "";
			String email = "";
			String given_name = "";
			String family_name = "";
			String name = "";
			if (profileJson.has("picture")) picture = profileJson.getString("picture");
			if (profileJson.has("id")) id = profileJson.getString("id");
			if (profileJson.has("email")) email =  profileJson.getString("email");
			if (profileJson.has("given_name")) given_name = profileJson.getString("given_name");
			if (profileJson.has("family_name")) family_name = profileJson.getString("family_name");
			if (profileJson.has("name")) name = profileJson.getString("name");
			AccountJDO account = AccountManager.addAccount(id, AccountJDO.GOOGLECLIENT, email, given_name, family_name,name, picture, false);
			 saveAccessToken(account.getUniqueId(), accessToken);
			 
			 (new MigrateGamesTask(profileJson.getString("email"), AccountJDO.GOOGLECLIENT, profileJson.getString("id"),accessToken)).scheduleTask();
			 (new MigrateRunsTask(profileJson.getString("email"), AccountJDO.GOOGLECLIENT, profileJson.getString("id"),accessToken)).scheduleTask();
			 
			 (new MigrateUserTask(profileJson.getString("email"), AccountJDO.GOOGLECLIENT, profileJson.getString("id"),accessToken)).scheduleTask();
		} catch (Throwable ex) {
			throw new RuntimeException("failed login", ex);
		}
	}

}
