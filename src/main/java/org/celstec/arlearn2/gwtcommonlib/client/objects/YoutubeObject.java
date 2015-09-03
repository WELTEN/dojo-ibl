package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.YoutubeExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

import java.util.LinkedHashMap;

public class YoutubeObject extends NarratorItem {

	public  final static String YOUTUBE_URL = "youtubeUrl";
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.YoutubeObject";
	public  final static String HUMAN_READABLE_NAME = "Youtube Object";
	
	public YoutubeObject() {
		super();
	}

	public YoutubeObject(JSONObject object) {
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
		YoutubeExtensionViewer ev = new YoutubeExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new YoutubeObjectEditor(this);
	}

    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        sortList.put(YOUTUBE_URL, getString(YOUTUBE_URL));
        sortList.putAll(super.getMetadataFields());
        return sortList;
    }
}
