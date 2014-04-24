package org.celstec.arlearn2.portal.client.author.ui.run;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ContactsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.gwtcommonlib.client.objects.User;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.run.i18.RunConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.MultiComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class TeamPlayerConfigurationSection extends SectionConfig {
	private static RunConstants constants = GWT.create(RunConstants.class);

	private ListGrid playersGrid;
	private Run run;
	private DynamicForm playerForm;
	private SelectItem roleSelect;
	private SelectItem roleSelectQR;
	private SelectItem teamSelect;
	private SelectItem teamSelectQR;
	
	public TeamPlayerConfigurationSection() {
		super(constants.teamAndUsers());
		VLayout collabLayout = new VLayout();
		collabLayout.addMember(getAddPlayerForm());
		collabLayout.addMember(getQRLoginForm());
		
		HStack layout = new HStack();
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);

		
		layout.addMember(getPlayersGrid());
		layout.addMember(vSpacer);
		layout.addMember(collabLayout);
		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
	}

	protected Canvas createRecordComponent2(final ListGridRecord record, Integer colNum, String fieldName) {

		if (fieldName.equals("deleteField")) {
			ImgButton chartImg = new ImgButton();
			chartImg.setShowDown(false);
			chartImg.setShowRollOver(false);
			chartImg.setAlign(Alignment.CENTER);
			chartImg.setSrc("/images/icon_delete.png");
			chartImg.setPrompt(constants.delete());
			chartImg.setHeight(16);
			chartImg.setWidth(16);
			chartImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SC.ask(constants.deletePlayer(),  constants.confirmDeletePlayer().replace("***", record.getAttribute(ContactModel.NAME_FIELD)), new BooleanCallback() {
						
						@Override
						public void execute(Boolean value) {
							if (value) {
								String accountId = record.getAttribute(ContactModel.ACCOUNT_TYPE_FIELD)+":"+record.getAttribute(ContactModel.LOCAL_ID_FIELD);
								UserClient.getInstance().deleteUser(run.getRunId(), accountId, null);
							}
							
						}
					});
					
				}
			});

			return chartImg;
		} else {
			return null;
		}
	}

	private Canvas getPlayersGrid() {
		playersGrid = new ListGrid() {
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				return createRecordComponent2(record, colNum,  this.getFieldName(colNum));
			}
		};
		
		
		playersGrid.setCanEdit(false);
		playersGrid.setWidth("30%");
		playersGrid.setShowRollOverCanvas(false);
		playersGrid.setShowRecordComponentsByCell(true);
		playersGrid.setShowRecordComponents(true);

		playersGrid.setAutoFetchData(true);

		playersGrid.setDataSource(UserDataSource.getInstance());
		
		 ListGridField pictureField = new ListGridField(UserModel.PICTURE_FIELD, " ", 40);  
		 pictureField.setAlign(Alignment.CENTER);  
		 pictureField.setType(ListGridFieldType.IMAGE);  
	         
		ListGridField nameField = new ListGridField(UserModel.NAME_FIELD, constants.name());
		ListGridField emailField = new ListGridField(UserModel.EMAIL_FIELD, constants.email());
		ListGridField deleteField = new ListGridField("deleteField", " ");  
		 deleteField.setWidth(20);
		playersGrid.setFields(new ListGridField[] {pictureField, nameField, emailField, deleteField });
	
		return playersGrid;
	}
	
	private Canvas getQRLoginForm() {
		final DynamicForm anonymousAccountForm = new DynamicForm();
		anonymousAccountForm.setWidth(300);

		anonymousAccountForm.setGroupTitle(constants.createQRLoginTokens());
		anonymousAccountForm.setIsGroup(true);
		
		TextItem nameTextItem = new TextItem("Name", constants.defineNamePrefix());

		nameTextItem.setWrapTitle(false);
//		nameTextItem.setShowIfCondition(playerMustNotAuthenticate);
		nameTextItem.setStartRow(true);
		
		final TextItem amountTextItem = new TextItem("amount", constants.amount());
		amountTextItem.setValue("1");
		amountTextItem.setWrapTitle(false);
//		amountTextItem.setShowIfCondition(playerMustNotAuthenticate);
		amountTextItem.setStartRow(true);
		IntegerRangeValidator validator = new IntegerRangeValidator();
		validator.setMax(1000);
		validator.setMin(1);
		amountTextItem.setValidators(new IsIntegerValidator(),validator);
		
		roleSelectQR = new SelectItem("roleSelect", "role");
		roleSelectQR.setAllowEmptyValue(true);
		roleSelectQR.setOptionDataSource(GameRolesDataSource.getInstance());
		roleSelectQR.setDisplayField(GameRoleModel.ROLE_FIELD);
		roleSelectQR.setValueField(GameRoleModel.ROLE_FIELD);
		roleSelectQR.setMultiple(true);
		
		
		teamSelectQR = new SelectItem("teamSelect", "team");
		teamSelectQR.setAllowEmptyValue(true);
		teamSelectQR.setOptionDataSource(TeamDataSource.getInstance());
		teamSelectQR.setDisplayField(TeamModel.NAME_FIELD);
		teamSelectQR.setValueField(TeamModel.TEAMID_FIELD);
		
		ButtonItem submitButton = new ButtonItem("Submit");
		submitButton.setTitle(constants.submitPlayers());
		submitButton.setColSpan(2);
		submitButton.setAlign(Alignment.CENTER);

		submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (run == null) return;
				System.out.println("i is "+ anonymousAccountForm.validate());
				if (!anonymousAccountForm.validate()) return;
				Account account = new Account();
				String suffix = "";
				int amount = (Integer)anonymousAccountForm.getValue("amount");
				for (int i = 1; i <= (Integer)anonymousAccountForm.getValue("amount"); i++ ) {
					System.out.println("i is "+ i);
					if (amount > 1) suffix = "-"+i;
					account.setString(UserModel.NAME_FIELD, anonymousAccountForm.getValueAsString("Name")+suffix);
					account.setString(UserModel.EMAIL_FIELD, "---");
					account.setString(UserModel.PICTURE_FIELD, "");
					AccountClient.getInstance().createAnonymousContact(account, new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							JSONObject account = jsonValue.isObject();
							if (account.containsKey("localId")){
								User u = new User();
								u.setRunId(run.getRunId());
								u.setFullIdentifier("0:"+account.get("localId").isString().stringValue());
								u.setRoles(roleSelectQR.getValues());
								if (teamSelectQR.getValue() != null && !teamSelectQR.getValue().equals("")) {
									u.setTeam(teamSelectQR.getValueAsString());
								}
								UserClient.getInstance().createUser(u, new JsonCallback(){
									public void onJsonReceived(JSONValue jsonValue) {
										UserDataSource.getInstance().loadDataFromWeb(run.getRunId());
									}
			
								});
							}
						}
					});
				}
				
				
				
			}
		});
		
		ButtonItem printtags = new ButtonItem("PrintTags", "Print tags");
		printtags.setColSpan(2);
		printtags.setAlign(Alignment.CENTER);

		printtags.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				Window.open("/qrsFor.jsp?runId="+run.getRunId(), "_blank", "");
			}
		});
		
		anonymousAccountForm.setFields( nameTextItem, amountTextItem,  roleSelectQR, teamSelectQR, submitButton, printtags);
		
		return anonymousAccountForm;
	}
	
	private Canvas getAddPlayerForm() {
		playerForm = new DynamicForm();
		playerForm.setWidth(300);
		playerForm.setGroupTitle(constants.addPlayers());
		playerForm.setIsGroup(true);

		final MultiComboBoxItem playersComboBox = new MultiComboBoxItem(ContactModel.LOCAL_ID_FIELD, constants.selectAccounts());
		playersComboBox.setDisplayField(ContactModel.NAME_FIELD);
		playersComboBox.setValueField(ContactModel.ACCOUNT_FIELD);
		playersComboBox.setAutoFetchData(true);
		playersComboBox.setOptionDataSource(ContactsDataSource.getInstance());
//		playersComboBox.setShowTitle(false);
//		playersComboBox.setShowIfCondition(realAccountFunction);


//		CheckboxItem 	qrToggle = new CheckboxItem("qrToggle","Player must authenticate");
//		qrToggle.setValue(true);
//		qrToggle.setRedrawOnChange(true);
		
		roleSelect = new SelectItem("roleSelect", "role");
		roleSelect.setAllowEmptyValue(true);
		roleSelect.setOptionDataSource(GameRolesDataSource.getInstance());
		roleSelect.setDisplayField(GameRoleModel.ROLE_FIELD);
		roleSelect.setValueField(GameRoleModel.ROLE_FIELD);
		roleSelect.setMultiple(true);
		
		
		teamSelect = new SelectItem("teamSelect", "team");
		teamSelect.setAllowEmptyValue(true);
		teamSelect.setOptionDataSource(TeamDataSource.getInstance());
		teamSelect.setDisplayField(TeamModel.NAME_FIELD);
		teamSelect.setValueField(TeamModel.TEAMID_FIELD);
		
		
		
		
		ButtonItem submitButton = new ButtonItem("Submit");
		submitButton.setTitle(constants.submitPlayers());
		submitButton.setColSpan(2);
		submitButton.setAlign(Alignment.CENTER);

		submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (run == null) return;
					for (String account : playersComboBox.getValues()) {
						User u = new User();
						u.setRunId(run.getRunId());
						u.setFullIdentifier(account);
						u.setRoles(roleSelect.getValues());
						if (teamSelect.getValue() != null && !teamSelect.getValue().equals("")) {
							u.setTeam(teamSelect.getValueAsString());
						}
						
						UserClient.getInstance().createUser(u, new JsonCallback(){
							public void onJsonReceived(JSONValue jsonValue) {
								UserDataSource.getInstance().loadDataFromWeb(run.getRunId());
							}
	
						});
					}
					playersComboBox.setValues();
				
			}
		});
		playerForm.setFields(playersComboBox,  roleSelect, teamSelect, submitButton);
		return playerForm;
	}
	
