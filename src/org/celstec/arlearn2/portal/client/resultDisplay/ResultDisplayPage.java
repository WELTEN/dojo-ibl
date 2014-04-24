package org.celstec.arlearn2.portal.client.resultDisplay;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OnFinishedInterface;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.account.AccountManager.NotifyAccountLoaded;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.layout.ResultDisplayLayout;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.layout.ResultDisplayLayoutSideBar;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.slideShow.SlideShow;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Grid;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.List;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Mixed;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickHandler;

public class ResultDisplayPage {

	private SlideShow slide;

	
	private Grid tileGrid = null;
	private List listGrid = null;
	private Mixed columnTreeGrid = null;
	private long runId;
	

	
	private static int NUMBER_VERSION = 1;  // Set this parameter to 1 to use first Layout 
											// Set this parameter to 2 to use second Layout

	public void loadPage() {
        String runIdAsString = com.google.gwt.user.client.Window.Location.getParameter("runId");
        if (runIdAsString == null) runIdAsString = "0";
		runId = Long.parseLong(runIdAsString);
		NUMBER_VERSION = (int) Long.parseLong(com.google.gwt.user.client.Window.Location.getParameter("version"));
		
		TeamDataSource.getInstance().loadDataFromWeb(runId);
		final UserDataSource userDataSource = UserDataSource.getInstance();
		userDataSource.setOnFinished(new OnFinishedInterface() {
			@Override
			public void finishedLoadingResults() {
				OwnerResponseDataSource.getInstance().loadDataFromWeb(runId);			
			}
		});
		
		
				
		userDataSource.loadDataFromWeb(runId);
		
		RunClient.getInstance().getItemsForRun(runId, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemDataSource.getInstance().loadDataFromWeb((long) jsonValue.isObject().get("gameId").isNumber().doubleValue());
			}
		});
				
	
		tileGrid = Grid.getInstance();		
		listGrid = List.getInstance();		
		columnTreeGrid = Mixed.getInstance();
		
		tileGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid, event.getRecord());
				}				
			}
		});
		
		listGrid.addRecordDoubleClickHandler(new com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(
					com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent event) {
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid, event.getRecord());
				}
			}
		});

		Layout main = null;
		
		switch (NUMBER_VERSION) {
			case 1:
				main = new ResultDisplayLayout(tileGrid, listGrid, columnTreeGrid);
				break;
			case 2:
				main = new ResultDisplayLayoutSideBar(tileGrid, listGrid, columnTreeGrid);
				break;
			default:
				break;
		}
		
//		main.draw();
        RootPanel.get("result").add(main);
        
        if (Window.Location.getParameter("type") != null)
			Window.Location.replace("/ResultDisplay.html");
    }
	
	private void showPreview(final TileGrid tileGrid, Record record) {
		slide = SlideShow.getInstance(tileGrid, record);
		slide.show();
	}
}
