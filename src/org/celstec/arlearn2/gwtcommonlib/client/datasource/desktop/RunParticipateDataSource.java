package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

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
public class RunParticipateDataSource extends GenericDataSource {

    public static RunParticipateDataSource instance;

    public static RunParticipateDataSource getInstance() {
        if (instance == null)
            instance = new RunParticipateDataSource();
        return instance;
    }

    private RunParticipateDataSource() {
        super();
        setDataSourceModel(new RunModel(this));
    }

    public GenericClient getHttpClient() {
        return RunClient.getInstance();
    }

    public void loadDataFromWeb() {
        ((RunClient) getHttpClient()).runsParticipate(new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
            public void onJsonObjectReceived(JSONObject jsonObject) {
                jsonObject.put("accessRights", new JSONNumber(3));
                getDataSourceModel().addJsonObject(jsonObject);
            }

        });
    }

    protected String getBeanType() {
        return "runs";
    }

    @Override
    public void processNotification(JSONObject bean) {
        loadDataFromWeb();
    }
}