//	FormItemIfFunction realAccountFunction = new FormItemIfFunction() {
//		public boolean execute(FormItem item, Object value, DynamicForm form) {
//			if (form.getValue("qrToggle") == null)
//				return true;
//			return form.getValue("qrToggle").equals(Boolean.TRUE) ;
//		}
//
//	};
//	
//	FormItemIfFunction playerMustNotAuthenticate = new FormItemIfFunction() {
//		public boolean execute(FormItem item, Object value, DynamicForm form) {
//			if (form.getValue("qrToggle") == null)
//				return false;
//			return !form.getValue("qrToggle").equals(Boolean.TRUE) ;
//		}
//
//	};

	public void loadRun(Run run) {
		this.run = run;
		long runId = run.getRunId();
		UserDataSource.getInstance().loadDataFromWeb(runId);
		Criteria criteria = new Criteria();
		criteria.addCriteria(RunModel.RUNID_FIELD,""+ runId);
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		playersGrid.setCriteria(criteria);
		
		Criteria crit = new Criteria();
		crit.addCriteria(GameModel.GAMEID_FIELD,""+ run.getGameId());
		System.out.println("set game id to "+ run.getGameId());
		roleSelect.setPickListCriteria(crit);
		GameRolesDataSource.getInstance().loadRoles(run.getGameId());
//		teamGrid.setCriteria(criteria);
		
	}
}
