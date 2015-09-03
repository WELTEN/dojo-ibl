package org.celstec.arlearn2.oauth;

import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;
import org.celstec.arlearn2.tasks.beans.migrate.MigrateGamesTask;
import org.celstec.arlearn2.tasks.beans.migrate.MigrateRunsTask;
import org.celstec.arlearn2.tasks.beans.migrate.MigrateUserTask;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class OauthWespotWorker extends OauthWorker {

    private static  String client_id;
    private static  String redirect_uri;
    private static  String client_secret;
    private static final Logger log = Logger.getLogger(OauthWespotWorker.class.getName());

    static {
        OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.WESPOTCLIENT);
        client_id = jdo.getClient_id();
        redirect_uri = jdo.getRedirect_uri();
        client_secret = jdo.getClient_secret();
    }

    public void exchangeCodeForAccessToken() {
        String authUrl = getAuthUrl(code).replace("https:", "http:");

        RequestAccessToken request = new RequestAccessToken();
        request.getUrl(authUrl);

        if (request.getAccessToken() !=  null) {
            saveAccount(request.getAccessToken());
            sendRedirect(request.getAccessToken(), ""+request.getExpires_in(), AccountJDO.GOOGLECLIENT);
        } else {
            error("The google authentication servers are currently not functional. Please retry later. <br> The service usually works again after 15:00 CEST. Find more (technical) information about this problem on. <ul> " +
                    "<li ><a href=\"https://code.google.com/p/google-glass-api/issues/detail?id=99\">oauth2 java.net.SocketTimeoutException on AppEngine</a>" +
                    "<li ><a href=\"https://groups.google.com/forum/?fromgroups#!topic/google-appengine-downtime-notify/TqKVL9TNq2A\">Google groups downtime</a></ul> ");
        }
    }

    @Override
    protected String getAuthUrl(String authCode) {
        return "https://wespot-arlearn.appspot.com/oauth/token?client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&client_secret=" + client_secret + "&code=" + authCode;
    }

    public void saveAccount(String accessToken) {
        try {

            System.out.println("TODO query user details");
//            String readUrl = readURL(new URL("http://localhost:8080/oauth/resource_query?access_token="+accessToken));
//            System.out.println("result is "+readUrl);
            JSONObject profileJson = new JSONObject(readURL(new URL("https://wespot-arlearn.appspot.com/oauth/resource_query?access_token="+accessToken)));
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
            AccountJDO account = AccountManager.addAccount(id, AccountJDO.WESPOTCLIENT, email, given_name, family_name, name, picture, false);
            saveAccessToken(account.getUniqueId(), accessToken);

        } catch (Throwable ex) {
            throw new RuntimeException("failed login", ex);
        }
    }
}
