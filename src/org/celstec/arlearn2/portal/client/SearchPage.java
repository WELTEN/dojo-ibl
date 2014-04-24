package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGameDataSource;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.account.AccountManager.NotifyAccountLoaded;
import org.celstec.arlearn2.portal.client.search.ListSearch;
import org.celstec.arlearn2.portal.client.search.SearchBar;
import org.celstec.arlearn2.portal.client.search.SearchForm;
import org.celstec.arlearn2.portal.client.search.Toolbar;
import org.celstec.arlearn2.portal.client.search.ToolbarFooter;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SearchPage {
	
	private VLayout vLayout;
	ToolBar toolStrip;

	public void loadPage() {
		AccountManager accountManager = AccountManager.getInstance();
		accountManager.setAccountNotification(new NotifyAccountLoaded() {
			
			@Override
			public void accountLoaded(boolean success) {
				if (success) {
					buildUi();
				} else {
					SC.say("Credentials are invalid. Log in again.");
				}
			}
		});
	}
	
	public void buildUi() {
		toolStrip = new ToolBar(false);
		final QueryGameDataSource qgds = new QueryGameDataSource();

		final ListSearch listGrid = new ListSearch();
		listGrid.setUpFieldsListGames(qgds);
		
//		qgds.search("");			
		
		/**
		 * Form to input text search
		 * */
		final SearchForm form = new SearchForm();
		form.getQuery().addKeyDownHandler(new com.smartgwt.client.widgets.form.fields.events.KeyDownHandler() {
			
			@Override
			public void onKeyDown(
					com.smartgwt.client.widgets.form.fields.events.KeyDownEvent event) {
				if (event.getKeyName().equals("Enter")) {
					qgds.search(form.getQuery().getValueAsString());
				}
			}
		});
        
		/**
		 * Toolbar to organize components, at this moment just the counter is set up.
		 * */
		final Toolbar toolbar = new Toolbar();
		listGrid.getListGames().addDataArrivedHandler(new DataArrivedHandler() {
			// TODO inefficient, it should be launched when filtered process were totally finished
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				toolbar.updateToolbar(listGrid.getListGames().getRecordList().getLength());
			}
		});
		
		/**
		 * Toolbar to organize search components. It has an icon, input text, 
		 * search button and clear button.
		 * */
        SearchBar search = new SearchBar();
        search.setItems(form);
        
        // Functionality of search button
		search.getButton().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
    			qgds.search(form.getQuery().getValueAsString());
			}
		});			

        // Functionality of clear button
		search.getClear().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					form.clearValues();
					form.focus();
//					qgds.search(form.getQuery().getValueAsString());

				}
			});
        
		/**
		 * Binding structure of main layout
		 * */
        vLayout = new VLayout();
		vLayout.setWidth100();
		vLayout.setHeight100();
		vLayout.addMember(toolStrip);
		vLayout.addMember(search);
		search.setHeight("20%");
		vLayout.addMember(toolbar);	
		toolbar.setHeight("4%");
		vLayout.addMember(listGrid);
		listGrid.setHeight("65%");

		final ToolbarFooter toolbarFooter = new ToolbarFooter();
//		final Toolbar toolbarFooter = new Toolbar();
		toolbarFooter.setHeight("40px");
		
		vLayout.addMember(toolbarFooter);
		RootPanel.get("search").add(vLayout);
	}

}
