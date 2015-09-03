package org.celstec.arlearn2.portal.client.search;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ToolbarFooter extends HLayout {
	
	private CustomButton download;
	private CustomButton copy;
	
	public ToolbarFooter(){
		setStyleName("toolbar-footer");
		setUpButtons();
		setPadding(0);
		setMargin(0);
//		setRight(0);
		
	}

	private void setUpButtons() {
		// TODO Auto-generated method stub
		download = new CustomButton();
		download.setStyleName("button-download-game gwt-Button");
//		download.getElement().getStyle().setRight(0, Unit.PX);
//		download.setText("Download Game"); 
		
		download.setResource("images/download_game.png");
		download.setText("Download");
		
		addMember(download);
		
		
		copy = new CustomButton();
		copy.setStyleName("button-copy-game  gwt-Button");
//		copy.getElement().getStyle().setMargin(10, Unit.PX);
		copy.setResource("/images/copy_game.png");
		copy.setText("Copy");
		addMember(copy);
	}

	public CustomButton getDownload() {
		return download;
	}

	public void setDownload(CustomButton download) {
		this.download = download;
	}

	public CustomButton getCopy() {
		return copy;
	}

	public void setCopy(CustomButton copy) {
		this.copy = copy;
	}
}
