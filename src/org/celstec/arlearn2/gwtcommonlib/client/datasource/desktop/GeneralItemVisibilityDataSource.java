package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import com.google.gwt.json.client.JSONObject;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemsVisibilityModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsVisibilityClient;

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
public class GeneralItemVisibilityDataSource  extends GenericDataSource {

    public static GeneralItemVisibilityDataSource instance;

    public static GeneralItemVisibilityDataSource getInstance() {
        if (instance == null)
            instance = new GeneralItemVisibilityDataSource();
        return instance;
    }

    public GeneralItemsVisibilityClient getHttpClient() {
        return GeneralItemsVisibilityClient.getInstance();
    }

    private GeneralItemVisibilityDataSource() {
        super();
        setDataSourceModel(new GeneralItemsVisibilityModel(this));
    }
    @Override
    public void loadDataFromWeb() {
    }

    @Override
    public void processNotification(JSONObject bean) {
        loadDataFromWeb((long) bean.get("runId").isNumber().doubleValue());
    }


    public void loadDataFromWeb(final long runId) {
        getHttpClient().getVisibilityStatements(runId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
    }

    protected String getBeanType() {
        return "generalItemsVisibility";
    }
}
