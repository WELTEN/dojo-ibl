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

import org.celstec.arlearn2.jdo.manager.ApplicationKeyManager;
import org.celstec.arlearn2.jdo.manager.OauthKeyManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.celstec.arlearn2.util.FirebaseUtils.getEmailOfToken;

//import javax.servlet.http.Cookie;


@Path("/oauth")
public class Oauth extends Service {

	private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/dojo-ibl-firebase-adminsdk-ofvly-bf28455fa0.json";

	@GET
	@Path("/addkey")
	public String addKey(
			@QueryParam("oauthProviderId") int oauthProviderId,  
			@QueryParam("client_id") String client_id,  
			@QueryParam("client_secret") String client_secret,  
			@QueryParam("redirect_uri") String redirect_uri
		) {
		OauthKeyManager.addKey(oauthProviderId, client_id, client_secret, redirect_uri);
		
		return "{}";
	}

	@GET
	@Path("/addOnBehalfOfKey")
	public String addKey(
			@QueryParam("applicationName") String appName
		) {
		ApplicationKeyManager.addKey(appName);
		return "{}";
	}

	@POST
	@Path("/authenticate")
	public boolean authenticate(@HeaderParam("Authorization") String token,
								@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
								@DefaultValue("application/json") @HeaderParam("Accept") String accept,
								String account) {

		String email = getEmailOfToken(token);

		if (email != null) {
			return true;
		}

		return false;
	}

//@POST
//	@Path("/authenticate")
//	public boolean authenticate(@HeaderParam("Authorization") String atoken,
//								@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
//								@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//								String account)  {
//
//
//
//		final AtomicBoolean authenticated = new AtomicBoolean(false);
//		final AtomicBoolean done = new AtomicBoolean(false);
//		final AtomicReference uidRef = new AtomicReference<>();
//
////		String atoken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjMxMjNlMWE3ZTY5MTEyNTI4NDQ2M2ZjOWJmNmEyNGM0YmVkOGQ5NTIifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZG9qby1pYmwiLCJuYW1lIjoiQW5nZWwgU3VhcmV6IEZlcm5hbmRleiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vLXFpaTNQejlTUndjL0FBQUFBQUFBQUFJL0FBQUFBQUFBTS1rL0x3clBHT29YSFVNL3Bob3RvLmpwZyIsImF1ZCI6ImRvam8taWJsIiwiYXV0aF90aW1lIjoxNTAyODA1MjI3LCJ1c2VyX2lkIjoiMnZiUTIzck9LelpsclZ2VHpVb3JrVDhxdGFIMiIsInN1YiI6IjJ2YlEyM3JPS3pabHJWdlR6VW9ya1Q4cXRhSDIiLCJpYXQiOjE1MDI4MDUyMjcsImV4cCI6MTUwMjgwODgyNywiZW1haWwiOiJ0aXRvZ2Vsb0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJnb29nbGUuY29tIjpbIjExNzc2OTg3MTcxMDQwNDk0MzU4MyJdLCJlbWFpbCI6WyJ0aXRvZ2Vsb0BnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJnb29nbGUuY29tIn19.yMMxIkWljBmHNe6wSuACqKGfw3kRNlvfP7Zr4lvg707AOO97GY9lSS35OKDvKqt1FBFc_9LGqx_mPR3Cwo7EbtvY0HRQmDDNuLuf_T4nC3aaZLS0FxnfQ-AehOOQ5QYdcwaVbW-ryBqJ4D2qgpWfFRCnsBqoWq8F8qwIqq5oe8kyCIKfi6gjcVJWy0yxH5asypeVKstdnx8uhJPHiy6qYaAspVnc7vlTNURZkyV-leyNpJpE1foQ0mOcBG00CbATJE9FVz8hfX7j6K27FiVOeW_RwjzoKxddbl8bC2y0QT-eHMKv7AAhTJqj-gzKTjjjqwAMhHQFzPVot2UWQVR6cw";
//
//
//		FileInputStream serviceAccount = null;
//		try {
//			serviceAccount = new FileInputStream(FIREBASE_SNIPPET_PATH);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		if (FirebaseApp.getApps().isEmpty()) {
//
//			FirebaseOptions options = null;
//			try {
//				options = new FirebaseOptions.Builder()
//                        .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
//                        .setDatabaseUrl("https://dojo-ibl.firebaseio.com")
//                        .build();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			FirebaseApp.initializeApp(options, "dojo-ibl");
//		}
//
//		FirebaseApp app = FirebaseApp.getInstance("dojo-ibl");
//
////		FirebaseDatabase db = FirebaseDatabase.getInstance(app);
////
////		DatabaseReference ref = FirebaseDatabase
////				.getInstance(app)
////				.getReference("accounts");
////		ref.addListenerForSingleValueEvent(new ValueEventListener() {
////
////			@Override
////			public void onDataChange(DataSnapshot dataSnapshot) {
////				Object document = dataSnapshot.getValue();
//////				Log.info("new value: " + document);
////
////				String todoText = "Don't forget to...\n\n";
////
////				Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
////
////				while(children.hasNext()){
////					DataSnapshot childSnapshot = (DataSnapshot) children.next();
////					todoText = todoText + " * " + childSnapshot.getValue().toString() + "\n";
////				}
////
////			}
////
////			@Override
////			public void onCancelled(DatabaseError error){
////				System.out.println("Error: "+error);
////			}
////		});
//
//		Task<FirebaseToken> task = FirebaseAuth.getInstance(app).verifyIdToken(atoken);
//
//
////		.addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
////			@Override
////			public void onSuccess(FirebaseToken decodedToken) {
////				uidRef.set(decodedToken.getUid());
////				authenticated.set(true);
////				done.set(true);
////				//FirebaseDatabase.getInstance().getReference("/messages");
////			}
////		})
////				.addOnFailureListener(new OnFailureListener() {
////					@Override
////					public void onFailure(@NonNull Exception e) {
////						uidRef.set("error");
////						authenticated.set(false);
////						done.set(true);
////					}
////				})
////				.addOnCompleteListener(new OnCompleteListener<FirebaseToken>() {
////					@Override
////					public void onComplete(@NonNull Task<FirebaseToken> task) {
////						uidRef.set("error");
////						authenticated.set(false);
////						done.set(true);
////					}
////				})
//
//		while (!task.isComplete()) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if (task.isSuccessful()) {
//			uidRef.set(task.getResult().getUid());
//			authenticated.set(true);
//			done.set(true);
//		}
//		else {
//			task.getException().printStackTrace();
//			authenticated.set(false);
//			done.set(true);
//		}
//
//		return authenticated.get();
//	}

