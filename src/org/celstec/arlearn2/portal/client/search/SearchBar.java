package org.celstec.arlearn2.portal.client.search;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.layout.HLayout;

public class SearchBar extends HLayout {
	private Image icon;
	private SearchForm form;
	private Button button;
	private ImgButton clear;
	
	
	public SearchBar(){
		setBackgroundColor("#F1F1F1");
		setHeight(60);
		
		icon = new Image("images/icon_arlearn.png");

        button = new Button();
        button.setText("Search");
        button.getElement().getStyle().setProperty("position", "relative");
        
        clear = new ImgButton();  
        clear.setSrc("icon_delete.png");
        clear.setPosition(Positioning.ABSOLUTE);
        clear.setTop("11px");
        clear.setWidth(16);
        clear.setHeight(16);
	}
	
	public void setItems(SearchForm _form) {
        addMember(icon);
       	addMember(_form);
        addMember(button);
       	addMember(clear);
	}

	public Image getIcon() {
		return icon;
	}
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	public SearchForm getForm() {
		return form;
	}
	public void setForm(SearchForm form) {
		this.form = form;
	}
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}
	public ImgButton getClear() {
		return clear;
	}
	public void setClear(ImgButton clear) {
		this.clear = clear;
	}
}
