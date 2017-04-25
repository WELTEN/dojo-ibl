package org.celstec.arlearn2.oauth;

import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * ****************************************************************************
 * Copyright (C) 2017 Open Universiteit Nederland
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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */
public class OauthWespotMobileWorker extends OauthWespotWorker {

    private static  String client_id;
    private static  String redirect_uri;
    private static  String client_secret;
    private static final Logger log = Logger.getLogger(OauthWespotMobileWorker.class.getName());

    static {
        OauthConfigurationJDO jdo = OauthKeyManager.getConfigurationObject(AccountJDO.WESPOTCLIENT);
        client_id = jdo.getClient_id();
        redirect_uri = jdo.getRedirect_uri();
        client_secret = jdo.getClient_secret();
    }

    protected void sendRedirect(String accessToken, String expires, int type) {
        long expiresLong = 3600*24*7l;
        try {
            resp.sendRedirect("dojoiblmobile://"+baseUrl+"/main.html#/oauth/" + accessToken + "/" + type + "/" + expiresLong);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
