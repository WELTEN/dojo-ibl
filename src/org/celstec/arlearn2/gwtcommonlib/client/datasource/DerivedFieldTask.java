package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public interface DerivedFieldTask {

	public Object process();
	public int getType();
	public String getTargetFieldName();
	public void setJsonSource(JSONObject jsonObject);
}
