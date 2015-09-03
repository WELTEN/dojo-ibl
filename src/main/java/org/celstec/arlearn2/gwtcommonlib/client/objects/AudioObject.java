package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.AudioExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.AudioExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

import java.util.LinkedHashMap;

public class AudioObject extends NarratorItem {

	public  final static String AUDIO_FEED = "audioFeed";
    public  final static String MD5_HASH = "md5Hash";
    public  final static String AUTO_PLAY = "autoPlay";
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.AudioObject";
	public  final static String HUMAN_READABLE_NAME = "Audio Object";
	public AudioObject() {
		super();
	}

	public AudioObject(JSONObject object) {
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
		AudioExtensionViewer ev = new AudioExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new AudioExtensionEditor(this);
	}

    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        sortList.put(AUDIO_FEED, getString(AUDIO_FEED));
        sortList.put(AUTO_PLAY, ""+ getBoolean(AUTO_PLAY));
        sortList.putAll(super.getMetadataFields());
        return sortList;
    }
}
