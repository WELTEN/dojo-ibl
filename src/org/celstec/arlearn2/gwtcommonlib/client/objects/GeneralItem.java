package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;

import java.util.LinkedHashMap;

public abstract class GeneralItem extends Bean {
	public  final static String ID = "id";

	public GeneralItem(JSONObject json) {
		super(json);
	}
	
	public GeneralItem(){
		super();
	}

	public String getTitle() {
		if (jsonRep.containsKey(GeneralItemModel.NAME_FIELD)) {
			return jsonRep.get(GeneralItemModel.NAME_FIELD).isString().stringValue();
		}
		return "";
	}
	
	public String getRichText() {
		return getString(GeneralItemModel.RICH_TEXT_FIELD);
	}
	
	public void linkToGame(Game game) {
		jsonRep.put(GameModel.GAMEID_FIELD, new JSONNumber(game.getGameId()));
	}

	public String getHumanReadableName() {
		return "general Item";
	}

    public String getDependsOn() {
        if (jsonRep.get("dependsOn") == null) return "";
        return depToHumanReadableString(jsonRep.get("dependsOn").isObject());
    }

    private String depToHumanReadableString(JSONObject json) {
        if (json.containsKey("type") && json.get("type").isString().stringValue().contains("ActionDependency")) {
            return actionDepToHumanReadableString(json);
        }
        if (json.containsKey("type") && json.get("type").isString().stringValue().contains("TimeDependency")) {
            return timeDepToHumanReadableString(json);
        }
        if (json.containsKey("type") && json.get("type").isString().stringValue().contains("AndDependency")) {
            return andDepToHumanReadableString(json);
        }
        if (json.containsKey("type") && json.get("type").isString().stringValue().contains("OrDependency")) {
            return orDepToHumanReadableString(json);
        }
        if (json.containsKey("type") && json.get("type").isString().stringValue().contains("ProximityDependency")) {
            return proximityDepToHumanReadableString(json);
        }
        return "not supported in viewing mode";
    }

    protected String actionDepToHumanReadableString(JSONObject json){
        String returnString = "";
        returnString += "<table border=0 cellspacing=0 class=\"detailBlock\">";
        if (json.containsKey("action")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">action</td><td class=\"detail\" style=\"text-align:left;\">"+json.get("action").isString().stringValue()+"</td><tr>";
        if (json.containsKey("generalItemId")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">generalItemId</td><td class=\"detail\" style=\"text-align:left;\">"+(long)json.get("generalItemId").isNumber().doubleValue()+"</td><tr>";
        if (json.containsKey("scope")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">scope</td><td class=\"detail\" style=\"text-align:left;\">"+(long)json.get("scope").isNumber().doubleValue()+"</td><tr>";
        returnString += "</table>";
        return returnString;
    }

    protected String proximityDepToHumanReadableString(JSONObject json){
        String returnString = "";
        returnString += "<table border=0 cellspacing=0 class=\"detailBlock\">";
        if (json.containsKey("radius")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">radius (m)</td><td class=\"detail\" style=\"text-align:left;\">"+(long)json.get("radius").isNumber().doubleValue()+"</td><tr>";
        if (json.containsKey("lat")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">lat</td><td class=\"detail\" style=\"text-align:left;\">"+json.get("lat").isNumber().doubleValue()+"</td><tr>";
        if (json.containsKey("lng")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">lng</td><td class=\"detail\" style=\"text-align:left;\">"+json.get("lng").isNumber().doubleValue()+"</td><tr>";
        returnString += "</table>";
        return returnString;
    }

    protected String timeDepToHumanReadableString(JSONObject json){
        String returnString = "";
        returnString += "<table border=0 cellspacing=0 class=\"detailBlock\" >";
        if (json.containsKey("timeDelta")) returnString += "<tr><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">timeDelta</td><td class=\"detail\" style=\"text-align:left;\">"+((long)json.get("timeDelta").isNumber().doubleValue()/1000)+"</td><tr>";
        if (json.containsKey("offset")) returnString += "<tr ><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">offset</td><td class=\"detail\" style=\"text-align:left;\">"+depToHumanReadableString(json.get("offset").isObject())+"</td><tr>";
        returnString += "</table>";
        return returnString;
    }

    protected String andDepToHumanReadableString(JSONObject json){
        String returnString = "";
        returnString += "<table border=0 cellspacing=0 class=\"detailBlock\" cellpadding=\"3\" width=\"100%\"><tbody>";
        if (json.containsKey("dependencies")) {
            JSONArray array = json.get("dependencies").isArray();
            for (int i = 0; i <array.size(); i++) {
                 returnString += "<tr ><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">and "+(i+1)+"</td><td class=\"detail\" style=\"text-align:left;\">"+depToHumanReadableString(array.get(i).isObject())+"</td><tr>";

            }
        }
        returnString += "<tbody></table>";
        return returnString;
    }

    protected String orDepToHumanReadableString(JSONObject json){
        String returnString = "";
        returnString += "<table border=0 cellspacing=0 class=\"detailBlock\" cellpadding=\"3\" width=\"100%\"><tbody>";
        if (json.containsKey("dependencies")) {
            JSONArray array = json.get("dependencies").isArray();
            for (int i = 0; i <array.size(); i++) {
                returnString += "<tr ><td width=\"10%\" class=\"detailLabel\" align=\"RIGHT\">or "+(i+1)+"</td><td class=\"detail\" style=\"text-align:left;\">"+depToHumanReadableString(array.get(i).isObject())+"</td><tr>";

            }
        }
        returnString += "<tbody></table>";
        return returnString;
    }

    public static GeneralItem createObject(JSONObject object) {
		String type = object.get("type").isString().stringValue();
		if (type.equals(VideoObject.TYPE)) {
			return new VideoObject(object);
		} else if (type.equals(NarratorItem.TYPE)) {
			return new NarratorItem(object);
		} else if (type.equals(YoutubeObject.TYPE)) {
			return new YoutubeObject(object);
		} else if (type.equals(AudioObject.TYPE)) {
			return new AudioObject(object);
		} else if (type.equals(MultipleChoiceTest.TYPE)) {
			return new MultipleChoiceTest(object);
		} else if (type.equals(SingleChoiceTest.TYPE)) {
			return new SingleChoiceTest(object);
		} else if (type.equals(ScanTagObject.TYPE)) {
			return new ScanTagObject(object);
		} else if (type.equals(MultipleChoiceImage.TYPE)) {
			return new MultipleChoiceImage(object);
		} else if (type.equals(SingleChoiceImage.TYPE)) {
			return new SingleChoiceImage(object);
		} else if (type.equals(MozillaOpenBadge.TYPE)) {
			return new MozillaOpenBadge(object);
		} else if (type.equals (ObjectCollectionDisplay.TYPE)) {
            return new ObjectCollectionDisplay(object);
        }
		return null;
	}

	public abstract Canvas getViewerComponent();

	public abstract Canvas getMetadataExtensionEditor();
	public abstract boolean enableDataCollection();
	
	public void writeToCloud(JsonCallback jsonCallback) {
		GeneralItemsClient.getInstance().createGeneralItem(this, jsonCallback);
	}


    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        return sortList;
    }

    public FileReference[] getFileReferences() {
        if (jsonRep.containsKey("fileReferences")) {
            JSONArray fileReferences = jsonRep.get("fileReferences").isArray();
            FileReference[] result = new FileReference[fileReferences.size()];
            for (int i = 0; i < fileReferences.size();i++) {
                result[i] = new FileReference(fileReferences.get(i).isObject());
            }
            return result;
        }
        return new FileReference[0];    }


}
