package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class CollaboratorModel extends DataSourceModel {

	public static final String ACCESS_RIGHTS = "accessRights";
	public static final String ACCOUNT_TYPE_FIELD = "accountType";
	public static final String LOCAL_ID_FIELD = "localId";
	public static final String ACCOUNT_FIELD = "account";
	public static final String EMAIL_FIELD = "email";
	public static final String NAME_FIELD = "name";
	public static final String GIVEN_NAME_FIELD = "givenName";
	public static final String FAMILY_NAME_FIELD = "familyName";
	public static final String PICTURE_FIELD = "picture";
	public static final String ACCESS_PICTURE = "accessPicture";
	public CollaboratorModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, ACCESS_RIGHTS, false, true);
		addField(INTEGER_DATA_TYPE, ACCOUNT_TYPE_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, GameModel.GAMEID_FIELD, false, true);
		addField(STRING_DATA_TYPE, LOCAL_ID_FIELD, false, true);
		addField(STRING_DATA_TYPE, EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
		addField(STRING_DATA_TYPE, GIVEN_NAME_FIELD, false, true);
		addField(STRING_DATA_TYPE, FAMILY_NAME_FIELD, false, true);
		addField(STRING_DATA_TYPE, PICTURE_FIELD, false, true);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				int type = (int) jsonObject.get(ACCOUNT_TYPE_FIELD).isNumber().doubleValue();
				int gameId = (int) jsonObject.get(GameModel.GAMEID_FIELD).isNumber().doubleValue();
				String lId = jsonObject.get(LOCAL_ID_FIELD).isString().stringValue();
				jsonObject.put(ACCOUNT_FIELD, new JSONString(type+":"+lId+":"+gameId));
				return type+":"+lId+":"+gameId;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return ACCOUNT_FIELD;
			}
		}, true, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				int access = (int) jsonObject.get(ACCESS_RIGHTS).isNumber().doubleValue();
				switch (access) {
				case 1:
					return "owner.png";
					
				case 2:
					return "icon_edit.png";

				default:
					break;
				}
				return "viewIcon.png";
				
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return ACCESS_PICTURE;
			}
		}, false, false);
	}
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.account.Account";
	}
}
