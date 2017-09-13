package org.celstec.arlearn2.delegators;

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
 * Date: 04/07/17
 * ****************************************************************************
 */

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FirebaseChannelDelegator extends GoogleDelegator{

    private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/dojo-ibl-firebase-adminsdk-ofvly-57bc30f6da.json";
    static InputStream firebaseConfigStream = null;
    private static final Collection FIREBASE_SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/firebase.database",
            "https://www.googleapis.com/auth/userinfo.email"
    );
    private static final String IDENTITY_ENDPOINT =
            "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit";

    private String firebaseDbUrl;
    private GoogleCredential credential;
    // Keep this a package-private member variable, so that it can be mocked for unit tests
    HttpTransport httpTransport;

    private static FirebaseChannelDelegator instance;

    private static final Logger log = Logger.getLogger(FirebaseChannelDelegator.class.getName());



    public FirebaseChannelDelegator(String authtoken) {
        super(authtoken);
    }

    public FirebaseChannelDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public FirebaseChannelDelegator(Service service) {
        super(service);
    }

    public FirebaseChannelDelegator(Account account, String token) {
        super(account, token);
    }

    /**
     * FirebaseChannel is a singleton, since it's just utility functions.
     * The class derives auth information when first instantiated.
     */
    public static FirebaseChannelDelegator getInstance() {
        if (instance == null) {
            instance = new FirebaseChannelDelegator();
        }
        return instance;
    }

    /**
     * Construct the singleton, with derived auth information. The Firebase database url is derived
     * from the snippet that we provide to the client code, to guarantee that the client and the
     * server are communicating with the same Firebase database. The auth credentials we'll use to
     * communicate with Firebase is derived from App Engine's default credentials, and given
     * Firebase's OAuth scopes.
     */
    public FirebaseChannelDelegator() {
        super();

        try {
            // This variables exist primarily so it can be stubbed out in unit tests.
            if (null == firebaseConfigStream) {
                firebaseConfigStream = new FileInputStream(FIREBASE_SNIPPET_PATH);
            }

            String firebaseSnippet = CharStreams.toString(new InputStreamReader(
                    firebaseConfigStream, StandardCharsets.UTF_8));
            firebaseDbUrl = parseFirebaseUrl(firebaseSnippet);

            credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
            httpTransport = UrlFetchTransport.getDefaultInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses out the Firebase database url from the client-side code snippet.
     * The code snippet is a piece of javascript that defines an object with the key 'databaseURL'. So
     * look for that key, then parse out its quote-surrounded value.
     */
    private static String parseFirebaseUrl(String firebaseSnippet) {
        int idx = firebaseSnippet.indexOf("databaseURL");
        if (-1 == idx) {
            throw new RuntimeException(
                    "Please copy your Firebase web snippet into " + FIREBASE_SNIPPET_PATH);
        }
        idx = firebaseSnippet.indexOf(':', idx);
        int openQuote = firebaseSnippet.indexOf('"', idx);
        int closeQuote = firebaseSnippet.indexOf('"', openQuote + 1);
        return firebaseSnippet.substring(openQuote + 1, closeQuote);
    }

    public void sendFirebaseMessage(String channelKey, JSONObject game)
            throws IOException {
        // Make requests auth'ed using Application Default Credentials
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
        GenericUrl url = new GenericUrl(
                String.format("%s/channels/%s.json", firebaseDbUrl, channelKey));
        HttpResponse response = null;

        try {
            if (null == game) {
                response = requestFactory.buildDeleteRequest(url).execute();
            } else {
                String gameJson = new Gson().toJson(game);
                response = requestFactory.buildPatchRequest(
                        url, new ByteArrayContent("application/json", gameJson.getBytes())).execute();
            }

            if (response.getStatusCode() != 200) {
                throw new RuntimeException(
                        "Error code while updating Firebase: " + response.getStatusCode());
            }

        } finally {
            if (null != response) {
                response.disconnect();
            }
        }
    }

    public void broadcast(Bean bean, String account) {
        broadcast(JsonBeanSerialiser.serialiseToJson(bean), account);
    }

    public void broadcast(String notification, String account) {
        try {
            JSONObject json = new JSONObject(notification);
            broadcast(json, account);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void broadcast(JSONObject notification, String account) {
        try {
            Iterator<String> it = notification.keys();
            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            while (it.hasNext()) {
                String key = it.next();
                if (!(notification.get(key) instanceof JSONObject))
                    valueMap.put(key, notification.get(key));

            }
            log.log(Level.WARNING, "about to send channel message to "+account);
            FirebaseChannelDelegator.getInstance().sendFirebaseMessage(account, notification);
            log.log(Level.WARNING, "channel message sent to ");
        } catch (Exception e) {

            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }

}
