/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwtcommonlib.client.ui.grid;

import org.celstec.arlearn2.gwtcommonlib.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;  
import com.smartgwt.client.widgets.events.ClickHandler;  
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import com.smartgwt.client.types.Alignment;

public class GenericListGrid extends ListGrid {
	
	private HLayout rollOverCanvas = null;
	private boolean editable = false;
	private boolean deleteable = false;
	private boolean downloadable = false;
	private boolean mapable = false;
	private boolean browsable = false;
	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	
	public GenericListGrid(boolean editable, boolean deleteable) {
		this.editable =editable;
		this.deleteable = deleteable;
	}
	
	public GenericListGrid(boolean editable, boolean deleteable, boolean downloadable, boolean mapable, boolean browsable) {
		this.editable =editable;
		this.deleteable = deleteable;
		this.downloadable = downloadable;
		this.mapable = mapable;
		this.browsable = browsable;
	}
	
 	private ListGridRecord rollOverRecord;// = this.getRecord(rowNum);  

	 protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) { 
		 rollOverRecord = this.getRecord(rowNum);

		 if(rollOverCanvas == null) {  
             rollOverCanvas = new HLayout(3);  
             rollOverCanvas.setSnapTo("TR");  
             rollOverCanvas.setWidth(50);  
             rollOverCanvas.setHeight(22);  

			if (editable) {
				ImgButton editImg = new ImgButton();
				editImg.setShowDown(false);
				editImg.setShowRollOver(false);
				editImg.setLayoutAlign(Alignment.CENTER);
				editImg.setSrc("icon_edit.png");
				editImg.setPrompt(constants.editItem());
				editImg.setHeight(16);
				editImg.setWidth(16);
				editImg.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						editItem(rollOverRecord);
					}
				});
	             rollOverCanvas.addMember(editImg);  

			}
			
			if (mapable) {
				
					ImgButton mapImg = new ImgButton();
					mapImg.setShowDown(false);
					mapImg.setShowRollOver(false);
					mapImg.setLayoutAlign(Alignment.CENTER);
					mapImg.setSrc("icon_maps.png");
					mapImg.setPrompt(constants.mapItem());
					mapImg.setHeight(16);
					mapImg.setWidth(16);
					mapImg.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							mapItem(rollOverRecord);

						}

					});

					rollOverCanvas.addMember(mapImg);
				
			}
			if (downloadable) {

	             ImgButton deleteImg = new ImgButton();  
	             deleteImg.setShowDown(false);  
	             deleteImg.setShowRollOver(false);  
	             deleteImg.setLayoutAlign(Alignment.CENTER);  
	             deleteImg.setSrc("icon_down.png");  
	             deleteImg.setPrompt(constants.downloadItem());  
	             deleteImg.setHeight(16);  
	             deleteImg.setWidth(16);  
	             deleteImg.addClickHandler(new ClickHandler() {  
	                 public void onClick(ClickEvent event) {  
	                	 download( rollOverRecord);
	                     
	                 }

	             });  

	             rollOverCanvas.addMember(deleteImg);  
				}
			if (browsable) {

	             ImgButton openImg = new ImgButton();  
	             openImg.setShowDown(false);  
	             openImg.setShowRollOver(false);  
	             openImg.setLayoutAlign(Alignment.CENTER);  
	             openImg.setSrc("icon_magnify.png");  
	             openImg.setPrompt(constants.browseItem());  
	             openImg.setHeight(16);  
	             openImg.setWidth(16);  
	             openImg.addClickHandler(new ClickHandler() {  
	                 public void onClick(ClickEvent event) {  
	                	 browseItem( rollOverRecord);
	                     
	                 }  
	             });  

	             rollOverCanvas.addMember(openImg);  
			}
			
			if (deleteable) {

             ImgButton deleteImg = new ImgButton();  
             deleteImg.setShowDown(false);  
             deleteImg.setShowRollOver(false);  
             deleteImg.setLayoutAlign(Alignment.CENTER);  
             deleteImg.setSrc("icon_delete.png");  
             deleteImg.setPrompt(constants.deleteItem());  
             deleteImg.setHeight(16);  
             deleteImg.setWidth(16);  
             deleteImg.addClickHandler(new ClickHandler() {  
                 public void onClick(ClickEvent event) {  
                	 deleteItem( rollOverRecord);
                     
                 }  
             });  

             rollOverCanvas.addMember(deleteImg);  
			}
			
			
         }  
         return rollOverCanvas;  

     }

	 public void setMapable(boolean mapable){
		 this.mapable = mapable;
	 }
	protected void editItem(ListGridRecord rollOverRecord) {		
	}

	protected void deleteItem(ListGridRecord rollOverRecord) {
	} 
	protected void download(ListGridRecord rollOverRecord) {
	}  
	protected void mapItem(ListGridRecord rollOverRecord) {}  
	
	protected void browseItem(ListGridRecord rollOverRecord) {}  
	

}

