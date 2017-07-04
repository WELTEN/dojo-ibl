package org.celstec.arlearn2.firebase;

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
import com.google.api.client.http.HttpTransport;
import com.google.common.io.CharStreams;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

public class FirebaseChannel {

    private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/view/firebase_config.jspf";
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

    private static FirebaseChannel instance;

    /**
     * FirebaseChannel is a singleton, since it's just utility functions.
     * The class derives auth information when first instantiated.
     */
    public static FirebaseChannel getInstance() {
        if (instance == null) {
            instance = new FirebaseChannel();
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
    private FirebaseChannel() {
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

}
