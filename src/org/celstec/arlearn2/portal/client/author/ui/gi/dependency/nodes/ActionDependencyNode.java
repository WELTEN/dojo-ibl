package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyTreeGrid;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.tree.TreeNode;


public class ActionDependencyNode extends DependencyTreeGrid{
	
	public static final int TYPE = 2;
	public static final String DEP_TYPE = "org.celstec.arlearn2.beans.dependencies.ActionDependency";
	public static final String ACTION = "action";
	public static final String GENERALITEM = "generalItem";
	public static final String ROLE = "role";
	public static final String SCOPE = "scope";

	public ActionDependencyNode() {
		 setAttribute("Name", "Action Dependency "+System.currentTimeMillis());
		 setAttribute("typeInt", TYPE);

		 setTitle();
		 TreeNode[] chil = new TreeNode[4];
		 
		 chil[0] = new TreeNode();
		 chil[0].setAttribute("Name", "Action");
		 chil[0].setAttribute("type", ACTION);
		 
		 chil[1] = new TreeNode();
		 chil[1].setAttribute("Name", "item");
		 chil[1].setAttribute("type", GENERALITEM);
		 chil[2] = new TreeNode("scope");
		 chil[2].setAttribute("Name", "scope");
		 chil[2].setAttribute("type", SCOPE);
		 chil[3] = new TreeNode("role");
		 chil[3].setAttribute("Name", "role");
		 chil[3].setAttribute("type", ROLE);
         setAttribute("children", chil);  
         setCanAcceptDrop(false);
         
         
	}
	
	public void setTitle() {
		super.setTitle("Action Dependency");
	}

	public static JSONObject getJson(TreeNode[] childNodes) {
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString(DEP_TYPE));
		for (TreeNode tn: childNodes) {
			if (tn.getAttribute("type").equals(ACTION)) {
				if (tn.getAttribute("Value")!=null) dep.put("action", new JSONString(tn.getAttribute("Value")));
			}
			if (tn.getAttribute("type").equals(GENERALITEM)) {
				if (tn.getAttribute("Value")!=null) dep.put("generalItemId", new JSONNumber(Long.parseLong(tn.getAttribute("Value"))));
			}
			if (tn.getAttribute("type").equals(SCOPE)) {
				if (tn.getAttribute("Value")!=null) dep.put("scope", new JSONNumber(Long.parseLong(tn.getAttribute("Value"))));
			}
			if (tn.getAttribute("type").equals(ROLE)) {
				if (tn.getAttribute("Value")!=null) if (tn.getAttribute("Value") != null) dep.put("role", new JSONString(tn.getAttribute("Value")));
			}
		}
		return dep;
	}

}
