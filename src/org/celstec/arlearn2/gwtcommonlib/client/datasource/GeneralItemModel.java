package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MozillaOpenBadge;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceImage;

import com.google.gwt.json.client.JSONObject;

public class GeneralItemModel extends DataSourceModel {
	
	
	public static final String ID_FIELD = "id";
	public static final String GENERALITEMID_FIELD = "generalItemId";
	public static final String SORTKEY_FIELD = "sortKey";
	public static final String LAT_FIELD = "lat";
	public static final String LNG_FIELD = "lng";
	public static final String NAME_FIELD = "name";
	public static final String RICH_TEXT_FIELD = "richText";
	public static final String SIMPLE_NAME_FIELD = "simpleName";
	public static final String AUTO_LAUNCH = "autoLaunch";
    public static final String ICON_URL = "iconUrl";
    public static final String SECTION = "section";
    public static final String TAGS = "tags";
    public static final String ROLES = "roles";
    public static final String DELETE_ICON = "deleteIcon";

	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	public final static int VISIBLE = 4;
	public final static int DISAPPEARED = 5;
	
	public GeneralItemModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, ID_FIELD, true, true);
		addField(INTEGER_DATA_TYPE, SORTKEY_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, false, true);
		addField(DOUBLE_DATA_TYPE, LAT_FIELD, false, true);
		addField(DOUBLE_DATA_TYPE, LNG_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, false);
        addField(STRING_DATA_TYPE, ICON_URL, false, false);
        addField(STRING_DATA_TYPE, SECTION, false, false);
        addField(STRING_DATA_TYPE, TAGS, false, false);

		addField(INTEGER_DATA_TYPE, GameModel.GAMEID_FIELD, false, true); 
		addField(STRING_DATA_TYPE, "description", false, false);
		addField(STRING_DATA_TYPE, "richText", false, false);
		addField(STRING_DATA_TYPE, "type", false, false);
		addField(STRING_DATA_TYPE, "account", false, false);
		addField(STRING_DATA_TYPE, "answer", false, false);
		addField(STRING_AR_DATA_TYPE, ROLES, false, false);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addField(BOOLEAN_DATA_TYPE, "read", false, false);
		addField(BOOLEAN_DATA_TYPE, "correct", false, false);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				String firstValue = jsonObject.get("type").isString().stringValue();
				if (firstValue.contains("AudioObject")) return "Audio Object";
				if (firstValue.contains("VideoObject")) return "Video Object";
				if (firstValue.contains("MultipleChoiceTest")) return "Multiple Choice Test";
				if (firstValue.contains("SingleChoiceTest")) return "Single Choice Test";
				if (firstValue.contains("NarratorItem")) return "Narrator Item";
				if (firstValue.contains("OpenUrl")) return "Open URL";
				if (firstValue.contains("YoutubeObject")) return "Youtube movie";
				if (firstValue.contains("OpenBadge")) return "Mozilla Open Badge";
				if (firstValue.contains("ScanTag")) return "Scan Tag";
				if (firstValue.equals(SingleChoiceImage.TYPE)) return SingleChoiceImage.HUMAN_READABLE_NAME;
				if (firstValue.equals(MultipleChoiceImage.TYPE)) return MultipleChoiceImage.HUMAN_READABLE_NAME;
				if (firstValue.equals(MozillaOpenBadge.TYPE)) return MozillaOpenBadge.HUMAN_READABLE_NAME;
				return firstValue;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return SIMPLE_NAME_FIELD;
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
				return (long) jsonObject.get("id").isNumber().doubleValue();
			}

			@Override
			public int getType() {
				return LONG_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return GENERALITEMID_FIELD;
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
                return DELETE_ICON;
            }
        }, false, false);
	}
	
	
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.notification.GeneralItemModification";
	}
//	@Override
//	protected void registerForNotifications() {
//		NotificationSubscriber.getInstance().addNotificationHandler(,new NotificationHandler() {
//			
//			@Override
//			public void onNotification(JSONObject bean) {
//								processNotification(bean);
//
////				switch ((int) bean.get("modificationType").isNumber().doubleValue()) {
////				case DISAPPEARED:
//////					removeObject((long) bean.get("generalItem").isObject().get("id").isNumber().doubleValue());
////					break;
////
////				case CREATED:
////					addJsonObject(bean.get("generalItem").isObject());
////					break;
////					
////				case VISIBLE:
////					addJsonObject(bean.get("generalItem").isObject());
////					break;
////				case DELETED:
////					removeObject((long) bean.get("generalItem").isObject().get("id").isNumber().doubleValue());
////					break;
////				default:
////					break;
////				}
//////				addJsonObject(bean.get("generalItem").isObject());
//			}
//		});	
//	}
}
