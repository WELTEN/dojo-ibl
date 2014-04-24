package org.celstec.arlearn2.gwtcommonlib.client.network;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.docs.Offline;
import com.smartgwt.client.util.SC;

public class LoginClient {

	public static String urlPrefix = "";
//	public static String urlPrefix = "http://streetlearn.appspot.com/";

	private String username;
	private String password;

	public static final String USERNAME_COOKIE = "org.celstec.arlearn.const.username";
	public static final String AUTH_COOKIE = "org.celstec.arlearn.const.auth";
	
	public LoginClient(String username, String password) {
		this.username = username;
		this.password = password;
		Cookies.setCookie(USERNAME_COOKIE, username);
		System.out.println(com.smartgwt.client.util.Offline.get(AUTH_COOKIE));
		System.out.println(com.smartgwt.client.util.Offline.get(USERNAME_COOKIE));
	}

	public void authenticate(final LoginCallback lc) {
		String url = urlPrefix+"rest/login";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("accept", "application/json");
		builder.setHeader("Content-Type", "text/plain");
		try {
			Request request = builder.sendRequest(username+"\n"+password,
					new RequestCallback() {

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							if (200 == response.getStatusCode()) {
								try {
									JSONValue jsonValue = JSONParser
											.parseLenient(response.getText());
									if (jsonValue.isObject() != null) {
										String token = jsonValue.isObject().get("auth").isString().stringValue();
										com.smartgwt.client.util.Offline.put(AUTH_COOKIE, token);
										Cookies.setCookie(AUTH_COOKIE, token);	
										lc.onAuthenticationTokenReceived(token);
									}

								} catch (JSONException e) {
									SC.say("jsonException: "+e);
								}
							} else {										
								com.smartgwt.client.util.Offline.remove(AUTH_COOKIE);
								com.smartgwt.client.util.Offline.remove(USERNAME_COOKIE);

								Cookies.removeCookie(AUTH_COOKIE);		
								Cookies.removeCookie(USERNAME_COOKIE);	
								lc.onError();
							}

						}

						@Override
						public void onError(Request request, Throwable exception) {
							// TODO Auto-generated method stub

						}
					});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
