package org.celstec.arlearn2.gwtcommonlib.client.network;


import org.celstec.arlearn2.gwtcommonlib.client.LocalSettings;
import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;

public class GenericClient {
	
	public static String urlPrefix = null;

	public String getUrl() {
		if (urlPrefix != null) return urlPrefix;
		return "rest/";
//		return "http://streetlearn.appspot.com/rest/";
	}

	public RequestBuilder getRequestBuilder(String urlPostfix) {
		return getRequestBuilder(urlPostfix, RequestBuilder.GET);
	}
	
	public RequestBuilder getRequestBuilderEvenIfNotAuthenticated(String urlPostfix) {
		return getRequestBuilderEvenIfNotAuthenticated(urlPostfix, RequestBuilder.GET);
	}

	public RequestBuilder getRequestBuilder(String urlPostfix, RequestBuilder.Method m) {
		String url = urlPostfix == null ? getUrl() : getUrl() + urlPostfix;
		RequestBuilder builder = new RequestBuilder(m, url);
		if (OauthClient.checkAuthentication() == null) return null;
		String authorization = "GoogleLogin auth=" + OauthClient.checkAuthentication().getAccessToken();
		if (LocalSettings.getInstance().getOnBehalfOf() != null) {
			authorization = LocalSettings.getInstance().getOnBehalfOf();
		}
		builder.setHeader("Authorization", authorization);
		builder.setHeader("Accept", "application/json");
		return builder;
	}
	
	public RequestBuilder getRequestBuilderEvenIfNotAuthenticated(String urlPostfix, RequestBuilder.Method m) {
		String url = urlPostfix == null ? getUrl() : getUrl() + urlPostfix;
		RequestBuilder builder = new RequestBuilder(m, url);
		String authorization = "GoogleLogin auth=" + Authentication.getInstance().getAuthenticationToken();
		if (LocalSettings.getInstance().getOnBehalfOf() != null) {
			authorization = LocalSettings.getInstance().getOnBehalfOf();
		}
		builder.setHeader("Authorization", authorization);
		builder.setHeader("Accept", "application/json");
		return builder;
	}

	protected void invokeJsonPOST(String urlPostfix, JSONObject object, final JsonCallback jcb) {
		invokeJsonPOST(urlPostfix, object == null ? "" : object.toString(), jcb);
	}

	protected void invokeJsonPUT(String urlPostfix, JSONObject object, final JsonCallback jcb) {
		invokeJsonPUT(urlPostfix, object == null ? "" : object.toString(), jcb);
	}

	protected void invokeJsonPOST(String urlPostfix, String object, final JsonCallback jcb) {
		invokeJsonPOST(urlPostfix, object, null, jcb);
	}

	protected void invokeJsonPOST(String urlPostfix, String object, String onBehalfOf, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.POST);
		builder.setHeader("Content-Type", "application/json");
		if (onBehalfOf != null) builder.setHeader("Authorization", onBehalfOf);

		try {
			Request request = builder.sendRequest(object, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jcb != null)
								jcb.onJsonReceived(jsonValue);
						} catch (JSONException e) {
							if (jcb != null)
								jcb.onError();

						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}
	
	protected void invokeJsonPUT(String urlPostfix, String object, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.PUT);
		builder.setHeader("Content-Type", "application/json");
		try {
			Request request = builder.sendRequest(object, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jcb != null)
								jcb.onJsonReceived(jsonValue);
						} catch (JSONException e) {
							if (jcb != null)
								jcb.onError();

						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	protected void invokeJsonDELETE(String urlPostfix, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.DELETE);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						if (jcb != null) {
							try {
								if (response.getText().equals("")) {
									jcb.onJsonReceived(new JSONObject());
									return;
								}
								JSONValue jsonValue = JSONParser.parseLenient(response.getText());
								if (jsonValue.isObject() != null) {
									jcb.onJsonReceived(jsonValue);
								}

							} catch (JSONException e) {
								jcb.onError();
							}
						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	protected void invokeJsonGETEvenIfNotAuthenticated(String urlPostfix, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilderEvenIfNotAuthenticated(urlPostfix);
		if (builder == null) return;
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
								jcb.onJsonReceived(jsonValue);

						} catch (JSONException e) {
							jcb.onError();
						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {
					exception.printStackTrace();
				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}
	
	protected void invokeJsonGET(String urlPostfix, final JsonCallback jcb) {
		invokeJsonGET(urlPostfix, null, jcb);
	}
	
	protected void invokeJsonGET(String urlPostfix, String onBehalfOf, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix);
		if (builder == null) return;
		if (onBehalfOf != null) builder.setHeader("Authorization", onBehalfOf);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jsonValue.isObject() != null) {
								jcb.onJsonReceived(jsonValue);
							}

						} catch (JSONException e) {
							jcb.onError();
						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {
					exception.printStackTrace();
				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	public void getItemsForRun(long runId, JsonCallback jsonCallback) {
		invokeJsonGET(getRunUrlPostfix(runId), jsonCallback);
	}

	public void getItemsForGame(long gameId, JsonCallback jsonCallback) {
		invokeJsonGET(getGameUrlPostfix(gameId), jsonCallback);
	}

	public void getItems(JsonCallback jsonCallback) {
		invokeJsonGET(null, jsonCallback);
	}
	
	public void deleteItemsForGame(long gameId, JsonCallback jsonCallback) {
		invokeJsonDELETE(getGameUrlPostfix(gameId), jsonCallback);
	}

	public void deleteItemsForRun(long runId, JsonCallback jsonCallback) {
		invokeJsonDELETE(getRunUrlPostfix(runId), jsonCallback);
	}

	protected String getRunUrlPostfix(long runId) {
		return "/runId/" + runId;
	}

	protected String getGameUrlPostfix(long gameId) {
		return "/gameId/" + gameId;
	}
}
