package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

import java.util.ArrayList;
import java.util.List;

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
public class ObjectCollectionDisplay extends GeneralItem {
    public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay";
    public  final static String TYPE_ZONE = "org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay$DisplayZone";
    public  final static String TYPE_OBJECT = "org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay$DisplayObject";
    public  final static String DISPLAY_ZONES = "displayZones";
    public  final static String DISPLAY_OBJECT = "objects";
    private String[] zones;

    public ObjectCollectionDisplay() {
        super();
    }

    @Override
    public Canvas getViewerComponent() {
        return null;
    }

    @Override
    public Canvas getMetadataExtensionEditor() {
        return null;
    }

    @Override
    public boolean enableDataCollection() {
        return false;
    }

    public ObjectCollectionDisplay(JSONObject object) {
        super(object);
    }

    public String getType() {
        return TYPE;
    }

    public List<Zone> getZones() {
        ArrayList<Zone> zones = new ArrayList<Zone>();
        if (!getJsonRep().containsKey(DISPLAY_ZONES)) return zones;
        JSONArray zonesJsonArray = getJsonRep().get(DISPLAY_ZONES).isArray();
        for (int i = 0; i < zonesJsonArray.size(); i++ ) {
            zones.add(new Zone(zonesJsonArray.get(i).isObject()));
        }
        return zones;
    }

    public class Zone extends Bean {


        public Zone(JSONObject json) {
            super(json);
        }

        @Override
        public String getType() {
            return TYPE_ZONE;
        }

        public String getTitle() {
            return getString("title");
        }

        public List<DisplayObject> getZones() {
            ArrayList<DisplayObject> objects = new ArrayList<DisplayObject>();
            if (!getJsonRep().containsKey(DISPLAY_OBJECT)) return objects;
            JSONArray zonesJsonArray = getJsonRep().get(DISPLAY_OBJECT).isArray();
            for (int i = 0; i < zonesJsonArray.size(); i++ ) {
                objects.add(new DisplayObject(zonesJsonArray.get(i).isObject()));
            }
            return objects;
        }
    }

    public class DisplayObject extends Bean{
        public DisplayObject(JSONObject json) {
            super(json);
        }

        public String getUrl(){
            return getString("imgUrl");
        }

        public String getAction() {
            if (!getJsonRep().containsKey("dependsOn")) return null;
            if (!getJsonRep().get("dependsOn").isObject().containsKey("action")) return null;
            return getJsonRep().get("dependsOn").isObject().get("action").isString().stringValue();
        }

        public Long getGeneralItemId() {
            if (!getJsonRep().containsKey("dependsOn")) return null;
            if (!getJsonRep().get("dependsOn").isObject().containsKey("generalItemId")) return null;
            return (long) getJsonRep().get("dependsOn").isObject().get("generalItemId").isNumber().doubleValue();
        }

        @Override
        public String getType() {
            return TYPE_OBJECT;
        }
    }

}
