package org.celstec.arlearn2.portal.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class PortalPage {

	
	public void loadPage() {
		CustomButton create_button = new CustomButton();
		create_button.setText("Create Game");
		create_button.setResource("images/create.png");
		create_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open("/Authoring.html", "_self", ""); 
			}
		});

		CustomButton search_button = new CustomButton();
		search_button.setText("Search Game");
		search_button.setResource("images/search.png");
		search_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open("/search.html", "_self", ""); 
			}
		});

		CustomButton result_button = new CustomButton();
		result_button.setText("Result Display");
		result_button.setResource("images/results.png");
		result_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open("/ResultDisplay.html", "_self", ""); 
			}
		});

		RootPanel layout_button_create = RootPanel.get("button-create");
		RootPanel layout_button_search = RootPanel.get("button-search");
		RootPanel layout_button_result = RootPanel.get("button-result");

		layout_button_create.add(create_button);
		layout_button_search.add(search_button);
		layout_button_result.add(result_button);
		if (Window.Location.getParameter("type") != null)
			Window.Location.replace("/portal.html");
	}
}
