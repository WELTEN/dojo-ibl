package org.celstec.arlearn2.portal.client.author.ui.game;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;

import java.util.LinkedHashMap;

public class SharingConfigSection extends SectionConfig {
	private final static String CCBY = "Attribution CC BY";
	private static GameConstants constants = GWT.create(GameConstants.class);
	RadioGroupItem sharingOptions;
	RadioGroupItem licenseOptions;
	DynamicForm sharingForm;
	DynamicForm licenseForm;

	Game game;

	public SharingConfigSection() {
		super(constants.share());
		HStack layout = new HStack();

		layout.addMember(getAccessForm());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		layout.addMember(vSpacer);
		layout.addMember(getLicenseForm());

		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
	}

	private Canvas getAccessForm() {
		sharingOptions = new RadioGroupItem();
		sharingOptions.setName("sharing");
		sharingOptions.setTitle(constants.shareVisibilityOptions());
		sharingOptions.setValueMap(new String[] { constants.privateSharing(),
				constants.linkSharing(), constants.publicSharing() });
		sharingOptions.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				int sharingType = 1;

				if (event.getValue() != null) {
					if (event.getValue().equals(constants.linkSharing())) {
						sharingType = 2;
					}
					if (event.getValue().equals(constants.publicSharing())) {
						sharingType = 3;
						licenseForm.setVisibility(Visibility.INHERIT);
						if (game.getString(GameModel.LICENSE_CODE).equals("")) {
							licenseOptions.setValue("cc-by");
							game.setString(GameModel.LICENSE_CODE, "cc-by");
						}
					} else {
						licenseForm.setVisibility(Visibility.HIDDEN);
						game.setString(GameModel.LICENSE_CODE, "");

					}

					game.setLong(GameModel.SHARING_FIELD, sharingType);
					game.writeToCloud(new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {

						}

					});
				}

			}
		});
		sharingForm = new DynamicForm();
		sharingForm.setGroupTitle("Access");
		sharingForm.setIsGroup(true);
		sharingForm.setWidth(300);

		sharingForm.setFields(sharingOptions);
		return sharingForm;
	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		int sharing = game.getInteger(GameModel.SHARING_FIELD);
		setSharing(sharing);
		String licenseCode = game.getString(GameModel.LICENSE_CODE);
		if (licenseCode != null) {
			licenseOptions.setValue(licenseCode);
		}
	}

	public void setSharing(int sharing) {
		if (sharingOptions != null) {
			switch (sharing) {
			case 1:
				sharingOptions.setValue(constants.privateSharing());
				licenseForm.setVisibility(Visibility.HIDDEN);
				break;
			case 2:
				sharingOptions.setValue(constants.linkSharing());
				licenseForm.setVisibility(Visibility.HIDDEN);
				break;
			case 3:
				sharingOptions.setValue(constants.publicSharing());
				licenseForm.setVisibility(Visibility.INHERIT);

				break;

			default:
				break;
			}
			sharingForm.redraw();
		}
	}

	private Canvas getLicenseForm() {
		LinkedHashMap<String, String> licenseMap = new LinkedHashMap<String, String>();
		licenseMap.put("cc-by", CCBY);
		licenseMap.put("cc-by-nd", "Attribution-NoDerivs BY-ND");
		licenseMap.put("cc-by-sa", "Attribution-ShareAlike BY-SA");
		licenseMap.put("cc-by-nc", "Attribution-NonCommercial BY-NC");
		licenseMap.put("cc-by-nc-sa", "Attribution-NonCommercial-ShareAlike BY-NC-SA");
		licenseMap.put("cc-by-nc-nd", "Attribution-NonCommercial-NoDerivs BY-NC-ND");
		
		licenseOptions = new RadioGroupItem();
		licenseOptions.setName("license");
		licenseOptions.setTitle("choose creative commons license");
		licenseOptions.setValueMap(licenseMap);
		


		// "Attribution-NonCommercial CC BY-NC",
		// "Attribution-NonCommercial-NoDerivs CC BY-NC-ND"
		// });
		licenseOptions.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				
				game.setString(GameModel.LICENSE_CODE, (String) event.getValue());
				game.writeToCloud(new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {

					}

				});
				
			}
		});
		
		licenseForm = new DynamicForm();
		licenseForm.setGroupTitle("License");
		licenseForm.setIsGroup(true);
		licenseForm.setFields();
		licenseForm.setWidth(500);
		licenseForm.setVisibility(Visibility.HIDDEN);
		licenseForm.setFields(licenseOptions);
		
		return licenseForm;
	}

//	public void hideDetail() {
//		mapTypeForm.setVisibility(Visibility.HIDDEN);
//		messageViewTypeForm.setVisibility(Visibility.HIDDEN);
//		
//	}
}
