package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.ProximityDependencyEditor;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class TestPage {

	public void loadPage2() {

		final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor(0l, null);
//		final DependencyEditor depEdit = new DependencyEditor();
		RootPanel.get("test").add(depEdit);
		IButton button = new IButton();
		GeneralItemDataSource.getInstance().loadDataFromWeb(1l);
		GameDataSource.getInstance().loadDataFromWeb();
		GameClient.getInstance().getGame(1, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				Game game = new Game(jsonValue.isObject());
				GameRolesDataSource.getInstance().addRole(game.getGameId(), game.getRoles());
			}

		});

		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				System.out.println("click" + depEdit.getJson());

			}
		});
		RootPanel.get("test").add(button);

	}
	private VLayout layout1;
	private VLayout layout2;
	
	public void loadPage() {

		TabSet topTabSet = new TabSet();
		final Tab tab1 = new Tab();
		final Tab tab2 = new Tab();
		topTabSet.addTab(tab1);
		topTabSet.addTab(tab2);
		topTabSet.setWidth100();
		topTabSet.setHeight100();
		

		
//		layout.setWidth100();
//		final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor(0l);
//		final MapWidget widget1 = MapWidget.getInstance();
//		widget1.setHeight("200px");
//		layout1.addMember(widget1);
//		layout1.removeMember(widget1);
		tab1.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				layout1 = new VLayout();
				tab1.setPane(layout1);

				System.out.println("adding tab");
//				RootPanel.detachNow(widget2);
//				widget1.removeFromParent();
				layout1.addMember(MapWidget.getInstance());
				
			}
		});
		tab1.addTabDeselectedHandler(new TabDeselectedHandler() {
			
			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (layout1 != null) layout1.removeMembers(layout1.getMembers());
				
			}
		});

		
		tab2.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				layout2 = new VLayout();
//				layout.setWidth100();
//				final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor(0l);
//				final MapWidget widget2 = MapWidget.getInstance();
				tab2.setPane(layout2);
//				widget1.removeFromParent();
				layout2.addMember(MapWidget.getInstance());
				
			}
		});

		tab2.addTabDeselectedHandler(new TabDeselectedHandler() {
			
			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (layout2 != null) layout2.removeMembers(layout1.getMembers());
				
			}
		});

		
//		RootPanel.get("test").add(edit);
//		layout.addMember(widget1);
//		layout.addMember(widget2);
		RootPanel.get("test").add(topTabSet);

	}
}
