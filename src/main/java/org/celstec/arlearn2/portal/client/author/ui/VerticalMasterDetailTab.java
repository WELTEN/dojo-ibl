package org.celstec.arlearn2.portal.client.author.ui;

import org.celstec.arlearn2.portal.client.i18.PortalConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public abstract class VerticalMasterDetailTab extends Tab {
	private PortalConstants constants = GWT.create(PortalConstants.class);

	private VLayout layout;
	
	public VerticalMasterDetailTab(String name) {
		super(name);
		layout = new VLayout(8);
		layout.setHeight100();
		layout.setCanDragResize(true);
		
		layout.addMember(getMasterCanvas());
		layout.addMember(getDetailCanvas());

		this.setPane(layout);

		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabSelect();
			}
		});
	}
	
	private Canvas getMasterCanvas(){
		Canvas masterCanvas = getMaster();
		masterCanvas.setCanDragResize(true);
		masterCanvas.setShowResizeBar(true);
		return masterCanvas;
	}
	
	private Canvas getDetailCanvas() {
		Canvas detailCanvas = getDetail();
		detailCanvas.setCanDragResize(true);
		return detailCanvas;
	}
	
	
	public abstract Canvas getMaster() ;
	public abstract Canvas getDetail() ;
	
	
	protected void tabSelect() {		
	}
	
	protected ImgButton createDeleteImg() {
		ImgButton deleteImg = new ImgButton();
		deleteImg.setShowDown(false);
		deleteImg.setShowRollOver(false);
		deleteImg.setAlign(Alignment.CENTER);
		deleteImg.setSrc("/images/icon_delete.png");
		deleteImg.setPrompt(constants.delete());
		deleteImg.setHeight(16);
		deleteImg.setWidth(16);
		return deleteImg;
	}

}
