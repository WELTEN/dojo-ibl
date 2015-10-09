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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//import javax.servlet.http.Cookie;


@Path("/oauth")
public class Oauth extends Service {

	
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
	@Produces({ MediaType.MULTIPART_FORM_DATA })
	@Path("/authenticate")
	public Response authenticate(
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
								 @DefaultValue("application/json") @HeaderParam("Accept") String accept,
								 String account)  {


//if (true) {
	String output = "File saved to server location : test" ;
	return Response.status(200).entity(output).build();
//}


//		try {
//			JSONObject toWespot = new JSONObject();
//			toWespot.put("username", account.get("email").get(0));
//			toWespot.put("password", account.get("password").get(0));
//
//			URL url = new URL("http://wespot-arlearn.appspot.com/oauth/account/authenticate");
//
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
//			connection.setRequestMethod("POST");
//
//			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//			writer.write(toWespot.toString());
//			writer.close();
//
//			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				System.out.println(" ok");
//				 ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				InputStream is = connection.getInputStream();
//				int r;
//				while ((r = is.read()) != -1) {
//					baos.write(r);
//				}
//				String result = new String(baos.toByteArray());
//				JSONObject resultJson = new JSONObject(result);
//				System.out.println(resultJson);
//
////				response.addCookie(new Cookie("arlearn.AccessToken", resultJson.getString("token")));
////				response.addCookie(new Cookie("arlearn.OauthType", "5"));
//				new OauthWespotWorker().saveAccount(resultJson.getString("token"));
////				return result;
////				return Response.ok().entity(new ArrayList<>()).cookie(new NewCookie("arlearn.AccessToken", resultJson.getString("token"))).build();
//				String output = "File saved to server location : test" ;
//				return Response.status(200).entity(output).build();
//			} else {
//				System.out.println("not ok");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
	}

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
