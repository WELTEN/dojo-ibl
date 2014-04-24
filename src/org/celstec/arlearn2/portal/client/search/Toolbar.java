package org.celstec.arlearn2.portal.client.search;

import com.google.gwt.user.client.ui.HTML;

public class Toolbar extends HTML {
	
	public Toolbar(){
		setHeight("29px");
		getElement().getStyle().setBackgroundColor("#5DA5D7");
		updateToolbar(0);
	}
	
	public void updateToolbar(int nElem){
		if (nElem == 1) {
			setHTML("<h1 class='toolbar'>"+nElem+" game was found</h1>");
		}else{
			setHTML("<h1 class='toolbar'>"+nElem+" games were found</h1>");
		}
	}
}
