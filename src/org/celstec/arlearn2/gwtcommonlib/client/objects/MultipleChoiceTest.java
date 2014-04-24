package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.MultipleChoiceExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class MultipleChoiceTest extends SingleChoiceTest {
	
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest";
	public  final static String HUMAN_READABLE_NAME = "Multiple Choice Test";
	
	public MultipleChoiceTest() {
		super();
	}

	public MultipleChoiceTest(JSONObject object) {
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
		MultipleChoiceExtensionViewer ev = new MultipleChoiceExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new MultipleChoiceExtensionEditor(this);
	}
}
