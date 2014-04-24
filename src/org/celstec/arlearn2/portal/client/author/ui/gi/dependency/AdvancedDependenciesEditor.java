package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.ActionForm;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.ProximityDependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.TimeDepEditor;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class AdvancedDependenciesEditor extends HLayout {

	private DependencyGrid depTreeGrid;
	private FromDependenciesGrid fromDep = new FromDependenciesGrid();
	
	private ActionForm actionForm;
	private long gameId;
	
	private ProximityDependencyEditor proxDep;
	
	private TimeDepEditor timeEditor = new TimeDepEditor() {
		public void onSave() {
			super.onSave();
			depTreeGrid.update();
		}
	};
	
	public AdvancedDependenciesEditor(long gameId, Tab mapTab) {
		this.gameId= gameId;
		actionForm = new ActionForm(true, gameId){
			public void onSave() {
				super.onSave();
				depTreeGrid.update();
			}
		};
		proxDep = new ProximityDependencyEditor(mapTab) {
			public void onSave() {
				super.onSave();
				if (depTreeGrid != null) depTreeGrid.update();
			}
		};
		depTreeGrid = new DependencyGrid(actionForm, proxDep, timeEditor);
		addMember(fromDep);
		// addMember(moveControls);
		addMember(depTreeGrid);
		addMember(actionForm);
		addMember(proxDep);
		addMember(timeEditor);
		setHeight("*");
//		setBorder("2px solid blue");
		
	}


	public JSONObject getJson() {
		try {
			return depTreeGrid.getJson();
		} finally {
			proxDep.hideProximityDependencyNode();
		}
	}


	public void loadJson(JSONObject object) {
		depTreeGrid.loadJson(object);
	}


	public void deselect() {
		proxDep.removeMarkers();
		
	}

	
}
