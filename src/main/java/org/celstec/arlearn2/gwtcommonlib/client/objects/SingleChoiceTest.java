package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.SingleChoiceExtensionViewer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;

import java.util.LinkedHashMap;

public class SingleChoiceTest extends GeneralItem {
	
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.SingleChoiceTest";
	public  final static String HUMAN_READABLE_NAME = "Single Choice Test";

	public SingleChoiceTest() {
		super();
	}

	public SingleChoiceTest(JSONObject object) {
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
		SingleChoiceExtensionViewer ev = new SingleChoiceExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new SingleChoiceExtensionEditor(this);
	}

	public MultipleChoiceAnswer[] getAnswers() {
		if (jsonRep.containsKey("answers")) {
			JSONArray answerArray = jsonRep.get("answers").isArray();
			MultipleChoiceAnswer[] result = new MultipleChoiceAnswer[answerArray.size()];
			for (int i = 0; i < answerArray.size();i++) {
				result[i] = new MultipleChoiceAnswer(answerArray.get(i).isObject());
			}
			return result;
		}
		return new MultipleChoiceAnswer[0];
	}

	@Override
	public boolean enableDataCollection() {
		return false;
	}

    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        int i = 1;
        for (MultipleChoiceAnswer answer: getAnswers()) {
            sortList.put("Answer "+i++, answer.getString("answer"));
        }

        sortList.putAll(super.getMetadataFields());
        return sortList;
    }
	
}

