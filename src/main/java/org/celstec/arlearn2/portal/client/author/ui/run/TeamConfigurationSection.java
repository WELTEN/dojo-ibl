package org.celstec.arlearn2.portal.client.author.ui.run;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.TeamClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Team;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.run.i18.RunConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class TeamConfigurationSection extends SectionConfig {

	private static RunConstants constants = GWT.create(RunConstants.class);
	private ListGrid teamGrid;
	private DynamicForm teamForm;
	private Run run;
	
	public TeamConfigurationSection() {
		super(constants.team());
		VLayout collabLayout = new VLayout();
		collabLayout.addMember(getAddPlayerForm());
		
		HStack layout = new HStack();
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);

		
		layout.addMember(getTeamGrid());
		layout.addMember(vSpacer);
		layout.addMember(collabLayout);
		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
	}
	
	private Canvas getAddPlayerForm() {
		teamForm = new DynamicForm();
		teamForm.setWidth(300);
		teamForm.setGroupTitle(constants.addTeams());
		teamForm.setIsGroup(true);

		final TextItem teamNameItem = new TextItem("teamName", constants.teamName());
		
		
		
		ButtonItem submitButton = new ButtonItem("Submit");
		submitButton.setTitle(constants.submitTeam());
		submitButton.setColSpan(2);
		submitButton.setAlign(Alignment.CENTER);

		submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				Team t = new Team();
				t.setString("name", teamForm.getValueAsString("teamName"));
				t.setLong("runId", run.getRunId());
				TeamClient.getInstance().createTeam(t, new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						TeamDataSource.getInstance().loadDataFromWeb(run.getRunId());
					}

				});
				
			}
		});
		teamForm.setFields(teamNameItem, submitButton);
		return teamForm;
	}
	private Canvas getTeamGrid() {
		teamGrid= new ListGrid() {
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				return createRecordComponent2(record, colNum,  this.getFieldName(colNum));
			}
		};
		
		
		teamGrid.setCanEdit(false);
		teamGrid.setWidth("30%");
		teamGrid.setShowRollOverCanvas(false);
		teamGrid.setShowRecordComponentsByCell(true);
		teamGrid.setShowRecordComponents(true);

		teamGrid.setAutoFetchData(true);

		teamGrid.setDataSource(TeamDataSource.getInstance());
		
	         
		ListGridField nameField = new ListGridField(TeamModel.NAME_FIELD, constants.name());
		ListGridField deleteField = new ListGridField("deleteField", " ");  
		 deleteField.setWidth(20);
		teamGrid.setFields(new ListGridField[] {nameField, deleteField });
	
		return teamGrid;
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
					SC.ask(constants.deleteTeam(),  constants.confirmDeleteTeam().replace("***", record.getAttribute(TeamModel.NAME_FIELD)), new BooleanCallback() {
						
						@Override
						public void execute(Boolean value) {
							if (value) {
								TeamClient.getInstance().deleteTeam( record.getAttributeAsString(TeamModel.TEAMID_FIELD), null);
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

	
	public void loadRun(Run run) {
		this.run = run;
		Criteria criteria = new Criteria();
		criteria.addCriteria(RunModel.RUNID_FIELD,""+ run.getRunId());
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		
		TeamDataSource.getInstance().loadDataFromWeb(run.getRunId());
		teamGrid.setCriteria(criteria);
		
	}
}
