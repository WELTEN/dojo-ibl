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
package org.celstec.arlearn2.api;

import com.google.appengine.tools.mapreduce.MapSettings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.codehaus.jettison.json.JSONException;
import org.glassfish.jersey.server.Uri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class Service {

	public interface OntaskCompleted{
		void onSuccess(Uri returnurl);
		void onfail();
	}

	private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/dojo-ibl-firebase-adminsdk-ofvly-57bc30f6da.json";

	protected Account account;
	protected String token;

	protected UsersDelegator verifyCredentials(String authToken) {
		UsersDelegator qu = new UsersDelegator(authToken);
		if (qu.getCurrentUserAccount()== null) return null;
		return qu;
	}
	
	protected Object getInvalidCredentialsBean() {
		Bean error = new Bean();
		error.setError("credentials are invalid");
		error.setErrorCode(Bean.INVALID_CREDENTIALS);
		return error;
	}
	
	protected Object getBeanDoesNotParseException(String specificErrorMessage) {
		Bean error = new Bean();
		error.setError("Could not parse bean: "+specificErrorMessage);
		return error;
	}

	protected boolean validCredentials(String authToken) {

		final AtomicBoolean authenticated = new AtomicBoolean(false);
		final AtomicBoolean done = new AtomicBoolean(false);
		final AtomicReference uidRef = new AtomicReference<>();

		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream(FIREBASE_SNIPPET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
				.setDatabaseUrl("https://fir-oauth-fcm.firebaseio.com")
				.build();

		FirebaseApp.initializeApp(options, "fir-oauth-fcm");

		FirebaseApp app = FirebaseApp.getInstance("fir-oauth-fcm");

		FirebaseAuth.getInstance(app).verifyIdToken(authToken)
				.addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
					@Override
					public void onSuccess(FirebaseToken decodedToken) {
						uidRef.set(decodedToken.getUid());
						authenticated.set(true);
						done.set(true);
//						FirebaseDatabase.getInstance().getReference("/messages");
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						uidRef.set("error");
						authenticated.set(false);
						done.set(true);
					}
				});

		while (!done.get());
		return authenticated.get();
	}

    public boolean isAdministrator(String authToken) {
        return validCredentials(authToken) && account.isAdministrator();
    }
	
	protected String toJson(Object bean) {
		if (bean == null) return "";
		return bean.toString();
	}
	
	protected String serialise(Object bean, String accept) {
		if  ("application/json".equalsIgnoreCase(accept)) return toJson(bean);
		if  (accept != null && accept.contains("application/json")) return toJson(bean);
		if  ("*/*".equalsIgnoreCase(accept)) return toJson(bean);
		return toJson(bean);
//		return accept + " is not yet supported\n";
	}
	
	protected Object deserialise(String beanString, Class beanClass, String contentType) {
		if  (contentType == null || "application/json".equalsIgnoreCase(contentType) ||  "*/*".equalsIgnoreCase(contentType)) 
			return jsonDeserialise(beanString, beanClass);
		if (contentType != null && contentType.contains("application/json"))
			return jsonDeserialise(beanString, beanClass);
		return contentType + " is not yet supported\n";
	}
	
	protected Object jsonDeserialise(String beanString, Class beanClass) {
		JsonBeanDeserializer jbd;
		try {
			jbd = new JsonBeanDeserializer(beanString);
			return (Bean) jbd.deserialize(beanClass);
		} catch (JSONException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

//    protected MapReduceSettings getSettings() {
//        return new MapReduceSettings().setWorkerQueueName("default").setBucketName("streetlearn-mapreduce").setModule("default");
//    }

    protected MapSettings getSettings() {
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("default")
                .setModule("default")
                .build();
        return settings;
    }
    protected MapSettings getSettings2() {
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("mapreduce-workers")
                .setModule("mapreduce")
                .build();
        return settings;
    }
}
