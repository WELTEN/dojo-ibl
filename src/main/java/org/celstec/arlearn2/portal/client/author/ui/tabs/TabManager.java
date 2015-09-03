package org.celstec.arlearn2.portal.client.author.ui.tabs;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;

public class TabManager {
	
	public static TabManager instance;

	public static TabManager getInstance() {
		if (instance == null)
			instance = new TabManager();
		return instance;
	}
	
	private TabSet topTabSet;
	private HashMap<String, String> tabHashMap = new HashMap<String, String>();
	/**
	 * 
	 */
	private Tab gamesTab;
	private Tab runsTab;
	private HashMap<Long, GeneralItemsTab> giTabs = new HashMap<Long, GeneralItemsTab>();
	
	private VLayout drawableWidget;
	
	private TabManager() {
		topTabSet = new TabSet();
		topTabSet.setSize("100%", "100%");
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				String toDelete = "";
				for (Map.Entry<String, String> entry: tabHashMap.entrySet()) {
					if (entry.getValue().equals(event.getTab().getID())) toDelete =entry.getKey();
				}
				tabHashMap.remove(toDelete);
				if (event.getTab() instanceof GeneralItemsTab) {
					giTabs.remove(((GeneralItemsTab) event.getTab()).getGame().getGameId());
				}
			}
		});
		topTabSet.addTabDeselectedHandler(new TabDeselectedHandler() {
			
			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (event.getTab() instanceof GeneralItemsTab) {
					((GeneralItemsTab) event.getTab()).hideDetail();
				}
				
			}
		});
		
	}
	
	public Widget getDrawableWidget() {
		drawableWidget = new VLayout();  
		drawableWidget.setMembersMargin(15); 
               
		drawableWidget.addMember(topTabSet);  
		drawableWidget.setWidth100();
		drawableWidget.setHeight100();
//		drawableWidget.setSize("828", "600");
		return drawableWidget;
	}

	public void addTab(GamesTab gamesTab) {
		this.gamesTab = gamesTab;
        topTabSet.addTab(gamesTab);  

	}
	
	public void addTab(RunsTab runsTab) {
		this.runsTab = runsTab;
        topTabSet.addTab(runsTab);  

	}

	public void addTab(GeneralItemsTab giTab) {
		
		if (giTabs.containsKey(giTab.getGame().getGameId())) {
//			giTabs.get(giTab.getGame().getGameId()).
			topTabSet.selectTab(giTabs.get(giTab.getGame().getGameId()));
		} else {
			topTabSet.addTab(giTab);
			topTabSet.selectTab(giTab);
			giTabs.put(giTab.getGame().getGameId(), giTab);	
		}
		
	}
}
