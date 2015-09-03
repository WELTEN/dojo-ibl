package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ActionModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonResumptionListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.action.ActionClient;

import com.google.gwt.json.client.JSONObject;

import java.util.HashMap;

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
public class ActionsDataSource extends GenericDataSource {

    public static ActionsDataSource instance;

    public static ActionsDataSource getInstance() {
        if (instance == null)
            instance = new ActionsDataSource();
        return instance;
    }

    public ActionClient getHttpClient() {
        return ActionClient.getInstance();
    }

    private ActionsDataSource() {
        setDataSourceModel(new ActionModel(this));
    }
    @Override
    public void loadDataFromWeb() {
    }

    @Override
    public void processNotification(JSONObject bean) {
        loadDataFromWeb((long) bean.get("runId").isNumber().doubleValue());
    }


    public void loadDataFromWeb(final long runId) {
        JsonResumptionListCallback callback = new JsonResumptionListCallback(getBeanType(), this.getDataSourceModel(), 0l) {

            @Override
            public void nextCall() {
                ((ActionClient) getHttpClient()).getActions(runId, ActionsDataSource.this.lastSyncDate, resumptionToken, this);
            }
        };
        ((ActionClient) getHttpClient()).getActions(runId, ActionsDataSource.this.lastSyncDate, null, callback);
    }

    protected String getBeanType() {
        return "actions";
    }
}
