package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GameRolesDataSource extends GenericDataSource {
	
	public static GameRolesDataSource instance;

	public static GameRolesDataSource getInstance() {
		if (instance == null)
			instance = new GameRolesDataSource();
		return instance;
	}

	public GameRolesDataSource() {
		super();
		setDataSourceModel(new GameRoleModel(this));
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

	public void addRole (long gameId, String role) {
		String pk = gameId+":"+role;
		if (getRecord(pk) == null) {
		AbstractRecord record = createRecord();
		record.setCorrespondingJsonObject(new JSONObject());
		record.getCorrespondingJsonObject().put(GameRoleModel.ROLE_PK_FIELD, new JSONString(pk));
		record.setAttribute(GameModel.GAMEID_FIELD, gameId);
		record.setAttribute(GameRoleModel.ROLE_PK_FIELD, pk);
		record.setAttribute(GameRoleModel.ROLE_FIELD, role);
		saveRecord(record);
		}
	}

	public void addRole(long gameId, String[] roles) {
		if (roles == null) return;
		for (String role: roles) {
			addRole(gameId, role);
		}
	}

	public void loadRoles(long gameId) {
		Game game = GameDataSource.getInstance().getGame(gameId);
		if (game != null) {
			loadRoles(game);
		} else {
			GameClient.getInstance().getGame(gameId, new JsonCallback() {
				public void onJsonReceived(JSONValue jsonValue) {
					loadRoles(new Game(jsonValue.isObject()));
				}
			});
		}
	}
	
	public void loadRoles(Game game) {
		addRole(game.getGameId(), game.getRoles());
	}

}