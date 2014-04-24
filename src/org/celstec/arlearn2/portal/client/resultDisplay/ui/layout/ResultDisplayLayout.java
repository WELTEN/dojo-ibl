package org.celstec.arlearn2.portal.client.resultDisplay.ui.layout;

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.Window;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Grid;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.List;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Mixed;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * First approach of Result Display. This approach uses all width of the screen to show data. Filter and 
 * other functionality is in the upper part of the screen.
 * 
 * @version
 * Using width of screen.
 * 
 * @author
 * Angel
 * 
 * @see
 * ResultDisplayLayoutSideBar
 * 
 * */
public class ResultDisplayLayout extends VLayout {
	
	private Grid grid = null;
	private List list = null;
	private Mixed mixed = null;
	private final HTML breadcrumb = new HTML();
	private FilterForm filter = null; 
	

	public ResultDisplayLayout(Grid tileGrid, List listGrid, Mixed mixedGrid) {
		super();
		setWidth100();  
        setHeight100();
        
        this.grid = tileGrid;
        this.list = listGrid;
        this.mixed = mixedGrid;
        this.filter = new FilterForm();
        filter.setNumCols(8);
        
        HLayout toolbar = new HLayout(10); 	// Layout for visualization options, clear button and breadcrumb.
        toolbar.setHeight(22);
        toolbar.setPadding(20);
        toolbar.setBackgroundColor("#5DA5D7");
        
        breadcrumb.setHTML("<b>Filters</b>");
        breadcrumb.setStyleName("breadcrumb-style");
		breadcrumb.setWidth("80%");
		breadcrumb.setHeight("30px");
		refreshBreadcrumbs(filter);

//        IButton homeButton = createClearButton("Home");
//        homeButton.setIcon("home.png");
//
//        homeButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                Window.open("/index.html", "_self", "");
//            }
//        });
//
//        toolbar.addMember(homeButton);

        toolbar.addMember(createVisualizationToolbar());
        
        IButton clearButton = createClearButton("Reset filter");
        
        clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.fetchData();
				list.fetchData();
				filter.clearValues();
				refreshBreadcrumbs(filter);
			}
		});
        
        toolbar.addMember(clearButton);
        toolbar.addMember(breadcrumb);
        
        addMember(filter);
		addMember(toolbar);
		addMember(tileGrid);
		
		filter.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				grid.fetchData(filter.getValuesAsCriteria());
				list.fetchData(filter.getValuesAsCriteria());
				refreshBreadcrumbs(filter);
				System.out.println(filter.getValues());
			}
		});
        
	}

	private IButton createClearButton(String name) {
		IButton clearButton = new IButton(name);
		clearButton.setAutoFit(true);
		clearButton.setWidth(150);
		
		return clearButton;
	}
	
	private ToolStrip createVisualizationToolbar() {
		/**
		 * Toolstrip menu for visualization
		 * */		
		ToolStrip toolStrip = new ToolStrip(); 
        toolStrip.setWidth(72);  
        toolStrip.setHeight(22);
        toolStrip.setTop(17);
        toolStrip.setPadding(2);

        ImgButton homeButton = new ImgButton();
        homeButton.setSize(24);
        homeButton.setShowRollOver(false);
        homeButton.setSrc("home.png");

        homeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open("/index.html", "_self", "");
            }
        });
        toolStrip.addMember(homeButton);

        toolStrip.addSpacer(15);

        ImgButton gridButton = new ImgButton();  
        gridButton.setSize(24);  
        gridButton.setShowRollOver(false);  
        gridButton.setSrc("grid.png");
        
        gridButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (hasMember(list)) {
					removeMember(list);	
				}
				
				if (hasMember(mixed)) {
					removeMember(mixed);					
				}
        		addMember(grid);
			}
		});
        

        
        toolStrip.addMember(gridButton);  
          
        ImgButton listButton = new ImgButton();  
        listButton.setSize(24);  
        listButton.setShowRollOver(false);  
        listButton.setSrc("list.png"); 
        toolStrip.addMember(listButton); 
        listButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (hasMember(grid)) {
					removeMember(grid);					
				}
				
				if (hasMember(mixed)) {
					removeMember(mixed);					
				}
        		addMember(list);
			}
		});
          
        ImgButton mixedButton = new ImgButton();  
        mixedButton.setSize(24);  
        mixedButton.setShowRollOver(false);  
        mixedButton.setSrc("mixed.png");  
        toolStrip.addMember(mixedButton);
        mixedButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (hasMember(grid)) {
					removeMember(grid);					
				}
				if (hasMember(list)) {
					removeMember(list);		
				}
				addMember(mixed);
			}
		});
		return toolStrip;
	}
	
	private void refreshBreadcrumbs(final DynamicForm filterForm) {
		String bCrumb = "<b>Filtered by: ";
		
		@SuppressWarnings("rawtypes")
		Iterator it = filterForm.getValues().entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        
	        
	        if (pairs.getValue() != null) {
	        	bCrumb += " "+pairs.getKey();
		        bCrumb += " = "+pairs.getValue();
		        if (it.hasNext()) {
			        bCrumb += " >> ";
				}
			}
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    breadcrumb.setHTML(bCrumb+"</b>");
	}

}
