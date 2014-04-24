package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.TeamClient;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Record;

public class TeamDataSource extends GenericDataSource {

	public static TeamDataSource instance;

	public static TeamDataSource getInstance() {
		if (instance == null)
			instance = new TeamDataSource();
		return instance;
	}
	
	private TeamDataSource() {
		super();
		setDataSourceModel(new TeamModel(this));
	}
	
	public GenericClient getHttpClient() {
		return TeamClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromWeb(long runId) {
		for (Map.Entry entry: recordMap.entrySet()) {
			removeData((Record) entry.getValue());
		}
		((TeamClient) getHttpClient()).getTeams(runId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	protected String getBeanType() {
		return "teams";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb((long) bean.get("runId").isNumber().doubleValue());
		
	}
	
	public void setServerTime(long serverTime) {}
}
