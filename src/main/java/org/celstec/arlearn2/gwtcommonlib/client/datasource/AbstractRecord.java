package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public interface AbstractRecord {

	public void setAttribute(String property, Object value);
	
	public void setAttribute(String property, String[] value);	
	public void setCorrespondingJsonObject(JSONObject object);

	public JSONObject getCorrespondingJsonObject();
	
}
