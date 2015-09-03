package org.celstec.arlearn2.oauth;

import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
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
public class OauthTwitterWorker {

    private static final Logger log = Logger.getLogger(OauthTwitterWorker.class.getName());

    private static  String client_id;
    private static  String redirect_uri;
    private static  String client_secret;


    static {
        OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.TWITTERCLIENT);
        client_id = jdo.getClient_id();
        redirect_uri = jdo.getRedirect_uri();
        client_secret = jdo.getClient_secret();
    }


    public OauthTwitterWorker() {

    }

    public void redirectToTwitterForAuthentication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(client_id, client_secret);
        RequestToken token = null;
        try {
            token = twitter.getOAuthRequestToken(redirect_uri);
            req.getSession().setAttribute("requestToken", token);
            String loginUrl = token.getAuthenticationURL() + "&force_login=true";
            resp.sendRedirect(loginUrl);
        } catch (TwitterException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, e.getErrorMessage(), e);
        }
    }

    public String afterSuccesfullAuthentication(HttpServletRequest request) {
        Twitter twitter = new TwitterFactory().getInstance();
        RequestToken token = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        twitter.setOAuthConsumer(client_id, client_secret);
        try {
             AccessToken accessToken = twitter.getOAuthAccessToken(token, verifier);
            User user = twitter.verifyCredentials();
            AccountJDO account = AccountManager.addAccount(""+user.getId(), AccountJDO.TWITTERCLIENT, "",
                    "", "", user.getName(),
                    user.getProfileImageURL(),false);
            UserLoggedInManager.submitOauthUser(account.getUniqueId(), accessToken.getToken());
            return accessToken.getToken();
        } catch (TwitterException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

}
