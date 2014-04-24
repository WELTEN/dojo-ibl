package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceImageExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.MultipleChoiceExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class MultipleChoiceImage extends SingleChoiceImage {
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest";
	public  final static String HUMAN_READABLE_NAME = "Multiple Choice Image Test";
	
	public MultipleChoiceImage() {
		super();
	}

	public MultipleChoiceImage(JSONObject object) {
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
		return new MultipleChoiceImageExtensionEditor(this);
	}

}
