package org.celstec.arlearn2.portal.client.author.ui.run;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.ListMasterSectionSectionStackDetailTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.run.i18.RunConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;

public class RunsTab extends ListMasterSectionSectionStackDetailTab {

	private static RunConstants constants = GWT.create(RunConstants.class);

	public static RunsTab instance;
    RunConfigSection runConfigSection;
	TeamPlayerConfigurationSection teamPlayerConfigurationSection;
	TeamConfigurationSection teamConfigurationSection;
	
	public RunsTab() {
		super(constants.runs());
		instance = this;
	}
	
	protected void tabSelect() {
//		hideDetail();
		RunDataSource.getInstance().loadDataFromWeb();
	}
	
	@Override
	protected void initGrid(){
		getMasterListGrid().setDataSource(RunDataSource.getInstance());
		getMasterListGrid().setCanEdit(false);
		
		ListGridField idField = new ListGridField(RunModel.RUNID_FIELD, "id");
		idField.setWidth(60);
		idField.setHidden(true);
		
		ListGridField gameIdField = new ListGridField(RunModel.GAMEID_FIELD, "gameId ");
		gameIdField.setHidden(true);
        gameIdField.setWidth(60);

		ListGridField titleRunField = new ListGridField(RunModel.RUNTITLE_FIELD, constants.runTitle());
		ListGridField titleGameField = new ListGridField(RunModel.GAME_TITLE_FIELD, "Game Title ");
		titleGameField.setCanEdit(false);
		
		ListGridField accessRunField = new ListGridField(RunModel.RUN_ACCESS_STRING, "Run access ");
		accessRunField.setCanEdit(false);
		accessRunField.setWidth(100);
		
		ListGridField deleteField = new ListGridField(RunModel.DELETED_ICON, " ");
        deleteField.setWidth(20);
        deleteField.setAlign(Alignment.CENTER);
        deleteField.setType(ListGridFieldType.IMAGE);
        deleteField.setImageURLSuffix(".png");
        deleteField.setPrompt(constants.delete());

        if (AccountManager.getInstance().isAdministrator()) {
            getMasterListGrid().setCanEdit(true);
            getMasterListGrid().setShowFilterEditor(true);
        }
		getMasterListGrid().setFields(new ListGridField[] { idField, gameIdField, titleRunField,  titleGameField, accessRunField, deleteField });
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		getMasterListGrid().setCriteria(criteria);

        getMasterListGrid().addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
              if (RunModel.DELETED_ICON.equals(getMasterListGrid().getFieldName(event.getColNum()))) {
                    RunsTab.this.deleteItem(event.getRecord());
                }
            }
        });
	}
	
	@Override
	protected void initConfigSections() {
        runConfigSection = new RunConfigSection();
		teamPlayerConfigurationSection = new TeamPlayerConfigurationSection();
		teamConfigurationSection = new TeamConfigurationSection();
        addSectionDetail(runConfigSection);
        addSectionDetail(teamPlayerConfigurationSection);
		addSectionDetail(teamConfigurationSection);
		
	}

	@Override
	protected void masterRecordClick(RecordClickEvent event) {
		if (event.getRecord() == null || event.getRecord().getAttributeAsLong(RunModel.RUNID_FIELD) == null) return;
		Run run = new Run(((AbstractRecord) RunDataSource.getInstance().getRecord(event.getRecord().getAttributeAsLong(RunModel.RUNID_FIELD))).getCorrespondingJsonObject());
        runConfigSection.loadRun(run);
		teamPlayerConfigurationSection.loadRun(run);
		teamConfigurationSection.loadRun(run);
		showDetail();
		
	}

	@Override
	protected void deleteItem(final ListGridRecord rollOverRecord) {
		SC.ask(constants.deleteThisRun().replace("***", rollOverRecord.getAttributeAsString(RunModel.RUNTITLE_FIELD)), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					RunClient.getInstance().deleteItemsForRun(rollOverRecord.getAttributeAsLong("runId"), new JsonCallback() {
						
						@Override
						public void onJsonReceived(JSONValue jsonValue) {
							
							RunDataSource.getInstance().loadDataFromWeb();
		
						}
					});	
					
				}
			}
		});	
	}

	
}
