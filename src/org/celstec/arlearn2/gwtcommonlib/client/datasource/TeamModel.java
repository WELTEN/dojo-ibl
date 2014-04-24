package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;

public class TeamModel extends DataSourceModel {

	public static final String TEAMID_FIELD = "teamId";
	public static final String NAME_FIELD = "name";
	public static final String DELETED_FIELD = "deleted";

	public TeamModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, TEAMID_FIELD, true, true);
		addField(LONG_DATA_TYPE, RunModel.RUNID_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
		addField(BOOLEAN_DATA_TYPE, DELETED_FIELD, false, true);
	}

//	@Override
//	protected void registerForNotifications() {
//		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.TeamModification", new NotificationHandler() {
//
//			@Override
//			public void onNotification(JSONObject bean) {
//				processNotification(bean);
//			}
//		});
//	}
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.notification.TeamModification";
	}

}
