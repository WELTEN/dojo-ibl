package org.celstec.arlearn2.gwtcommonlib.client.auth;

import com.google.gwt.http.client.*;
import org.celstec.arlearn2.gwtcommonlib.client.LocalSettings;

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
public class OauthTwitter extends OauthClient{

    public String getLoginRedirectURL() {
        return "/oauth/twitter?twitter=init";
    }
    public static void autenticate() {

        String url = "https://api.twitter.com/oauth/request_token";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
//        builder.setHeader("Content-Type", "application/json");
        builder.setHeader("Authorization", "oauth_callback=\"http%3A%2F%2Fmyapp.com%3A3005%2Ftwitter%2Fprocess_callback\"");

        try {
            Request request = builder.sendRequest("", new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    System.out.println("response received");
                    if (200 == response.getStatusCode()) {

                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
}
