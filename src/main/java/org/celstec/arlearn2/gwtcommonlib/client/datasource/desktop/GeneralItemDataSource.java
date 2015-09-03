package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import java.util.HashMap;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Record;

public class GeneralItemDataSource extends GenericDataSource {
	
	public static GeneralItemDataSource instance;
	public static HashMap<Long, Long> lastSyncDate = new HashMap<Long, Long>();
	public static HashMap<Long, HashMap<Long, Record>> gameToGi = new HashMap<Long, HashMap<Long, Record>>();

	public static GeneralItemDataSource getInstance() {
		if (instance == null)
			instance = new GeneralItemDataSource();
		return instance;
	}
	
	private GeneralItemDataSource() {
		super();
		setDataSourceModel(new GeneralItemModel(this));
	}
	
	public GeneralItemsClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
	}

	
	public void loadDataFromWeb(long gameId) {
		getHttpClient().getGeneralItemsGame(gameId, getLastSyncDate(gameId), 
				new JsonObjectListCallback(getBeanType(),  
				this.getDataSourceModel(), gameId));
	}

	protected String getBeanType() {
		return "generalItems";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb((long) bean.get("gameId").isNumber().doubleValue());
	}
	
	protected long getLastSyncDate(long gameId) {
		if (lastSyncDate.containsKey(gameId)) return lastSyncDate.get(gameId);
		return 0l;
	}
	public class JsonObjectListCallback extends JsonCallback {

		private String beanType;
		private DataSourceModel dataSourceModel;
		private  long gameId;

		public JsonObjectListCallback(String beanType, DataSourceModel dataSourceModel, long gameId) {
			this.beanType = beanType;
			this.dataSourceModel = dataSourceModel;
			this.gameId = gameId;
		}

		public void onJsonReceived(JSONValue jsonValue) {
			JSONObject jsonObject = jsonValue.isObject();
			if (jsonObject == null) {
				return;
			}
			if (jsonObject.containsKey("serverTime") )
				lastSyncDate.put(gameId, (long) jsonObject.get("serverTime").isNumber().doubleValue());
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
			if (dataSourceModel != null) dataSourceModel.addJsonObject(jsonObject);
		}

	}
	
	@Override
	public void saveRecord(AbstractRecord record) {
		super.saveRecord(record);
		Record rec = (Record) record;
		Long gameId = rec.getAttributeAsLong(GameModel.GAMEID_FIELD);
		if (!gameToGi.containsKey(gameId)) {
			gameToGi.put(gameId, new HashMap<Long, Record>());
		}
		gameToGi.get(gameId).put(rec.getAttributeAsLong(GeneralItemModel.GENERALITEMID_FIELD), rec);
	}
	
	public HashMap<Long, Record> getRecords(long gameId){
		return gameToGi.get(gameId);
	}

}
