package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;

import sun.misc.IOUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
public class GlassDelegator extends GoogleDelegator{

    private static GlassDelegator glassDelegator;

    public GlassDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public GlassDelegator() {
        super();
    }

    public static boolean glassCanProcess(String account, HashMap<String,Object> valueMap) {
        if (account.equals("2:116757187626671489073")) {
            if (valueMap.containsKey("type")) {
//                if (GeneralItemModification.class.getCanonicalName().equals(valueMap.get("type"))){
                if (GeneralItemModification.class.getCanonicalName().equals(valueMap.get("type")) && valueMap.get("modificationType").equals(GeneralItemModification.VISIBLE)){
                    return true;
                }
            }
        }
        return false;
    }

    public void processGlassRequest(String account, HashMap<String,Object> valueMap) {
        if (this.authToken == null || this.authToken.startsWith("onBehalfOf")) {
            return;
        }
        displayItem("116757187626671489073", (Long) valueMap.get("itemId"));
    }

    private void displayItem(String s, Long itemId) {
        GeneralItem gi = GeneralItemManager.getGeneralItem(itemId);

//        if (gi instanceof org.celstec.arlearn2.beans.generalItem.VideoObject) {
//            displayItem((org.celstec.arlearn2.beans.generalItem.VideoObject)gi);
//            return;
//        }

        try {
            URL url = new URL("https://www.googleapis.com/mirror/v1/timeline");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer "+this.authToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            JSONObject json = getJson(gi);
            if (gi instanceof org.celstec.arlearn2.beans.generalItem.VideoObject) {
                json = getJson((org.celstec.arlearn2.beans.generalItem.VideoObject)gi);
            } else if (gi instanceof AudioObject) {
                json = getJson((AudioObject)gi);
            }
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json.toString());
            writer.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("ok");

            } else {
                System.out.println("nok" +connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject getJson(GeneralItem gi) throws JSONException{
        String html = "<article class=\"auto-paginate\"><h1>"+gi.getName()+"</h1><br>";
        JSONObject json = JsonBeanSerialiser.serialiseToJson(gi);
        if (json.has("richText")) {
            html += json.getString("richText");
        }

        html += "</article>";
        json.put("html", html);
        return json;
    }

    public JSONObject getJson(org.celstec.arlearn2.beans.generalItem.VideoObject gi) throws JSONException{
        String html = "<article class=\"auto-paginate\"><h1>"+gi.getName()+"</h1><br>";
        JSONObject json = JsonBeanSerialiser.serialiseToJson(gi);

        if (json.has("richText")) {
            html += json.getString("richText");
        }

        html += "</article>";
        JSONObject jsonresult = new JSONObject();
        jsonresult.put("title", gi.getName());
        jsonresult.put("html", html);
        JSONArray menuItems = new JSONArray();
        JSONObject menuItem =new JSONObject();
        jsonresult.put("menuItems", menuItems);
        menuItem.put("id", "play");
        menuItem.put("action", "PLAY_VIDEO");
        menuItem.put("payload", gi.getVideoFeed());
        JSONArray values = new JSONArray();
        JSONObject valuesItem  =new JSONObject();
        valuesItem.put("displayName","Play Video");
        menuItem.put("values", values);
        menuItems.put(menuItem);
        values.put(valuesItem);
        JSONObject notification =new JSONObject();
        notification.put("level", "DEFAULT");

        jsonresult.put("notification", notification);
        return jsonresult;
    }

    public JSONObject getJson(AudioObject gi) throws JSONException{
        String html = "<article class=\"auto-paginate\"><h1>"+gi.getName()+"</h1><br>";
        JSONObject json = JsonBeanSerialiser.serialiseToJson(gi);

        if (json.has("richText")) {
            html += json.getString("richText");
        }

        html += "</article>";
        JSONObject jsonresult = new JSONObject();
        jsonresult.put("title", gi.getName());
        jsonresult.put("html", html);
        JSONArray menuItems = new JSONArray();
        JSONObject menuItem =new JSONObject();
        jsonresult.put("menuItems", menuItems);
        menuItem.put("id", "play");
        menuItem.put("action", "PLAY_VIDEO");
        menuItem.put("payload", gi.getAudioFeed());
        JSONArray values = new JSONArray();
        JSONObject valuesItem  =new JSONObject();
        valuesItem.put("displayName","Play Audio");
        menuItem.put("values", values);
        menuItems.put(menuItem);
        values.put(valuesItem);
        JSONObject notification =new JSONObject();
        notification.put("level", "DEFAULT");

        jsonresult.put("notification", notification);
        return jsonresult;
    }

    private void displayItem(org.celstec.arlearn2.beans.generalItem.VideoObject gi) {
        String video = gi.getVideoFeed();
        System.out.println("video url "+video);
        System.out.println(this.authToken);


    }


}
