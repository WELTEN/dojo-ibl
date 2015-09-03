package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;

import java.util.Date;

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
public class GeneralItemsVisibilityModel extends DataSourceModel {


    public static final String TIMESTAMP_FIELD = "timeStamp";
    public static final String STATUS_FIELD = "status";
    public static final String TIMESTAMP_PRETTY_FIELD = "timestampPretty";
    public static final String LASTMODIFICATION_FIELD = "lastModificationDate";
    public static final String LASTMODIFICATIONPRETTY_FIELD = "lastModificationDatePretty";
    public static final String RUNID_FIELD = "runId";
    public static final String GENERALITEMID_FIELD = "generalItemId";
    public static final String ACCOUNT_FIELD = "email";

    public GeneralItemsVisibilityModel(DataSourceAdapter dataSourceAdapter) {
        super(dataSourceAdapter);
    }

    @Override
    protected void initFields() {
        addField(INTEGER_DATA_TYPE, STATUS_FIELD, false, true);
        addField(INTEGER_DATA_TYPE, LASTMODIFICATION_FIELD, true, true);
        addField(INTEGER_DATA_TYPE, TIMESTAMP_FIELD, false, true);
        addField(INTEGER_DATA_TYPE, RUNID_FIELD, false, true);
        addField(INTEGER_DATA_TYPE, GENERALITEMID_FIELD, false, true);
        addField(STRING_DATA_TYPE, ACCOUNT_FIELD, false, true);

        addDerivedField(new DerivedFieldTask() {
            JSONObject jsonObject;

            @Override
            public void setJsonSource(JSONObject jsonObject) {
                this.jsonObject = jsonObject;
            }

            @Override
            public Object process() {

                long timeStamp = (long) jsonObject.get(LASTMODIFICATION_FIELD).isNumber().doubleValue();
                Date timeDate = new Date(timeStamp);

                DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd hh:mm:ss:SSS aaa");


                return fmt.format(timeDate);
            }

            @Override
            public int getType() {
                return STRING_DATA_TYPE;
            }

            @Override
            public String getTargetFieldName() {
                return LASTMODIFICATIONPRETTY_FIELD;
            }
        }, false, true);

        addDerivedField(new DerivedFieldTask() {
            JSONObject jsonObject;

            @Override
            public void setJsonSource(JSONObject jsonObject) {
                this.jsonObject = jsonObject;
            }

            @Override
            public Object process() {

                long timeStamp = (long) jsonObject.get(TIMESTAMP_FIELD).isNumber().doubleValue();
                Date timeDate = new Date(timeStamp);

                DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd hh:mm:ss:SSS aaa");


                return fmt.format(timeDate);
            }

            @Override
            public int getType() {
                return STRING_DATA_TYPE;
            }

            @Override
            public String getTargetFieldName() {
                return TIMESTAMP_PRETTY_FIELD;
            }
        }, false, true);

    }

    protected String getNotificationType() {
        return "org.celstec.arlearn2.beans.run.GeneralItemVisibility";
    }
}