	@GET
	@Path("/getOauthInfo")
	public String getOauthClient(@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		return serialise(OauthKeyManager.getClientInformation(), accept);
	}
	
	@GET
	@Path("/facebook")
	public String facebookCode(@QueryParam("code") String code,  
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response) {
		System.out.println("code: " + code);

		try {
			response.sendRedirect("ResultDisplay.html?gwt.codesvr=127.0.0.1:9997");
			if (true) return "redirect";
			System.out.println("getAuthURL: " + getAuthURL(code));

			String authURL = getAuthURL(code);

			URL url = new URL(authURL);

			System.out.println("URL: " + url);
			String result = readURL(url);
			String accessToken = null;
			Integer expires = null;
			System.out.println("Que devuelve: " + result);
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
				System.out.println("AccessToken: " + accessToken + " " + expires);
				authFacebookLogin(accessToken, expires);
				// UserService us = new UserService();
				// us.authFacebookLogin(accessToken, expires);
				response.sendRedirect("ResultDisplay.html?gwt.codesvr=127.0.0.1:9997");
//				response.sendRedirect("http://www.google.com/");
			} else {
				throw new RuntimeException("Access token and expires not found");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return "forward";
	}

	public String getAuthURL(String authCode) {

		return "https://graph.facebook.com/oauth/access_token?client_id=";// + client_id + "&redirect_uri=" + redirect_uri + "&client_secret=" + secret + "&code=" + authCode;

	}

	private String readURL(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}
	
	 public void authFacebookLogin(String accessToken, int expires) {
		 try {
		 System.out.println( readURL(new URL("https://graph.facebook.com/me?access_token=" + accessToken)));
	        

	        } catch (Throwable ex) {
	            throw new RuntimeException("failed login", ex);
	        }
	    }

}
