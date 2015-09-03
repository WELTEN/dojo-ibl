package org.celstec.arlearn2.portal.client.portal;

import org.celstec.arlearn2.gwtcommonlib.client.LocalSettings;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class PortalPage {
	ToolBar toolStrip;
	TileGrid tileGrid;
	
	public void loadPage() {
//		createToolstrip();
		toolStrip = new ToolBar(false);

		
		VLayout vertical = new VLayout();
		vertical.setWidth100();
		vertical.setHeight100();
		vertical.addMember(toolStrip);
		createButtons();
		
		VLayout verticalGrid = new VLayout();
		
		verticalGrid.setLayoutAlign(Alignment.CENTER);
		verticalGrid.setAlign(Alignment.CENTER);  
		verticalGrid.setDefaultLayoutAlign(Alignment.CENTER); // As promised!  

		verticalGrid.setWidth100();
		verticalGrid.setHeight("*");
//		verticalGrid.setBorder("1px solid gray");
		verticalGrid.addMember(tileGrid);
		
		vertical.addMember(verticalGrid);
		RootPanel.get("portal").add(vertical);

	}

	private void createButtons() {
		  tileGrid = new TileGrid();  
	        tileGrid.setTileWidth(250);  
	        tileGrid.setTileHeight(250);  
	        tileGrid.setHeight(400);  
	        tileGrid.setWidth(800);  
	        tileGrid.setID("boundList");  
	        tileGrid.setCanReorderTiles(false);  
	        tileGrid.setShowAllRecords(false);  
	        
	        tileGrid.addRecordClickHandler(new RecordClickHandler() {
				
				@Override
				public void onRecordClick(RecordClickEvent event) {
					switch (event.getRecord().getAttributeAsInt("rec")) {
					case 1:
						Window.open("/author.html"+LocalSettings.getInstance().getLocateExtension(), "_self", ""); 
						break;
					case 2:
						Window.open("/resultDisplayRuns.html", "_self", "");
//						SC.say("feature not yet implemented");
						break;
					case 3:
						Window.open("/search.html", "_self", ""); 
						break;
					default:
						break;
					}
					
				}
			});
//	        tileGrid.setBorder("1px solid gray");
	        
	        
	        Record authoring = new Record();
	        authoring.setAttribute("picture", "author.png");
	        authoring.setAttribute("commonName", "Authoring");
	        authoring.setAttribute("rec", 1);
	        
	        Record results = new Record();
	        results.setAttribute("picture", "resultsTool.png");
	        results.setAttribute("commonName", "Results");
	        results.setAttribute("rec", 2);

	        Record search = new Record();
	        search.setAttribute("picture", "searchTool.png");
	        search.setAttribute("commonName", "Search");
	        search.setAttribute("rec", 3);

	        DataSource ds =new DataSource();
	        ds.setClientOnly(true);
	        ds.addData(authoring);
	        ds.addData(results);
	        ds.addData(search);
	        
	        tileGrid.setDataSource(ds); 
	        tileGrid.setAutoFetchData(true);  

	        DetailViewerField pictureField = new DetailViewerField("picture"); 
			pictureField.setType("image");
			pictureField.setImageHeight(200);
			pictureField.setImageWidth(200);

	        DetailViewerField commonNameField = new DetailViewerField("commonName");  
//	        commonNameField.setCellStyle("commonName");
	        
	  
//	        DetailViewerField lifeSpanField = new DetailViewerField("lifeSpan");  
//	        lifeSpanField.setCellStyle("lifeSpan");  
	        commonNameField.setDetailFormatter(new DetailFormatter() {  
	            public String format(Object value, Record record, DetailViewerField field) {  
	                return "<h1> " + value +"</h1>";  
	            }  
	        });  
	  
	        DetailViewerField statusField = new DetailViewerField("status");  
	        tileGrid.setFields(pictureField, commonNameField);  
	  
	}
}
