package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameAccessModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONObject;

public class GameAccessDataSource extends GenericDataSource {

	public static GameAccessDataSource instance;
	
	public static GameAccessDataSource getInstance() {
		if (instance == null)
			instance = new GameAccessDataSource();
		return instance;
	}
	
	private GameAccessDataSource() {
		super();
		setDataSourceModel(new GameAccessModel(this));
	}
	
	public GenericClient getHttpClient() {
		return GameClient.getInstance();
	}
	public void loadDataFromWeb() {
	}
	
	public void loadDataFromWeb(long gameId) {
		((GameClient) getHttpClient()).getGamesAccess(gameId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));

	}

	protected String getBeanType() {
		return "gamesAccess";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}

}
