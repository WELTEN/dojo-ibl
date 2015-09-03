package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.VideoExtensionViewer;

import java.util.LinkedHashMap;

public class VideoObject extends NarratorItem {

	public  final static String VIDEO_FEED = "videoFeed";
    public  final static String MD5_HASH = "md5Hash";

    public  final static String AUTO_PLAY = "autoPlay";
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.VideoObject";
	public  final static String HUMAN_READABLE_NAME = "Video Object";
	public VideoObject() {
		super();
	}

	public VideoObject(JSONObject object) {
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
		VideoExtensionViewer ev = new VideoExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new VideoObjectEditor(this);
	}
    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        sortList.put(VIDEO_FEED, getString(VIDEO_FEED));
        sortList.put(AUTO_PLAY, ""+ getBoolean(AUTO_PLAY));
        sortList.putAll(super.getMetadataFields());
        return sortList;
    }
}
