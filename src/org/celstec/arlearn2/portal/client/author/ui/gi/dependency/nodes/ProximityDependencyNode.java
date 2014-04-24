package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyTreeGrid;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ProximityDependencyNode extends DependencyTreeGrid{

	public static final int TYPE = 4;
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String RADIUS = "radius";
	public static final String DEP_TYPE = "org.celstec.arlearn2.beans.dependencies.ProximityDependency";
	
	public ProximityDependencyNode() {
		 setAttribute("Name", "Proximity Dependency");
		 setAttribute("typeInt", TYPE);

		 setTitle("Proximity");
		 TreeNode[] chil = new TreeNode[3];
		 
		 chil[0] = new TreeNode();
		 chil[0].setAttribute("Name", "radius");
		 chil[0].setAttribute("type", RADIUS);
		 
		 chil[1] = new TreeNode();
		 chil[1].setAttribute("Name", "lat");
		 chil[1].setAttribute("type", LAT);
		 chil[2] = new TreeNode();
		 chil[2].setAttribute("Name", "lng");
		 chil[2].setAttribute("type", LNG);
		 
        setAttribute("children", chil);  
        setCanAcceptDrop(false);
	}
	
	public static JSONObject getJson(TreeNode[] childNodes) {
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString(DEP_TYPE));
		for (TreeNode tn: childNodes) {
			if (tn.getAttribute("type").equals(LAT)) {
				if (tn.getAttribute("Value") != null) dep.put(LAT, new JSONNumber(Double.parseDouble(tn.getAttribute("Value"))));
			}
			if (tn.getAttribute("type").equals(LNG)) {
				if (tn.getAttribute("Value") != null) dep.put(LNG, new JSONNumber(Double.parseDouble(tn.getAttribute("Value"))));
			}
			if (tn.getAttribute("type").equals(RADIUS)) {
				if (tn.getAttribute("Value") != null) dep.put(RADIUS, new JSONNumber(Double.parseDouble(tn.getAttribute("Value"))));
			}
		}
		return dep;
	}
	
}
