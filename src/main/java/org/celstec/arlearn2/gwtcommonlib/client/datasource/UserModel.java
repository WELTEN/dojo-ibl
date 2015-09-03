package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public class UserModel extends DataSourceModel {

	public static final String PK_FIELD = "pkField";
	public static final String LOCALID = "localId";
	public static final String EMAIL_FIELD = "email";
	public static final String NAME_FIELD = "name";
	public static final String ROLES_FIELD = "roles";
	public static final String PICTURE_FIELD = "picture";
	public static final String FULL_ACCOUNT_FIELD = "fullAccount";
	
	
	public UserModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, LOCALID, false, true);
		addField(STRING_DATA_TYPE, TeamModel.TEAMID_FIELD, false, true);
		addField(STRING_DATA_TYPE, EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
		addField(STRING_DATA_TYPE, PICTURE_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, ContactModel.ACCOUNT_TYPE_FIELD, false, true);
		addField(ENUM_DATA_TYPE, ROLES_FIELD, false, true);
//		addField(BOOLEAN_DATA_TYPE, GameModel.DELETED_FIELD, false, true);

		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				long runId = (long) jsonObject.get(RunModel.RUNID_FIELD).isNumber().doubleValue();
				long accountType = (long) jsonObject.get(ContactModel.ACCOUNT_TYPE_FIELD).isNumber().doubleValue();
				String account = jsonObject.get(LOCALID).isString().stringValue();
				
				return runId+":"+accountType+":"+account;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return PK_FIELD;
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
				long accountType = (long) jsonObject.get(ContactModel.ACCOUNT_TYPE_FIELD).isNumber().doubleValue();
				String account = jsonObject.get(LOCALID).isString().stringValue();
				
				return accountType+":"+account;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return FULL_ACCOUNT_FIELD;
			}
		}, false, false);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				if (jsonObject.containsKey(GameModel.DELETED_FIELD)) {
					return jsonObject.get(GameModel.DELETED_FIELD).isBoolean().booleanValue();
				}
				return false;
			}

			@Override
			public int getType() {
				return BOOLEAN_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return GameModel.DELETED_FIELD;
			}
		}, false, false);

	}

	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.run.User";
	}

	public Object getPrimaryKey(AbstractRecord record) {
		long runId = (long) record.getCorrespondingJsonObject().get(RunModel.RUNID_FIELD).isNumber().doubleValue();
		String teamId = "";
		if (record.getCorrespondingJsonObject().containsKey(TeamModel.TEAMID_FIELD)) teamId = record.getCorrespondingJsonObject().get(TeamModel.TEAMID_FIELD).isString().stringValue();
		long accountType = (long) record.getCorrespondingJsonObject().get(ContactModel.ACCOUNT_TYPE_FIELD).isNumber().doubleValue();
		String localId = record.getCorrespondingJsonObject().get(LOCALID).isString().stringValue();
		
//		return runId+":"+teamId+":"+accountType+":"+account;
//		long runId = (long) record.getCorrespondingJsonObject().get(RunModel.RUNID_FIELD).isNumber().doubleValue();
//		long accountType = (long) record.getCorrespondingJsonObject().get(ContactModel.ACCOUNT_TYPE_FIELD).isNumber().doubleValue();
//		String localId = record.getCorrespondingJsonObject().get(LOCALID).isString().stringValue();
		return createKey(runId, accountType,  localId);
	}
	
	public static String createKey(long runId, long accountType, String localId) {
		return runId+":"+accountType+":"+localId;

	}
	
	public static String createKey(long runId, String fullAccount) {
		return runId+":"+fullAccount;
	}
}
