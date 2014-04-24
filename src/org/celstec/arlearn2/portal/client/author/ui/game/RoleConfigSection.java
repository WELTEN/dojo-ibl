package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class RoleConfigSection  extends SectionConfig {
	private static GameConstants constants = GWT.create(GameConstants.class);

	private GenericListGrid listGrid;
	private Game currentGame;

	public RoleConfigSection() {
		super("Roles");
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		
		HStack layout = new HStack();
		layout.addMember(getRoleGrid());
		layout.addMember(vSpacer);
		layout.addMember(getAddRoleForm());
		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		
		setItems(layout);
	}
	
	public void loadDataFromRecord(Game game) {
		currentGame = game;
		GameRolesDataSource.getInstance().addRole(game.getGameId(), game.getRoles());
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.GAMEID_FIELD,""+ game.getGameId());
		listGrid.setCriteria(criteria);
//		roleGrid.setValues(game.getRoles());
	}
	
	private Canvas getRoleGrid() {
		listGrid = new GenericListGrid(false, true, false, false, false){
			protected void deleteItem(ListGridRecord rollOverRecord) {
				
				RoleConfigSection.this.deleteRole(rollOverRecord.getAttributeAsString(GameRoleModel.ROLE_FIELD));
			}
		};
		listGrid.setWidth(300);
		listGrid.setShowRollOverCanvas(true);

		listGrid.setAutoFetchData(true);
		
		listGrid.setDataSource(GameRolesDataSource.getInstance());
		ListGridField roleField = new ListGridField(GameRoleModel.ROLE_FIELD, constants.roles());
		listGrid.setFields(new ListGridField[] { roleField });
		return listGrid;
	}
	
	private Canvas getAddRoleForm() {
		final DynamicForm form = new DynamicForm();
		form.setWidth(300);
        form.setGroupTitle(constants.newRole());  
        form.setIsGroup(true);  

        TextItem subjectItem = new TextItem("Role");  
        subjectItem.setTitle(constants.role());  
        
        ButtonItem saveButton = new ButtonItem("Save");
        saveButton.setTitle(constants.saveRole());
        saveButton.setColSpan(2);  
        saveButton.setAlign(Alignment.CENTER);
        
        form.setFields(subjectItem, saveButton);  

        saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (currentGame != null) {
					currentGame.addRole(form.getValueAsString("Role"));
					currentGame.writeToCloud(new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
//							GameRolesDataSource.getInstance().addRole(gameId, role)
						}
						
					});
				}
			}
		});
		return form;
	}
	
	protected void deleteRole(final String role) {
		
		SC.ask(constants.confirmDeleteRole().replace("***", role), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					if (currentGame != null) {
						currentGame.deleteRole(role);
						GameRolesDataSource.getInstance().removeRecord((AbstractRecord) GameRolesDataSource.getInstance().getRecord( currentGame.getGameId()+":"+role)); 
						currentGame.writeToCloud(new JsonCallback());
					}
				}
			}
		});
	}

}
