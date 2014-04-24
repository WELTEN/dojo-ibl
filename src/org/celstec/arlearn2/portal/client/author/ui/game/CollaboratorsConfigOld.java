package org.celstec.arlearn2.portal.client.author.ui.game;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ContactsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.MultiComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class CollaboratorsConfigOld extends SectionConfig {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	// SelectItem roleGrid;
	private GenericListGrid listGrid;
	private Game currentGame;

	public CollaboratorsConfigOld() {
		super("Add Collaborators");
		HStack layout = new HStack();


		layout.addMember(getInviteForm());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		layout.addMember(vSpacer);

		layout.addMember(canWriteForm());
		layout.addMember(vSpacer);
		layout.addMember(canReadForm());

		ContactsDataSource.getInstance().loadDataFromWeb();

		setItems(layout);
	}

	private Canvas getInviteForm() {

		final DynamicForm form = new DynamicForm();
		form.setGroupTitle("Invite new contact");
		form.setIsGroup(true);
		form.setWidth(300);

		TextItem subjectItem = new TextItem("Contact");
		subjectItem.setTitle("Contact Email");

		ButtonItem saveButton = new ButtonItem("Save");
		saveButton.setTitle("Invite");
		saveButton.setColSpan(2);
		saveButton.setAlign(Alignment.CENTER);

		form.setFields(subjectItem, saveButton);

		saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				CollaborationClient.getInstance().addContactViaEmail(form.getValueAsString("Contact"), new JsonCallback() {
				});
				form.setValue("Contact", "");
			}
		});

		return form;
	}

	private DynamicForm writeForm;
	private MultiComboBoxItem canWrite;
	private MultiComboBoxItem canRead;
	private DynamicForm readForm;

	private Canvas canWriteForm() {
		writeForm = new DynamicForm();
		canWrite = new MultiComboBoxItem("canwrite");
		canWrite.setDisplayField(ContactModel.NAME_FIELD);
		canWrite.setValueField(ContactModel.ACCOUNT_FIELD);
		canWrite.setAutoFetchData(true);
		canWrite.setOptionDataSource(ContactsDataSource.getInstance());
		canWrite.setShowTitle(false);
		canWrite.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String oldValue = "" + event.getOldValue();
				oldValue =removeTrailingComma(oldValue);
				StringTokenizer st = new StringTokenizer(oldValue, ",");
				ArrayList<String> oldValues = new ArrayList<String>();

				while (st.hasMoreTokens()) {
					oldValues.add(st.nextToken());
				}
				
				String newValue = "" + event.getValue();
				newValue =removeTrailingComma(newValue);
				st = new StringTokenizer(newValue, ",");
				ArrayList<String> newValues = new ArrayList<String>();

				while (st.hasMoreTokens()) {
					newValues.add(st.nextToken());
				}
				System.out.println(oldValues);
				System.out.println(newValues);
				
				if (newValues.size()>oldValues.size()) {
					for (String removeString:oldValues) {
						newValues.remove(removeString);
					}
					System.out.println("to add" +newValues);
					for (String addValue: newValues) {

//					canRead.setValues(removeValue(canWrite.getValues(), addValue).toArray(new String[0]));
					
					GameClient.getInstance().addAccess(currentGame.getGameId(), addValue, 2, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
					}
				}
				if (newValues.size()<oldValues.size()) {
					for (String removeString:newValues) {
						oldValues.remove(removeString);
					}
					System.out.println("to rem" +oldValues);
					for (String removeValue: oldValues) {
					GameClient.getInstance().removeAccess(currentGame.getGameId(), removeValue, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
					}
				}
			}
		});

		writeForm.setFields(canWrite);
		writeForm.setWidth(240);
		writeForm.setGroupTitle("Can write");
		writeForm.setIsGroup(true);
		return writeForm;
	}
	
	private String removeTrailingComma(String oldValue) {
		oldValue = oldValue.trim();
		if (oldValue.endsWith(",")){
			oldValue = oldValue.substring(0, oldValue.length()-1);
		}
		return oldValue;
	}

	private Canvas canReadForm() {
		readForm = new DynamicForm();
		canRead = new MultiComboBoxItem(ContactModel.LOCAL_ID_FIELD);
		canRead.setDisplayField(ContactModel.NAME_FIELD);
		canRead.setValueField(ContactModel.ACCOUNT_FIELD);
		canRead.setAutoFetchData(true);
		canRead.setOptionDataSource(ContactsDataSource.getInstance());
		canRead.setShowTitle(false);

		canRead.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String oldValue = "" + event.getOldValue();
				oldValue =removeTrailingComma(oldValue);

				String newValue = "" + event.getValue();
				newValue =removeTrailingComma(newValue);

				if (oldValue.equals("")) {
					canWrite.setValues(removeValue(canWrite.getValues(), newValue).toArray(new String[0]));
					GameClient.getInstance().addAccess(currentGame.getGameId(), newValue, 3, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							loadGameAccess();
						};
					});
				}
				if (newValue.equals("")) {
					GameClient.getInstance().removeAccess(currentGame.getGameId(), oldValue, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							loadGameAccess();
						};
					});
				}
			}
		});

		readForm.setFields(canRead);
		readForm.setWidth(240);
		readForm.setGroupTitle("Can read");
		readForm.setIsGroup(true);
		return readForm;
	}

	private ArrayList<String> removeValue(String[] string, String value) {
		if (string.length == 0)
			return new ArrayList<String>();
		ArrayList<String> returnString = new ArrayList<String>();

		for (String itValue : string) {
			if (!itValue.equals(value)) {
				returnString.add(itValue);
			}
		}
		return returnString;
	}

	public void loadDataFromRecord(Game game) {
		canWrite.setValues();
		canRead.setValues();
		

		currentGame = game;
		loadGameAccess();
	}
	
	private void loadGameAccess() {
		GameClient.getInstance().getGamesAccessAccount(currentGame.getGameId(), new JsonObjectListCallback("gamesAccess", null) {
			public void onJsonObjectReceived(JSONObject jsonObject) {
				int accessRight = (int) jsonObject.get("accessRights").isNumber().doubleValue();
				String account = jsonObject.get("account").isString().stringValue();
				switch (accessRight) {
				case 2:
					canWrite.setValues(account, canWrite.getValues());
					break;
				case 3:
					canRead.setValues(account, canRead.getValues());
					break;
				default:
					break;
				}
			}
		});
	}

	public void hideDetail() {
		// TODO Auto-generated method stub
		
	}
}
