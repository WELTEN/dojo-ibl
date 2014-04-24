package org.celstec.arlearn2.gwtcommonlib.client.datasource;

public class GameAccessModel extends DataSourceModel {

	public static final String ACCOUNT_FIELD = "account";
	public static final String ACCESSRIGHT_FIELD = "accessRights";
	public static final String GAMEID_FIELD = "gameId";

	public GameAccessModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		
		addField(STRING_DATA_TYPE, ACCOUNT_FIELD, true, true);
		addField(INTEGER_DATA_TYPE, ACCESSRIGHT_FIELD, false, true);
		addField(LONG_DATA_TYPE, GAMEID_FIELD, false, true);
	}

	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.game.GameAccess";
	}
}
