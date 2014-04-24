package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ScanTagEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.ScanTagViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class ScanTagObject extends GeneralItem {
	
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.ScanTag";
	public  final static String HUMAN_READABLE_NAME = "Scan Tag";
	public  final static String AUTOLAUNCHQRREADER = "autoLaunchQrReader";

	public ScanTagObject() {
		super();
	}

	public ScanTagObject(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}
	
	public String getHumanReadableName() {
		return HUMAN_READABLE_NAME;
	}
	
	@Override
	public Canvas getViewerComponent() {
		ScanTagViewer ev = new ScanTagViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new ScanTagEditor(this);
	}

	@Override
	public boolean enableDataCollection() {
		return false;
	}
}
