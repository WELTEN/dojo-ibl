package org.celstec.arlearn2.portal.client.author.ui.gi;

import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.data.Criteria;

public class BasicMetadataEditorPlus extends BasicMetadataEditor {

	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected CheckboxItem drawOnMap;
	protected CheckboxItem automaticallyLaunch;
	protected CheckboxItem toggleRichtText;

	protected TextItem latText;
	protected TextItem lngText;
    protected TextItem customIcon;
//    protected TextItem section;
    protected TextItem tags;

    protected SelectItem rolesSelectItem;

    private long gameId;

	public BasicMetadataEditorPlus(boolean showTitle, boolean showDescription) {
		super(showTitle, showDescription);
//        this.gameId = gameId;
	}

	protected void createForm() {
		titleText = new TextItem(GeneralItemModel.NAME_FIELD, constants.title());
		titleText.setStartRow(true);
        orderText = new TextItem(GeneralItemModel.SORTKEY_FIELD, constants.order());

		drawOnMap = new CheckboxItem("drawOnMap", constants.drawOnMap());
		drawOnMap.setStartRow(true);
		drawOnMap.setRedrawOnChange(true);
		automaticallyLaunch = new CheckboxItem(GeneralItemModel.AUTO_LAUNCH, constants.automaticallyLaunch());

		latText = new TextItem(GeneralItemModel.LAT_FIELD, constants.latitude());
		latText.setStartRow(true);
		lngText = new TextItem(GeneralItemModel.LNG_FIELD, constants.longitude());
		lngText.setStartRow(true);

		toggleRichtText = new CheckboxItem("toggleRichtText", constants.richTextEditing());
		toggleRichtText.setValue(true);
		toggleRichtText.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				BasicMetadataEditorPlus.this.showRichtText = (Boolean) event.getValue();
				if (showRichtText) {
					richTextEditor.setVisibility(Visibility.INHERIT);
					richTextEditor.setValue(textAreaItem.getValueAsString());
                    form.setHeight(40);

				} else {
					richTextEditor.setVisibility(Visibility.HIDDEN);
					textAreaItem.setValue(richTextEditor.getValue());
                    form.setHeight100();

				}
				form.redraw();
			}
		});

        customIcon = new TextItem(GeneralItemModel.ICON_URL, constants.customIcon());
        customIcon.setStartRow(true);
        customIcon.setColSpan(4);

        section = new TextItem(GeneralItemModel.SECTION, constants.section());
        section.setStartRow(true);
        section.setColSpan(3);

        tags = new TextItem(GeneralItemModel.TAGS, constants.tags());
        tags.setStartRow(true);
        tags.setColSpan(2);

        rolesSelectItem = new SelectItem(GeneralItemModel.ROLES, constants.roles());
        rolesSelectItem.setMultiple(true);
        rolesSelectItem.setMultipleAppearance(MultipleAppearance.PICKLIST);
        rolesSelectItem.setDisplayField(GameRoleModel.ROLE_FIELD);
        rolesSelectItem.setValueField(GameRoleModel.ROLE_FIELD);

