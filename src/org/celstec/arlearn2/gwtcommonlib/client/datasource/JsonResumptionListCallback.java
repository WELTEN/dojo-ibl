package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class JsonResumptionListCallback extends JsonCallback {

	private String beanType;
	protected Long from;
	protected String resumptionToken;
	private DataSourceModel dataSourceModel;

	public JsonResumptionListCallback(String beanType, DataSourceModel dataSourceModel, Long from) {
		this.beanType = beanType;
		this.dataSourceModel = dataSourceModel;
		this.from = from;
	}

	public void onJsonReceived(JSONValue jsonValue) {
		JSONObject jsonObject = jsonValue.isObject();
		if (jsonObject == null) {
			return;
		}
		if (jsonObject.containsKey("resumptionToken")) {
			resumptionToken = jsonObject.get("resumptionToken").isString().stringValue();
			nextCall();
		} else {
			if (jsonObject.containsKey("serverTime"))
				dataSourceModel.setServerTime((long) jsonObject.get("serverTime").isNumber().doubleValue());
		}
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
	}

	public void onJsonObjectReceived(JSONObject jsonObject) {
		dataSourceModel.addJsonObject(jsonObject);
	}

	public abstract void nextCall();

}
