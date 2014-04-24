package org.celstec.arlearn2.portal.client.search;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGameDataSource;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class ListSearch extends HLayout {

	private ListGrid listGames;
//	private HTML informationArea;
	private DetailViewer informationArea;
	private ListGrid generalItems;
	private HLayout buttons;
	
	public ListSearch() {
		super();
		
		setUpMaster();
		addMember(listGames);
		
		addMember(setUpDetails());
	}

	private VLayout setUpDetails() {
		VLayout lRight = new VLayout();
		lRight.setStyleName("layoutRight");
//		lRight.setPadding(20);
		
		setUpInformationArea();
		lRight.addMember(informationArea);
		
		setUpGeneralItems();
		lRight.addMember(generalItems);
		
//		setUpButtons();
//		lRight.addMember(buttons);
		
		return lRight;
	}

	private void setUpGeneralItems() {
		generalItems = new ListGrid();
		generalItems.setStyleName("listGridGeneralItemsStyle");
		
		ListGridField gameIdField = new ListGridField(GameModel.GAMEID_FIELD, "Game");
		gameIdField.setHidden(true);
		ListGridField nameField = new ListGridField(GeneralItemModel.NAME_FIELD, "Name");
		nameField.setWidth("15%");
		ListGridField descriptionField = new ListGridField("description", "Description");
		descriptionField.setWidth("30%");
		ListGridField longDescriptionField = new ListGridField("richText", "Long Description");
		longDescriptionField.setWidth("50%");
		ListGridField rolesField = new ListGridField("roles", "Roles");
		rolesField.setWidth("5%");
		ListGridField simpleNameField = new ListGridField("simpleName", "Simple Name");
		simpleNameField.setWidth("10%");
		generalItems.setFields(new ListGridField[] { gameIdField, nameField, descriptionField, longDescriptionField, rolesField, simpleNameField });

		generalItems.setID("boundListGridGenealItems");
		generalItems.setHeight("45%");
		generalItems.setDataSource(GeneralItemDataSource.getInstance());
		generalItems.fetchData();
	}
	
	private void setUpButtons() {
		buttons = new HLayout();
		
		Button download = new Button();
		download.setText("Download game");
//		download.setStyleName("button-download");
		buttons.addMember(download);

		Button copy = new Button();
		copy.setText("Copy game into account");
//		copy.setStyleName("button-copy");
		buttons.addMember(copy);
		
		buttons.setHeight("10%");
	}

	private void setUpInformationArea() {
		informationArea = new DetailViewer();  
		informationArea.setHeight("45%");
		informationArea.setTop(250);  
		informationArea.setFields(  
                new DetailViewerField(GameModel.GAME_TITLE_FIELD, "Title"),
                new DetailViewerField(GameModel.GAMEID_FIELD, "Id"),
                new DetailViewerField(GameModel.DELETED_FIELD, "Deleted"),
                new DetailViewerField(GameModel.TIME_FIELD, "Time"));  
  
		informationArea.setEmptyMessage("Click a game in the grid");
	}

	private void setUpMaster() {
		
		listGames = new ListGrid();
		
		listGames.setID("boundListGridSearch");
		listGames.setCanResizeFields(true);
		listGames.setWidth("20%");
		listGames.setHeight100();
		
	}

	public void setUpFieldsListGames(QueryGameDataSource qGame) {
		listGames.setDataSource(qGame);
		listGames.fetchData();	
		ListGridField nameField = new ListGridField(GameModel.GAME_TITLE_FIELD, "Games");
		listGames.setFields(new ListGridField[] { nameField });
		
//		listGames.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				// TODO REVIEW IS NOT CORRECT RIGHT NOW
//				Long gameId = event.getRecord().getAttributeAsLong("gameId");
//				System.out.println("GAME ID:---"+gameId.toString());
//				Criteria c = new Criteria();
//				c.addCriteria(GameModel.GAMEID_FIELD, gameId.toString());
//
//				generalItems.setCriteria(c);
//				GeneralItemDataSource.getInstance().loadDataFromWeb(gameId);
//				
//			}
//		});
		
		listGames.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				// TODO Auto-generated method stub
				informationArea.setData(listGames.getSelectedRecords());
				Long gameId = event.getRecord().getAttributeAsLong("gameId");
				System.out.println("GAME ID:---"+gameId.toString());

				Criteria c = new Criteria();
				c.addCriteria(GameModel.GAMEID_FIELD, gameId.toString());

				generalItems.setCriteria(c);

				GeneralItemDataSource.getInstance().loadDataFromWeb(gameId);
			}
		});
	}

	public ListGrid getListGames() {
		return listGames;
	}

	public void setListGames(ListGrid listGames) {
		this.listGames = listGames;
	}
}
