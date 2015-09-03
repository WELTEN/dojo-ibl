package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class RunRolesDataSource extends GenericDataSource {
	
	public static RunRolesDataSource instance;

	public static RunRolesDataSource getInstance() {
		if (instance == null)
			instance = new RunRolesDataSource();
		return instance;
	}

	private RunRolesDataSource() {
		super();
		setDataSourceModel(new RunRoleModel(this));
	}

	public GenericClient getHttpClient() {
		return null;
	}

	@Override
	public void loadDataFromWeb() {
		
	}

	@Override
	public void processNotification(JSONObject bean) {
	}

	public void addRole (long runId, String role) {
		if (getRecord(role) == null) {
		AbstractRecord record = createRecord();
		record.setCorrespondingJsonObject(new JSONObject());
		record.getCorrespondingJsonObject().put(RunRoleModel.ROLE_FIELD, new JSONString(role));
		record.setAttribute(RunModel.RUNID_FIELD, runId);
		record.setAttribute(RunRoleModel.ROLE_FIELD, role);
		saveRecord(record);
		}
	}
}
