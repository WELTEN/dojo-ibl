package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONObject;

public class JsonObjectListCallback extends JsonCallback {

	private String beanType;
	private DataSourceModel dataSourceModel;

	public JsonObjectListCallback(String beanType, DataSourceModel dataSourceModel) {
		this.beanType = beanType;
		this.dataSourceModel = dataSourceModel;
	}

	public void onJsonReceived(JSONValue jsonValue) {
		JSONObject jsonObject = jsonValue.isObject();
		if (jsonObject == null) {
			return;
		}
		if (jsonObject.containsKey("serverTime") && dataSourceModel != null)
			dataSourceModel.setServerTime((long) jsonObject.get("serverTime").isNumber().doubleValue());
		if (jsonObject.get("error") != null) {
			Authentication.getInstance().disAuthenticate();
		} else {
			onJsonArrayReceived(jsonObject);
		}
	}

	public void onJsonArrayReceived(JSONObject jsonObject) {
		JSONArray array = jsonObject.get(beanType).isArray();
		for (int i = 0; i < array.size(); i++) {
			onJsonObjectReceived(array.get(i).isObject());
		}
		finishedLoadingArray(array.size());
	}
	
	public void finishedLoadingArray(int i) {
		
	}

	public void onJsonObjectReceived(JSONObject jsonObject) {
		if (dataSourceModel != null) dataSourceModel.addJsonObject(jsonObject);
	}

}
