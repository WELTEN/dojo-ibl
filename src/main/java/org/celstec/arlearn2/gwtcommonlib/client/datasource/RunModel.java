package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
//import org.celstec.arlearn2.mobileclient.client.common.datasource.mobile.GameDataSource;
import com.google.gwt.json.client.JSONParser;

public class RunModel extends DataSourceModel {

	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	public static final String RUNID_FIELD = "runId";
	public static final String RUN_ACCESS = "accessRights";
	public static final String RUN_ACCESS_STRING = "accessRightsString";
	public static final String RUNTITLE_FIELD = "title";
	public static final String GAME_TITLE_FIELD = "gameTitle";
	public static final String RUN_DELETED_FIELD = "deleted";
	public static final String RUN_OWNER_FIELD = "owner";
	public static final String GAMEID_FIELD = "gameId";
    public static final String DELETED_ICON = "deleteIcon";

	
	public RunModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, RUNID_FIELD, true, true);
		addField(INTEGER_DATA_TYPE, RUN_ACCESS, false, true);
		addField(INTEGER_DATA_TYPE, GAMEID_FIELD, false, true);
		addField(STRING_DATA_TYPE, GAME_TITLE_FIELD, false, true);
		addField(STRING_DATA_TYPE, RUNTITLE_FIELD, false, true);
		addField(STRING_DATA_TYPE, RUN_OWNER_FIELD, false, true);
		addField(BOOLEAN_DATA_TYPE, RUN_DELETED_FIELD, false, true);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				switch ((int)jsonObject.get(RUN_ACCESS).isNumber().doubleValue()) {
				case 1:
					return "Owner";
				case 2:
					return "Can write";
				case 3:
					return "Can read";

				default:
					break;
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return RUN_ACCESS_STRING;
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
                return "icon_delete";
            }

            @Override
            public int getType() {
                return STRING_DATA_TYPE;
            }

            @Override
            public String getTargetFieldName() {
                return DELETED_ICON;
            }
        }, false, false);
	}

	@Override
	protected void registerForNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler(getNotificationType()	,new NotificationHandler() {
			
			@Override
			public void onNotification(JSONObject bean) {
				processNotification(bean);
			}
		});	
	}
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.run.Run";
	}
	
	@Override
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = super.createRecord(object);
//		if (object.containsKey("game") &&  object.get("game").isObject()!=null) {
//			GameDataSource.getInstance().addJsonObject(object.get("game").isObject());
//		}
		return record;
	}
	
	
}
