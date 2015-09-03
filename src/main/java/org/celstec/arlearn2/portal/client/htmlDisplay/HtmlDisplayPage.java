package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.HTMLPane;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

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
public class HtmlDisplayPage {
    private long runId;
    private long generalItemId;
    private long gameId;
    String contents = "";

    private void loadParameters() {
        String gameIdAsString = com.google.gwt.user.client.Window.Location.getParameter("gameId");
        if (gameIdAsString == null) gameIdAsString = "0";
        gameId = Long.parseLong(gameIdAsString);

        String runIdAsString = com.google.gwt.user.client.Window.Location.getParameter("runId");
        if (runIdAsString == null) runIdAsString = "0";
        runId = Long.parseLong(runIdAsString);

        String generalItemIdAsString = com.google.gwt.user.client.Window.Location.getParameter("generalItemId");
        if (generalItemIdAsString == null) generalItemIdAsString = "0";
        generalItemId = Long.parseLong(generalItemIdAsString);

    }

    public void loadPage() {
        loadParameters();
        final HTMLPane htmlPane = new HTMLPane();
        htmlPane.setEvalScriptBlocks(true);
        GeneralItemsClient.getInstance().getGeneralItem(gameId, generalItemId, new JsonCallback(){
            public void onJsonReceived(JSONValue jsonValue) {
                System.out.println(jsonValue);
                String richText = jsonValue.isObject().get("richText").isString().stringValue();
                System.out.println("string "+richText);
                contents = richText;
                htmlPane.setContents(contents);
//                RootPanel.get("htmlDisplay").getElement().setInnerHTML(contents);
            }
        });

        htmlPane.setWidth100();
        htmlPane.setHeight100();
        htmlPane.setShowEdges(true);

        NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Message", new NotificationHandler() {

            @Override
            public void onNotification(JSONObject bean) {
                hideElement(bean.get("title").isString().stringValue());
//                htmlPane.setContents(contents);
            }
        });

        RootPanel.get("htmlDisplay").add(htmlPane);
        loadUsers(runId);
    }

    public static native void hideElement(String id) /*-{
        $doc.getElementById(id).style.display = 'none';
        $wnd.testArlearn = 'dit is een test';
    }-*/;

    private void loadUsers(long runId) {
        UserClient.getInstance().getUsers(runId, new JsonCallback() {
            public void onJsonReceived(JSONValue jsonValue) {
                System.out.println("users "+jsonValue);
                if (jsonValue.isObject().containsKey("users")) {
                    JSONArray array = jsonValue.isObject().get("users").isArray();
                    for (int i = 0; i< array.size();i++) {
                        JSONObject userObject = array.get(i).isObject();
                        registerUser(userObject.get("localId").isString().stringValue(),
                                userObject.get("name").isString().stringValue(),
                                userObject.get("picture").isString().stringValue());
                    }
                    updateUsers();
                }

            }
        });

    }

    public static native void updateUsers() /*-{
        if ($wnd.updateUsers != null) $wnd.updateUsers();
    }-*/;

    public static native void registerUser(String userId, String name, String url) /*-{
        if ($wnd.runUser == null) $wnd.runUser = {};
        $wnd.runUser[userId] = {};
        $wnd.runUser[userId].name = name;
        $wnd.runUser[userId].url = url;
    }-*/;
}
