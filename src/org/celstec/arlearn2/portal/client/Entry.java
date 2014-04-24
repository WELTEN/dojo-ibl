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
package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.LocalSettings;
import org.celstec.arlearn2.gwtcommonlib.client.auth.*;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.OauthNetworkClient;
import org.celstec.arlearn2.portal.client.author.AuthorPage;
import org.celstec.arlearn2.portal.client.debug.DebugPage;
import org.celstec.arlearn2.portal.client.game.GamePage;
import org.celstec.arlearn2.portal.client.htmlDisplay.CrsDisplay;
import org.celstec.arlearn2.portal.client.htmlDisplay.HtmlDisplayPage;
import org.celstec.arlearn2.portal.client.network.NetworkPage;
import org.celstec.arlearn2.portal.client.resultDisplay.ResultDisplayPage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;

import java.util.List;
import java.util.Map;

public class Entry implements EntryPoint {

	private VLayout vLayout;

	public Anchor anchorFacebook = new Anchor("Access with Facebook");
	public Anchor anchorGoogle = new Anchor("Access with Google");
	public Anchor anchorLinkedIn = new Anchor("Access with LinkedIn");

	@Override
	public void onModuleLoad() {
		if (Window.Location.getParameter("onBehalfOf")!= null) {
			LocalSettings.getInstance().setOnBehalfOf(Window.Location.getParameter("onBehalfOf"));
		} else {
			LocalSettings.getInstance().setOnBehalfOf(null);
		}
		String localeExtension = LocalSettings.getInstance().getLocateExtension();

		if (!"".equals(localeExtension) && Window.Location.getParameter("locale") == null) {

            Map<String, List<String>> paramMap = Window.Location.getParameterMap();
            if (paramMap.size() != 0) {
                String params = "";
                for (java.util.Map.Entry<String, List<String>> entry :paramMap.entrySet()) {
                    params += "&"+entry.getKey() +"="+Window.Location.getParameter(entry.getKey());
                }
                Window.open(Window.Location.getPath()+localeExtension +params, "_self", "");
            } else {
                Window.open(Window.Location.getPath()+localeExtension, "_self", "");
            }
		} else
			OauthNetworkClient.getInstance().getOauthClientPackage(new JsonObjectListCallback("oauthInfoList", null) {
			public void onJsonArrayReceived(JSONObject jsonObject) {
				super.onJsonArrayReceived(jsonObject);
				loadPage();
			}
			 public void onJsonObjectReceived(JSONObject object) {
				 
					switch ((int) object.get("providerId").isNumber().doubleValue()) {
					case OauthClient.FBCLIENT:
						OauthFbClient.init(object.get("clientId").isString().stringValue(), object.get("redirectUri").isString().stringValue());
						break;
					case OauthClient.GOOGLECLIENT:
						OauthGoogleClient.init(object.get("clientId").isString().stringValue(), object.get("redirectUri").isString().stringValue());
						break;
					case OauthClient.LINKEDINCLIENT:
						OauthLinkedIn.init(object.get("clientId").isString().stringValue(), object.get("redirectUri").isString().stringValue());
						break;
                    case OauthClient.WESPOTCLIENT:
                            OauthWespot.init(object.get("clientId").isString().stringValue(), object.get("redirectUri").isString().stringValue());
                            break;
					default:
						break;
				}
			 }
		 });
	}

	public void loadPage() {
		final OauthClient client = OauthClient.checkAuthentication();
		if (client != null) {
			String href = Cookies.getCookie("redirectAfterOauth");
			if (href != null) {
				Cookies.removeCookie("redirectAfterOauth");
				Window.open(href, "_self", "");

			} else {
				if (RootPanel.get("button-facebook") != null) (new OauthPage()).loadPage();
				if (RootPanel.get("author") != null) (new AuthorPage()).loadPage();
				if (RootPanel.get("test") != null) (new TestPage()).loadPage();
				if (RootPanel.get("contact") != null) (new AddContactPage()).loadPage();
                if (RootPanel.get("register") != null && Window.Location.getParameter("gameId") !=null) (new RegisterForGame()).loadPage();
                if (RootPanel.get("register") != null && Window.Location.getParameter("runId") !=null) (new RegisterForRun()).loadPage();
				if (RootPanel.get("result") != null) (new ResultDisplayPage()).loadPage();
				if (RootPanel.get("portal") != null) (new org.celstec.arlearn2.portal.client.portal.PortalPage()).loadPage();
				if (RootPanel.get("oauth_new") != null) (new OauthPage()).loadPage();
				if (RootPanel.get("resultDisplayRuns") != null) (new ResultDisplayRuns()).loadPage();
                if (RootPanel.get("htmlDisplay") != null) (new CrsDisplay()).loadPage();
                if (RootPanel.get("resultDisplayRunsParticipate") != null) (new ResultDisplayRunsParticipate()).loadPage();

				if (RootPanel.get("network") != null) (new NetworkPage()).loadPage();
				if (RootPanel.get("search") != null) (new SearchPage()).loadPage();
				if (RootPanel.get("game") != null) (new GamePage()).loadPage();
                if (RootPanel.get("debug") != null) (new DebugPage()).loadPage();
			}
		} else {
			String href = Window.Location.getHref();
			if (href.contains("oauth.html")) {
				(new OauthPage()).loadPage();
			} else {
				Cookies.setCookie("redirectAfterOauth", href);
				Window.open("/oauth.html", "_self", "");
			}
		}
	}

}
