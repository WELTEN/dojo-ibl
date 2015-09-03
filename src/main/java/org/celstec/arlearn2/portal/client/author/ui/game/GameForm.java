package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.portal.client.AuthoringConstants;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;

@Deprecated
public class GameForm {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	private DynamicForm basicMetadata;
	private HLayout smallConfigCanvas;

	public GameForm() {
		initBasicMetadata();
		
		smallConfigCanvas= new HLayout();
//		smallConfigCanvas.setBorder("1px solid gray");
		smallConfigCanvas.setHeight(100);
		smallConfigCanvas.addMember(getBasicMetadataCanvas());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(5);
		smallConfigCanvas.addMember(vSpacer);
		smallConfigCanvas.addMember(getRolesCanvas());
		smallConfigCanvas.addMember(vSpacer);
		smallConfigCanvas.addMember(getSharingConfig());
		smallConfigCanvas.addMember(vSpacer);
		smallConfigCanvas.addMember(getConfigCanvas());
	}

	private void initBasicMetadata() {
		
		basicMetadata = new DynamicForm();
		
		TextItem textItem = new TextItem(GameModel.GAME_TITLE_FIELD);
		textItem.setTitle("Title");

		basicMetadata.setFields(textItem);
		
		

	}
	
	public Canvas getDrawableWidget() {
		return smallConfigCanvas;
	}

	public void loadDataFromRecord(Record record) {
		
		basicMetadata.setValue(GameModel.GAME_TITLE_FIELD, record.getAttribute(GameModel.GAME_TITLE_FIELD));
		// TODO Auto-generated method stub
		
	}
	
	
	private Canvas getRolesCanvas() {
		// Canvas canvas = new Canvas();
		VStack layout = new VStack();
		layout.setBorder("1px solid gray");
		layout.setHeight(150);
		Label title = new Label(
				"<span style=\"font-size:125%; font-weight: bold;\">"+ constants.roles() + "</span>");
		title.setHeight(15);
		layout.setWidth(200);
		layout.addMember(title);

		DynamicForm form = new DynamicForm();
		form.setHeight("*");
		SelectItem roleGrid = new SelectItem();
		roleGrid.setTitle("Select Multiple (Grid)");

		roleGrid.setMultiple(true);
		roleGrid.setHeight(100);
		roleGrid.setShowTitle(false);
		roleGrid.setMultipleAppearance(MultipleAppearance.GRID);
		form.setFields(roleGrid);
		layout.addMember(form);

		IButton button = new IButton(constants.newRole());
		button.setHeight(20);
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				(new RoleWindow(gameId, GameTab.this)).show();
				// ListGridRecord lgr = new ListGridRecord();
				// lgr.setAttribute("id", gameId);
				// lgr.setAttribute("pk", ""+gameId+System.currentTimeMillis());
				// listGrid.startEditingNew(lgr);
			}
		});
		layout.addMember(button);
		// layout.setBorder("2px solid blue");
		// canvas.setHeight("*");

		return layout;
	}
	
	private Canvas getSharingConfig() {
		VStack layout = new VStack();
		layout.setBorder("1px solid gray");
		layout.setHeight(150);
		layout.setWidth(200);

		Label title = new Label(
				"<span style=\"font-size:125%; font-weight: bold;\">share</span>");
		title.setHeight(15);
		layout.addMember(title);

//		layout.addMember(configForm.getSharingConfiguration());
		return layout;
	}
	
	private Canvas getConfigCanvas() {
		VStack layout = new VStack();
		layout.setBorder("1px solid gray");
		layout.setHeight(150);
		Label title = new Label(
				"<span style=\"font-size:125%; font-weight: bold;\">i18 - map"+
						 "</span>");
		title.setHeight(15);
		layout.setWidth(200);

		layout.addMember(title);

//		layout.addMember(configForm.getSimpleConfiguration());
		return layout;
	}
	
	private Canvas getBasicMetadataCanvas() {
		VStack layout = new VStack();
		layout.setBorder("1px solid gray");
		layout.setHeight(150);
		Label title = new Label(
				"<span style=\"font-size:125%; font-weight: bold;\">"
						+ constants.config() + "</span>");
		title.setHeight(15);
		layout.setWidth(200);

		layout.addMember(title);

		layout.addMember(basicMetadata);
		return layout;
	}
	

}
