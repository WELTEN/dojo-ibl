package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;

public class MultipleChoiceImageAnswerItem extends Bean {

	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem";
	public  final static String IMAGE_URL = "imageUrl";
    public  final static String IMAGE_MD5 = "imageMd5Hash";
	public  final static String AUDIO_URL = "audioUrl";
    public  final static String AUDIO_MD5 = "audioMd5Hash";
    public  final static String LABEL = "label";
	public  final static String ID = "id";


	public MultipleChoiceImageAnswerItem() {
		super();
	}

	public MultipleChoiceImageAnswerItem(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}

}
