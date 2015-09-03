package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GeneralItemsListMapTabSet extends TabSet{

	private ListGrid generalItemsGrid;
	private Game game;
	private Tab mapTab;
	private Tab listTab;
	private boolean mapSelected = false;
	GeneralItemMapView mapLayout;// = new GeneralItemMapView();
	private LatLng mapCenter;
	private GeneralItemDetail giDetail;
	private Double zoom;
	private HLayout masterLayout;
	
	public GeneralItemsListMapTabSet(Game game) {
		this.game = game;
		setWidth("*");
		setTabBarPosition(Side.LEFT);
		buildUi();
	}
	
	private void buildUi() {
		Tab listTab = new Tab();
		listTab.setIcon("/images/list_icon.png", 16);
		listTab.setPane(generalItemsGrid);
		listTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mapSelected = false;
			}
		});

		mapTab = new Tab();
		mapTab.setIcon("/images/icon_maps.png", 16);

		mapTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				rebuildMapBecauseTabSelected();
				mapSelected = true;
			}
		});
		mapTab.addTabDeselectedHandler(new TabDeselectedHandler() {

			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (mapLayout != null) removeMapItemsBecauseDeselected();
				if (giDetail != null) giDetail.deselect();
			}
		});

		addTab(listTab);
		addTab(mapTab);
	}
	
	private void rebuildMapBecauseTabSelected() {
		mapLayout = new GeneralItemMapView(game) {
			public void loadItemDetail(GeneralItem gi){
				giDetail.deselect();
				giDetail.viewGeneralItem(gi, true, mapTab);
			}
			public void loadNewCoordinates(LatLng newCoordinates){
				giDetail.loadCoordinates(newCoordinates);
			}

		};
		mapTab.setPane(mapLayout);
		mapLayout.createMap(mapCenter, zoom);
	}
	
	private void removeMapItemsBecauseDeselected() {
		mapLayout.removeMarkers();
		mapCenter = mapLayout.getMapWidget().getMap().getCenter();
		zoom = mapLayout.getMapWidget().getMap().getZoom();
	}
	
//	protected void tabSelect() {
//		game = new Game(((AbstractRecord) GameDataSource.getInstance().getRecord(game.getGameId())).getCorrespondingJsonObject());
//
//		masterLayout.removeMembers(masterLayout.getMembers());
//		if (game.getMapAvailable()) {
//			masterLayout.addMember(mapListTabSet);
//		} else {
//			masterLayout.addMember(generalItemsGrid);
//		}
//		masterLayout.addMember(createReuseItemsPane);
//	}
}
