package org.celstec.arlearn2.portal.client.resultDisplay.ui.view;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;

import com.smartgwt.client.widgets.grid.ColumnTree;

public class Mixed extends ColumnTree {

	private static Mixed instance;

	// TODO Need to be finished. Still in test phase
	private Mixed() {
		super();
		// TODO Auto-generated constructor stub
		setWidth100();  
	    setHeight(205); 
	    setID("boundColumnTreeGrid");	
	    setDataSource(OwnerResponseDataSource.getInstance());  
	    setAutoFetchData(true);  
	    setNodeIcon("audio.png"); 
	    
	    setShowOpenIcons(false);  
	    setShowDropIcons(false);  
		setClosedIconSuffix("");  
	  
		setShowHeaders(true);  
	    setShowNodeCount(true);          
	    setLoadDataOnDemand(false);   
	}

	public static Mixed getInstance() {
		if (instance == null) {
			instance = new Mixed();
		}
		
		return instance;
	}

}
