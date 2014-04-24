package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceAdapter;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;

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
public class GameFileModel extends DataSourceModel {

    public static final String PRIMARY_KEY = "pk";
    public static final String FILE_ID = "fileId";
    public static final String FILE_URL = "fileUrl";

    public  GameFileModel(DataSourceAdapter dataSourceAdapter) {
        super(dataSourceAdapter);
    }

    @Override
    protected void initFields() {
        addField(INTEGER_DATA_TYPE, GameModel.GAMEID_FIELD, false, true);
        addField(STRING_DATA_TYPE, PRIMARY_KEY, true, true);
        addField(STRING_DATA_TYPE, FILE_ID, false, true);
        addField(STRING_DATA_TYPE, FILE_URL, false, true);
    }


    protected String getNotificationType() {
        return "org.celstec.arlearn2.beans.notification.GameModification";
    }

    @Override
    protected AbstractRecord createRecord(JSONObject object) {
        AbstractRecord record = super.createRecord(object);
        return record;
    }


}