package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MozillaOpenBadgeExtensionEditor;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class MozillaOpenBadge extends GeneralItem {
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.OpenBadge";
	public  final static String HUMAN_READABLE_NAME = "Mozilla Open Badge";
	public  final static String BADGE_URL = "badgeUrl";
	public  final static String IMAGE = "image";
	
	public MozillaOpenBadge() {
		super();
	}

	public MozillaOpenBadge(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}
	
	public String getHumanReadableName() {
		return HUMAN_READABLE_NAME;
	}
	
	@Override
	public Canvas getViewerComponent() { //TODO
//		MultipleChoiceExtensionViewer ev = new MultipleChoiceExtensionViewer();
//		ev.loadGeneralItem(this);
		return new Canvas();
	}
	
	public Canvas getMetadataExtensionEditor() { //TODO
		return new MozillaOpenBadgeExtensionEditor(this);
	}

	@Override
	public boolean enableDataCollection() {
		return false;
	}
}
