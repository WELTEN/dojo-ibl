package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Record;

public class QueryGameDataSource extends GenericDataSource {

	public QueryGameDataSource() {
		super();
		setDataSourceModel(new GameModel(this));
	}
	
	@Override
	public GenericClient getHttpClient() {
		return GameClient.getInstance();
	}

	@Override
	public void loadDataFromWeb() {
		
	}
	
	public void search(String query) {
		((GameClient) getHttpClient()).search(query, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			
			public void onJsonArrayReceived(JSONObject jsonObject) {
//				Set<Map.Entry<Object, AbstractRecord>> set = ;
				for (Map.Entry<Object, AbstractRecord> entry: recordMap.entrySet()) {
					QueryGameDataSource.this.removeRecord(entry.getValue());

				}
				super.onJsonArrayReceived(jsonObject);
			}
		});
	}

	@Override
	public void processNotification(JSONObject bean) {
	}

	protected String getBeanType() {
		return "games";
	}
}
