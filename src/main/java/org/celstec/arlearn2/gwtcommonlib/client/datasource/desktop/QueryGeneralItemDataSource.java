package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;

import com.google.gwt.json.client.JSONObject;

public class QueryGeneralItemDataSource extends GenericDataSource {

	public QueryGeneralItemDataSource() {
		super();
		setDataSourceModel(new GeneralItemModel(this));
	}
	
	@Override
	public GenericClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}

	public void search(String query) {
		((GeneralItemsClient) getHttpClient()).search(query, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			
			public void onJsonArrayReceived(JSONObject jsonObject) {
				for (Map.Entry<Object, AbstractRecord> entry: recordMap.entrySet()) {
					QueryGeneralItemDataSource.this.removeRecord(entry.getValue());

				}
				super.onJsonArrayReceived(jsonObject);
			}
		});
	}
	
	@Override
	public void loadDataFromWeb() {
		
	}

	@Override
	public void processNotification(JSONObject bean) {
	
	}
	
	protected String getBeanType() {
		return "generalItems";
	}

}
