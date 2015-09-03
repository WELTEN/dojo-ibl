package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;


import com.smartgwt.client.widgets.tree.TreeNode;

public class DependencyTreeGrid  extends TreeNode {  
    
	public DependencyTreeGrid() {  
      
    }  
	
	public void resetName() {
		setID(""+System.currentTimeMillis());
	}

}
