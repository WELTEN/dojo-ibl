package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

import java.util.LinkedHashMap;

public class NarratorItem extends GeneralItem {
	
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.NarratorItem";

	public NarratorItem() {
		super();
	}

	public NarratorItem(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}
	
	@Override
	public Canvas getViewerComponent() {
		return new Canvas();
	}
	
	public Canvas getMetadataExtensionEditor() {
		return null;
	}

	@Override
	public boolean enableDataCollection() {
		return true;
	}



    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = super.getMetadataFields();
        if (jsonRep.containsKey("openQuestion"))  {
            JSONObject openQuestion = jsonRep.get("openQuestion").isObject();
            sortList.put("dataCollection", "true");
            sortList.put("withAudio", ""+ openQuestion.get("withAudio").isBoolean().booleanValue());
            sortList.put("withText", ""+ openQuestion.get("withText").isBoolean().booleanValue());
            sortList.put("withPicture", ""+ openQuestion.get("withPicture").isBoolean().booleanValue());
            sortList.put("withVideo", ""+ openQuestion.get("withVideo").isBoolean().booleanValue());
        }   else {
            sortList.put("dataCollection", "false");
        }
        return sortList;
    }
	
}
