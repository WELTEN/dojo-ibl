package org.celstec.arlearn2.portal.client.resultDisplay.ui.view;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;

import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class Grid extends TileGrid {
	
	private static Grid instance;
	
	public static Grid getInstance(){
		if (instance == null) {
			instance = new Grid();
		}
		
		return instance;
	}

	private Grid() {
		super();
		
		setTileWidth(158);
		setTileHeight(158);
		setHeight100();
		setID("boundList");
		//setBackgroundColor("#f1f1f1");
		setCanReorderTiles(true);
		setShowAllRecords(true);
		setDataSource(OwnerResponseDataSource.getInstance());
		setAutoFetchData(true);
		setAnimateTileChange(true);
		setCanFocus(false);	
		
		/**
		 * Fields tilegrid setup
		 * */		
		// TODO to solve thumbnail problem take a look to the ResponseDataSource
		DetailViewerField pictureField = new DetailViewerField("picture");
		pictureField.setType("image");
		pictureField.setImageSize(100);
		
		
		setFields(pictureField);
		
	}

	
	
	
	
	
}
