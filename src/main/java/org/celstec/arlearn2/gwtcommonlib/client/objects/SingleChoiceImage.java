package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceImageExtensionEditor;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class SingleChoiceImage extends GeneralItem {
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest";
	public  final static String HUMAN_READABLE_NAME = "Single Choice Image Test";
	
	public SingleChoiceImage() {
		super();
	}

	public SingleChoiceImage(JSONObject object) {
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
		return new SingleChoiceImageExtensionEditor(this);
	}
	
	public MultipleChoiceImageAnswerItem[] getAnswers() {
		if (jsonRep.containsKey("answers")) {
			JSONArray answerArray = jsonRep.get("answers").isArray();
			MultipleChoiceImageAnswerItem[] result = new MultipleChoiceImageAnswerItem[answerArray.size()];
			for (int i = 0; i < answerArray.size();i++) {
				result[i] = new MultipleChoiceImageAnswerItem(answerArray.get(i).isObject());
			}
			return result;
		}
		return new MultipleChoiceImageAnswerItem[0];
	}

	@Override
	public boolean enableDataCollection() {
		return false;
	}
}
