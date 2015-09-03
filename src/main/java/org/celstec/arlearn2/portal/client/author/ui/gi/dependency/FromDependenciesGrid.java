package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.ActionDependencyNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.AndDependencyTreeNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.OrDependencyTreeNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.ProximityDependencyNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.TimeDependencyNode;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.DragCompleteEvent;
import com.smartgwt.client.widgets.events.DragCompleteHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

public class FromDependenciesGrid extends TreeGrid {

	OrDependencyTreeNode orDep = new OrDependencyTreeNode();
	AndDependencyTreeNode andDep =  new AndDependencyTreeNode();
	ActionDependencyNode actiondep = new ActionDependencyNode();
	TimeDependencyNode timeDep = new TimeDependencyNode();
	ProximityDependencyNode proxDep = new ProximityDependencyNode();
	
	public FromDependenciesGrid() {
		setWidth(200);
		// setHeight("*");
		setShowEdges(true);
		setBorder("0px");
		setBodyStyleName("normal");
		setShowHeader(false);
		setLeaveScrollbarGap(false);
		setManyItemsImage("cubes_all.png");
		setAppImgDir("pieces/16/");
		setCanReorderRecords(true);
		setCanAcceptDroppedRecords(false);
		setCanDragRecordsOut(true);
		setDragDataAction(DragDataAction.COPY);
		setData();

		addNodeClickHandler(new NodeClickHandler() {

			@Override
			public void onNodeClick(NodeClickEvent event) {
				System.out.println("click node" + event.getNode().getClass());
				System.out.println("click node" + event.getRecordNum());
				event.getNode().setAttribute("Name", ""+System.currentTimeMillis());

			}
		});
		addDragCompleteHandler(new DragCompleteHandler() {
			
			@Override
			public void onDragComplete(DragCompleteEvent event) {
				System.out.println("drag complete");
				
				
			}
		});
		addDragStartHandler(new DragStartHandler() {
			
			@Override
			public void onDragStart(DragStartEvent event) {
				orDep.setAttribute("Name", ""+System.currentTimeMillis());
				andDep.setAttribute("Name", ""+System.currentTimeMillis());
				actiondep.setAttribute("Name", ""+System.currentTimeMillis());
				timeDep.setAttribute("Name", ""+System.currentTimeMillis());
				proxDep.setAttribute("Name", ""+System.currentTimeMillis());
			}
		});
	}

	public void setData() {
		Tree grid1Tree = new Tree();
		grid1Tree.setModelType(TreeModelType.CHILDREN);
		grid1Tree.setNameProperty("Name");
//		grid1Tree.setRoot(new TreeNode("Root", new ActionDependencyNode(), new ProximityDependencyNode(), new OrDependencyTreeNode(), new AndDependencyTreeNode()));
		grid1Tree.setRoot(new TreeNode("Root", orDep, andDep, actiondep, timeDep, proxDep));

		setData(grid1Tree);
		getData().openAll();
	}

}
