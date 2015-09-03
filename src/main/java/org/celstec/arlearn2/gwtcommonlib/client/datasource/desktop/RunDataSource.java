package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class RunDataSource extends GenericDataSource {

	public static RunDataSource instance;

	public static RunDataSource getInstance() {
		if (instance == null)
			instance = new RunDataSource();
		return instance;
	}
	
	private RunDataSource() {
		super();
		setDataSourceModel(new RunModel(this));
	}
	
	public GenericClient getHttpClient() {
		return RunClient.getInstance();
	}
	
	public void loadDataFromWeb() {
		((RunClient) getHttpClient()).getRunAccess(getLastSyncDate(), new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			public void onJsonObjectReceived(final JSONObject jsonObject) {
				((RunClient) getHttpClient()).getRun((long)jsonObject.get("runId").isNumber().doubleValue(), new JsonCallback(){
					public void onJsonReceived(final JSONValue jsonValue1) {	
						GameClient.getInstance().getGame((long)jsonValue1.isObject().get(RunModel.GAMEID_FIELD).isNumber().doubleValue(), new JsonCallback(){
							public void onJsonReceived(JSONValue jsonValue) {
								jsonValue1.isObject().put(RunModel.RUN_ACCESS, jsonObject.get(RunModel.RUN_ACCESS));
								jsonValue1.isObject().put(RunModel.GAME_TITLE_FIELD, jsonValue.isObject().get(GameModel.GAME_TITLE_FIELD));
								callSuper(jsonValue1.isObject());
							}
						});		
					}
				});		
			}
			private void callSuper(JSONObject jsonObject) {
				super.onJsonObjectReceived(jsonObject);
			}
		});		
	}

	protected String getBeanType() {
		return "runAccess";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}

	public void loadRun(long runId) {
		((RunClient) getHttpClient()).getRun(runId, new JsonCallback(){
			public void onJsonReceived(final JSONValue jsonValue1) {	
				GameClient.getInstance().getGame((long)jsonValue1.isObject().get(RunModel.GAMEID_FIELD).isNumber().doubleValue(), new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						jsonValue1.isObject().put(RunModel.RUN_ACCESS, new JSONNumber(1));
						jsonValue1.isObject().put(RunModel.GAME_TITLE_FIELD, jsonValue.isObject().get(GameModel.GAME_TITLE_FIELD));
						getDataSourceModel().addJsonObject(jsonValue1.isObject());
					}
				});		
			}
		});		
		
	}


}
