package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.CollaboratorModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class GameCollaboratorDataSource extends GenericDataSource {

	public static GameCollaboratorDataSource instance;

	public static GameCollaboratorDataSource getInstance() {
		if (instance == null)
			instance = new GameCollaboratorDataSource();
		return instance;
	}
	
	private GameCollaboratorDataSource() {
		super();
		setDataSourceModel(new CollaboratorModel(this));
	}
	
	
	public GenericClient getHttpClient() {
		return CollaborationClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromWeb(final long gameId) {
		GameClient.getInstance().getGamesAccessAccount(gameId, new JsonObjectListCallback("gamesAccess", getDataSourceModel()) {
			public void onJsonObjectReceived(final JSONObject jsonObject) {
				String account = jsonObject.get("account").isString().stringValue();
				CollaborationClient.getInstance().getContact(account, new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						jsonValue.isObject().put("accessRights", jsonObject.get("accessRights"));
						jsonValue.isObject().put(GameModel.GAMEID_FIELD, new JSONNumber(gameId));
						callSuper(jsonValue.isObject());
					}
				});
			}
			private void callSuper(JSONObject jsonObject) {
				System.out.println("callSuper "+jsonObject);
				super.onJsonObjectReceived(jsonObject);
			}
		});
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		// TODO Auto-generated method stub
		
	}
	
	protected String getBeanType() {
		return "gamesAccess";
	}
	
	
	public void setServerTime(long serverTime) {}
}
