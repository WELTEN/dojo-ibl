package org.celstec.arlearn2.portal.client.resultDisplay.ui.layout;

import java.util.Iterator;
import java.util.Map;

import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Grid;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.List;
import org.celstec.arlearn2.portal.client.resultDisplay.ui.view.Mixed;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;


/**
 * Second approach of Result Display. This structure has a bar side in left part and on the other side has 
 * the toolbar and the results area.
 * 
 * @version
 * Left side bar
 * 
 * @author
 * Angel
 * 
 * @see
 * ResultDisplayLayout
 * 
 * */
public class ResultDisplayLayoutSideBar extends HLayout {
	
	private Grid grid = null;
	private List list = null;
	private Mixed mixed = null;
	private final HTML breadcrumb = new HTML();
//	private final Breadcrumb breadcrumb;
	private FilterForm filter = null; 
	
	private VLayout rigthSide = null;
	

	public ResultDisplayLayoutSideBar(Grid tileGrid, List listGrid, Mixed mixedGrid) {
		super();
		setWidth100();  
        setHeight100();
        
        this.grid = tileGrid;
        this.list = listGrid;
        this.mixed = mixedGrid;
        this.filter = new FilterForm();
        filter.setWidth("20%");
        filter.setBackgroundColor("#5DA5D7");
        
        HLayout toolbar = new HLayout(10); 	// Layout for visualization options, clear button and breadcrumb.
        toolbar.setHeight(22);
        toolbar.setPadding(10);
        
      breadcrumb.setHTML("<b>Filters</b>");
//        breadcrumb = new Layout();
//        breadcrumb = new Breadcrumb();
        breadcrumb.setStyleName("breadcrumb-left-side-style");
		breadcrumb.setWidth("80%");
		breadcrumb.setHeight("30px");
		refreshBreadcrumbs(filter);
        
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
        
        rigthSide = new VLayout(); 	// Layout toolbar and results

        rigthSide.addMember(toolbar);
        rigthSide.addMember(tileGrid);
        
        addMember(filter);
		addMember(rigthSide);
		
		filter.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				grid.fetchData(filter.getValuesAsCriteria());
				list.fetchData(filter.getValuesAsCriteria());
				refreshBreadcrumbs(filter);
//				System.out.println(filter.getValues());
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
        toolStrip.setHeight(24);  
  
        ImgButton gridButton = new ImgButton();  
        gridButton.setSize(24);  
        gridButton.setShowRollOver(false);  
        gridButton.setSrc("grid.png");  
        
        gridButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (rigthSide.hasMember(list)) {
					rigthSide.removeMember(list);	
				}
				
				if (rigthSide.hasMember(mixed)) {
					rigthSide.removeMember(mixed);					
				}
				rigthSide.addMember(grid);
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
				if (rigthSide.hasMember(grid)) {
					rigthSide.removeMember(grid);					
				}
				
				if (rigthSide.hasMember(mixed)) {
					rigthSide.removeMember(mixed);					
				}
				rigthSide.addMember(list);
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
				if (rigthSide.hasMember(grid)) {
					rigthSide.removeMember(grid);					
				}
				if (rigthSide.hasMember(list)) {
					rigthSide.removeMember(list);		
				}
				rigthSide.addMember(mixed);
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
		
		
//		if (f.getValue("teamId") != null) {
//			breadcrumb.updateTeamIdLabel(f.getValue("teamId").toString());
//		}
//		
//		if (f.getValue("generalItemId") != null) {
//			breadcrumb.updateGeneralItemLabel(f.getValue("generalItemId").toString());
//		}
//		
//		if (f.getValue("userEmail") != null) {
//			breadcrumb.updateUserEmailLabel(f.getValue("userEmail").toString());	
//		}
//		
//		if (f.getValue("role") != null) {
//			breadcrumb.updateRoleLabel(f.getValue("role").toString());
//		}
		
//			teamId = new Label("Team = "+f.getValue("teamId"));
//			System.out.println("TeamId: "+f.getValue("teamId"));
//
//
//			teamId.addClickHandler(new ClickHandler() {				
//				@Override
//				public void onClick(ClickEvent event) {
//					if (breadcrumb.hasMember(teamId)) {
//						breadcrumb.removeMember(teamId);
//					}
//				}
//			});
//	
//			if (breadcrumb.hasMember(teamId)) {
//				breadcrumb.removeMember(teamId);
//			}
//			
//			breadcrumb.addMember(teamId);
//		}

//		if (f.getValue("generalItem") != null) {
//			
//			generalItem = new Label("General Item = "+f.getValue("generalItem"));
//			System.out.println("GeneralItem: "+f.getValue("generalItemId"));
//
//			generalItem.addClickHandler(new ClickHandler() {				
//				@Override
//				public void onClick(ClickEvent event) {
//					if (breadcrumb.hasMember(generalItem)) {
//						breadcrumb.removeMember(generalItem);
//					}
//				}
//			});
//			
//			breadcrumb.addMember(generalItem);
//		}
//
//		if (f.getValue("userEmail") != null) {
//			
//			userEmail = new Label("User = "+f.getValue("userEmail"));
//			System.out.println("userEmail: "+f.getValue("userEmail"));
//			
//			userEmail.addClickHandler(new ClickHandler() {				
//				@Override
//				public void onClick(ClickEvent event) {
//					if (breadcrumb.hasMember(userEmail)) {
//						breadcrumb.removeMember(userEmail);
//					}
//				}
//			});
//			
//			breadcrumb.addMember(userEmail);
//		}
		
//		Iterator it = f.getValues().entrySet().iterator();
//
//		while(it.hasNext()){
//			Map.Entry pairs = (Map.Entry)it.next();
//			
//			String[] a = {pairs.getKey().toString(),pairs.getValue().toString()};
//			
//			Label b = new Label(a.toString());
//
//			
//			b.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					// TODO Auto-generated method stub
//					String[] a = (String[]) event.getSource();
//					
//					System.out.println(a[0]+" "+a[1]);
//					Criteria c = new Criteria(a[0], a[1]);
//					System.out.println(c.getValues().toString());
//					
//
//				}
//			});
//			
//			breadcrumb.addMember(b);
//		}
//		
//		
		
		
		
//		Widget bCrumb = "<b>Filtered by: </b>";
//		@SuppressWarnings("rawtypes")
//		Iterator it = filterForm.getValues().entrySet().iterator();
//	    while (it.hasNext()) {
//	        @SuppressWarnings("rawtypes")
//			Map.Entry pairs = (Map.Entry)it.next();
//	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
//	        
//	        if (pairs.getValue() != null) {
//    	
////	        	Anchor widget = new Anchor(pairs.getKey()+" = "+pairs.getValue());
//	        	Label widget = new Label(pairs.getKey()+" = "+pairs.getValue());
//	        	widget.addClickHandler(new ClickHandler() {
//					
//					@Override
//					public void onClick(ClickEvent event) {
//						// TODO Auto-generated method stub
//
//						System.out.println("sssssss"+event.getSource());
//					}
//				});
	        	
//	        	widget.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
//					
//					@Override
//					public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//						// TODO Auto-generated method stub
//						//filter.filterData(criteria, callback, requestProperties)
////						ilter.getValuesAsCriteria()
//						System.out.println("sssssss"+event.getRelativeElement());
//					}
//				});
//		        bCrumb widget.getElement();

//		        if (it.hasNext()) {
//			        bCrumb += " >> ";
//				}
//			}
//	        
//	        it.remove(); // avoids a ConcurrentModificationException
//	    }
//	    
//	    breadcrumb.addMember(component);
	}
	
//	public class Breadcrumb extends Layout{
//		private Label teamId;
//		private Label generalItem;
//		private Label role;
//		private Label userEmail;
//		
//		public Breadcrumb() {
//			super();
//			
//	        setStyleName("breadcrumb-left-side-style");
//			setWidth("80%");
//			setHeight("30px");
//			
//			teamId = new Label();
//			userEmail = new Label();
//			role = new Label();
//			generalItem = new Label();
//			
//			teamId.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					// TODO Auto-generated method stub
//					if (breadcrumb.hasMember(teamId)) {
//						breadcrumb.removeMember(teamId);
//					}
//				}
//			});
//			
//			userEmail.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					// TODO Auto-generated method stub
//					if (breadcrumb.hasMember(userEmail)) {
//						breadcrumb.removeMember(userEmail);
//					}
//				}
//			});
//			
//			role.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					// TODO Auto-generated method stub
//					if (breadcrumb.hasMember(role)) {
//						breadcrumb.removeMember(role);
//					}
//				}
//			});
//			
//			generalItem.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					// TODO Auto-generated method stub
//					if (breadcrumb.hasMember(generalItem)) {
//						breadcrumb.removeMember(generalItem);
//					}
//				}
//			});
//			
//			addMember(teamId);
//			addMember(userEmail);
//			addMember(role);
//			addMember(generalItem);
//		}
//		
//		public void updateTeamIdLabel(String teamIdFilter) {
//			teamId.setContents(teamIdFilter);
//			// TODO HERE I NEED TO REFRESH GRID AND LIST
//		}
//		
//		public void updateUserEmailLabel(String emailFilter) {
//			userEmail.setContents(emailFilter);
//			// TODO HERE I NEED TO REFRESH GRID AND LIST
//
//		}
//		
//		public void updateRoleLabel(String roleFilter) {
//			role.setContents(roleFilter);
//			// TODO HERE I NEED TO REFRESH GRID AND LIST
//		}
//		
//		public void updateGeneralItemLabel(String generalItemFilter) {
//			generalItem.setContents(generalItemFilter);
//			// TODO HERE I NEED TO REFRESH GRID AND LIST
//		}
//	}

}
