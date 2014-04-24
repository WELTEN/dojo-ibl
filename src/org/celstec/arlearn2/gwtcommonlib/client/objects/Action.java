package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;

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
public class Action extends Bean {
    public  final static String TYPE = "org.celstec.arlearn2.beans.run.Action";
    public  final static String ACTION_STRING = "action";
    public  final static String FULL_ID = "userEmail";
    public  final static String GENERAL_ITEM_ID = "generalItemId";

    public Action(JSONObject object) {
        super(object);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getAccount() {
        return getString(FULL_ID);
    }

    public String getAction(){
        return getString(ACTION_STRING);
    }

    public Long getGeneralItemId() {
        return getLong(GENERAL_ITEM_ID);
    }
}