//        GameRolesDataSource.getInstance().loadRoles(gameId);

        FormItemIfFunction drawOnMapFunction = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				return (form.getValue("drawOnMap") != null)
					&& form.getValue("drawOnMap").equals(Boolean.TRUE);
			}
		};
		latText.setShowIfCondition(drawOnMapFunction);
		lngText.setShowIfCondition(drawOnMapFunction);


        if (AccountManager.getInstance().isAdvancedUser()) {
		    form.setFields(titleText, orderText, drawOnMap, automaticallyLaunch, latText, toggleRichtText, lngText, rolesSelectItem, customIcon, section,tags, textAreaItem);
        }   else {
            form.setFields(titleText, orderText, drawOnMap, automaticallyLaunch, latText, toggleRichtText, lngText, rolesSelectItem, textAreaItem);
        }
		form.setNumCols(4);
		form.setWidth100();
        form.setHeight(40);

		addMember(form);
	}

	public void saveToBean(GeneralItem gi) {
		gi.getJsonRep().put(GeneralItemModel.SORTKEY_FIELD, new JSONNumber(Integer.parseInt(form.getValueAsString(GeneralItemModel.SORTKEY_FIELD))));
		if (form.getValue("drawOnMap") != null && (Boolean) form.getValue("drawOnMap")) {
			gi.setDouble(GeneralItemModel.LAT_FIELD, Double.parseDouble(form.getValueAsString(GeneralItemModel.LAT_FIELD)));
			gi.setDouble(GeneralItemModel.LNG_FIELD, Double.parseDouble(form.getValueAsString(GeneralItemModel.LNG_FIELD)));
		} else {
			gi.getJsonRep().put(GeneralItemModel.LAT_FIELD, null);
			gi.getJsonRep().put(GeneralItemModel.LNG_FIELD, null);
		}
		if (form.getValue(GeneralItemModel.AUTO_LAUNCH) != null && (Boolean) form.getValue(GeneralItemModel.AUTO_LAUNCH)) {
			gi.setBoolean(GeneralItemModel.AUTO_LAUNCH, true);
		} else {
			gi.setBoolean(GeneralItemModel.AUTO_LAUNCH, false);
		}
        if (form.getValue(GeneralItemModel.ICON_URL) != null &&  !form.getValueAsString(GeneralItemModel.ICON_URL).equals("")) {
            gi.setString(GeneralItemModel.ICON_URL, form.getValueAsString(GeneralItemModel.ICON_URL));
        }
        if (form.getValue(GeneralItemModel.SECTION) != null &&  !form.getValueAsString(GeneralItemModel.SECTION).equals("")) {
            gi.setString(GeneralItemModel.SECTION, form.getValueAsString(GeneralItemModel.SECTION));
        }
        if (form.getValue(GeneralItemModel.TAGS) != null &&  !form.getValueAsString(GeneralItemModel.TAGS).equals("")) {
            gi.setString(GeneralItemModel.TAGS, form.getValueAsString(GeneralItemModel.TAGS));
        }
        gi.setArray(GeneralItemModel.ROLES, rolesSelectItem.getValues());
		super.saveToBean(gi);
	}

	public void loadGeneralItem(GeneralItem gi) {
		super.loadGeneralItem(gi);
        GameRolesDataSource ds = new GameRolesDataSource();
        ds.loadRoles(gi.getLong(GameModel.GAMEID_FIELD));

        rolesSelectItem.setOptionDataSource(ds);


		form.setValue(GeneralItemModel.SORTKEY_FIELD, gi.getInteger(GeneralItemModel.SORTKEY_FIELD));
		if (gi.getDouble(GeneralItemModel.LAT_FIELD) != null)
			form.setValue(GeneralItemModel.LAT_FIELD, gi.getDouble(GeneralItemModel.LAT_FIELD));
		if (gi.getDouble(GeneralItemModel.LNG_FIELD) != null)
			form.setValue(GeneralItemModel.LNG_FIELD, gi.getDouble(GeneralItemModel.LNG_FIELD));
		form.setValue("drawOnMap", gi.getDouble(GeneralItemModel.LNG_FIELD) != null);
		form.setValue(GeneralItemModel.AUTO_LAUNCH, gi.getBoolean(GeneralItemModel.AUTO_LAUNCH));
        form.setValue(GeneralItemModel.ICON_URL, gi.getString(GeneralItemModel.ICON_URL));
        form.setValue(GeneralItemModel.SECTION, gi.getString(GeneralItemModel.SECTION));
        form.setValue(GeneralItemModel.TAGS, gi.getString(GeneralItemModel.TAGS));

        rolesSelectItem.setValues(gi.getValues(GeneralItemModel.ROLES));
	}

	public void coordinatesChanged(LatLng newCoordinates) {
		form.setValue("drawOnMap", true);
		form.setValue(GeneralItemModel.LAT_FIELD, newCoordinates.lat());
		form.setValue(GeneralItemModel.LNG_FIELD, newCoordinates.lng());

	}

}
