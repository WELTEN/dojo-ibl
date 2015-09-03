package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes;

import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyTreeGrid;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.tree.TreeNode;

public class TimeDependencyNode extends DependencyTreeGrid{

	public static final int TYPE = 5;
	public static final String TIMEDELTA = "timeDelta";

	public static final String DEP_TYPE = "org.celstec.arlearn2.beans.dependencies.TimeDependency";

	public TimeDependencyNode() {
		 setAttribute("Name", "Time Dependency");
		 setAttribute("typeInt", TYPE); 
        setCanAcceptDrop(true);
        setTitle("Time Dependency");
        
		 TreeNode[] chil = new TreeNode[1];
		 chil[0] = new TreeNode();
		 chil[0].setAttribute("Name", "Time Delta");
		 chil[0].setAttribute("type", TIMEDELTA);
		 setAttribute("children", chil);  
         

	}
	
	public static JSONObject getJson(JSONObject[] childObjects, TreeNode[] childNodes) {
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.TimeDependency"));
		JSONObject offset = null;
		int j = 0;
		for (TreeNode tn: childNodes) {
			if (tn.getAttribute("type")!= null &&tn.getAttribute("type").equals(TIMEDELTA)) {
				long value = Long.parseLong(tn.getAttribute("Value"))*1000;
				if (tn.getAttribute("Value")!=null) dep.put(TIMEDELTA, new JSONNumber(value));
			}
		}
		for (int i =0; i<childObjects.length && j== 0; i++) {
			JSONObject child = childObjects[i];
			if (child != null && child.containsKey("type")) {
				offset = child;	
				j++;
			}
		}
		if (offset == null) return null;
		dep.put("offset", offset);
		return dep;
	}
}
