package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public class GameRoleModel extends DataSourceModel{

	public static final String ROLE_PK_FIELD = "rolePk";
	public static final String ROLE_FIELD = "role";

	public  GameRoleModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, GameModel.GAMEID_FIELD, false, true);
		addField(STRING_DATA_TYPE, ROLE_PK_FIELD, true, true);
		addField(STRING_DATA_TYPE, ROLE_FIELD, false, true);
	}

	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.notification.GameModification";
	}
	
	@Override
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = super.createRecord(object);
		return record;
	}
	
	
}
