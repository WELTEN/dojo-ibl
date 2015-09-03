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
public class MatrixCollectionDisplay extends GeneralItem {

    public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.MatrixCollectionDisplay";
    public  final static String TYPE_COLUMN = "org.celstec.arlearn2.beans.generalItem.MatrixCollectionDisplay$DisplayColumn";
    public  final static String TYPE_ROW = "org.celstec.arlearn2.beans.generalItem.MatrixCollectionDisplay$DisplayRow";

    public  final static String DISPLAY_COLUMNS = "displayColumns";
    public  final static String DISPLAY_ROWS = "displayRows";

    public MatrixCollectionDisplay() {
        super();
    }

    public MatrixCollectionDisplay(JSONObject object) {
        super(object);
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

    public String getType() {
        return TYPE;
    }

    public List<Column> getColumns(){
        ArrayList<Column> zones = new ArrayList<Column>();
        if (!getJsonRep().containsKey(DISPLAY_COLUMNS)) return zones;
        JSONArray zonesJsonArray = getJsonRep().get(DISPLAY_COLUMNS).isArray();
        for (int i = 0; i < zonesJsonArray.size(); i++ ) {
            zones.add(new Column(zonesJsonArray.get(i).isObject()));
        }
        return zones;
    }

    public class Column extends Bean {
        public Column(JSONObject json) {
            super(json);
        }

        @Override
        public String getType() {
            return TYPE_COLUMN;
        }

        public Long getItemId() {
            return getLong("itemId");
        }

    }

    public List<Row> getRows(){
        ArrayList<Row> zones = new ArrayList<Row>();
        if (!getJsonRep().containsKey(DISPLAY_ROWS)) return zones;
        JSONArray zonesJsonArray = getJsonRep().get(DISPLAY_ROWS).isArray();
        for (int i = 0; i < zonesJsonArray.size(); i++ ) {
            zones.add(new Row(zonesJsonArray.get(i).isObject()));
        }
        return zones;
    }

    public class Row extends Bean {
        public Row(JSONObject json) {
            super(json);
        }

        @Override
        public String getType() {
            return TYPE_ROW;
        }

        public String getAction() {
            return getString("action");
        }

    }

}
