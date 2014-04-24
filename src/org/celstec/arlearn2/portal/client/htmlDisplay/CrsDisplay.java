package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallbackGeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.response.ResponseClient;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwtcommonlib.client.objects.*;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ObjectCollectionDisplay;

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
public class CrsDisplay {

    private long runId;
    private long generalItemId;
    private long gameId;


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
        startNotificationListener();
        loadGeneralItem();
    }

    private void startNotificationListener() {
        NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Response", new NotificationHandler() {

            @Override
            public void onNotification(JSONObject bean) {

                Response response = new Response(bean);
                generalItemDisplay.handleResponse(response);
            }
        });

        NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Action", new NotificationHandler() {

            @Override
            public void onNotification(JSONObject bean) {
                Action action = new Action(bean);
                generalItemDisplay.handleAction(action);
            }
        });
    }

    private GeneralItemDisplay generalItemDisplay;

    public static native void exportStaticMethod() /*-{
        $wnd.showAlert =
            $entry(@org.celstec.arlearn2.portal.client.htmlDisplay.ObjectCollectionCRSDisplay::showAlert());
    }-*/;

    private void loadGeneralItem() {
        exportStaticMethod();
        GeneralItemsClient.getInstance().getGeneralItem(gameId, generalItemId, new JsonCallbackGeneralItem(){

            public void onGeneralItemReceived(GeneralItem gi) {
                if (gi.getType().equals(SingleChoiceTest.TYPE)) {
                    generalItemDisplay = new SingleChoiceDisplay((SingleChoiceTest) gi);
                    SingleChoiceTest sct = (SingleChoiceTest) gi;
                    loadResponses(runId);
                } else if (gi.getType().equals(SingleChoiceImage.TYPE)) {
                    generalItemDisplay = new SingleChoiceDisplay((SingleChoiceImage) gi);
                    SingleChoiceTest sct = new SingleChoiceTest(gi.getJsonRep());
                    loadResponses(runId);

                } else if (gi.getType().equals(ObjectCollectionDisplay.TYPE)) {
                    generalItemDisplay = new ObjectCollectionCRSDisplay((ObjectCollectionDisplay) gi);
                }
                loadUsers(runId);

                RootPanel.get("htmlDisplay").add(generalItemDisplay.getCanvas());
            }



        });
    }



    private void loadResponses(Long runId) {
        ResponseClient.getInstance().getResponses(runId, generalItemId, new JsonCallback() {
            public void onJsonReceived(JSONValue jsonValue) {
                if (jsonValue.isObject().containsKey("responses")) {
                    JSONArray array = jsonValue.isObject().get("responses").isArray();
                    for (int i = 0; i< array.size(); i++) {
                        Response response = new Response(array.get(i).isObject());

                        generalItemDisplay.handleResponse(response);

                    }
                }

            };
        });
    }



    private void loadUsers(long runId) {
        UserClient.getInstance().getUsers(runId, new JsonCallback() {
            public void onJsonReceived(JSONValue jsonValue) {

                if (jsonValue.isObject().containsKey("users")) {
                    JSONArray array = jsonValue.isObject().get("users").isArray();
                    for (int i = 0; i< array.size();i++) {
                        JSONObject userObject = array.get(i).isObject();
                        Account account = new Account(userObject);
                        generalItemDisplay.putAccount(account);

                    }
                }

            }
        });

    }



}
