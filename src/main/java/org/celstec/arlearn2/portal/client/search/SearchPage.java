package org.celstec.arlearn2.portal.client.search;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGameDataSource;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SearchPage implements EntryPoint {

	private VLayout vLayout;

	
	@Override
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("search");
			//NotificationSubscriber.getInstance();
			
			final QueryGameDataSource qgds = new QueryGameDataSource();

			final ListSearch listGrid = new ListSearch();
			listGrid.setUpFieldsListGames(qgds);
			
			qgds.search("");			
			
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
						qgds.search(form.getQuery().getValueAsString());

					}
				});
	        
			/**
			 * Binding structure of main layout
			 * */
	        vLayout = new VLayout();
			vLayout.setWidth100();
			vLayout.setHeight100();
			vLayout.addMember(search);
			search.setHeight("20%");
			vLayout.addMember(toolbar);	
			toolbar.setHeight("4%");
			vLayout.addMember(listGrid);
			listGrid.setHeight("65%");

			final ToolbarFooter toolbarFooter = new ToolbarFooter();
//			final Toolbar toolbarFooter = new Toolbar();
			toolbarFooter.setHeight("40px");
			
			vLayout.addMember(toolbarFooter);

			rootPanel.add(vLayout);
		}
	

}
