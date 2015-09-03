package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.*;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.portal.client.author.ui.game.MapConfigSection;

public class Game extends Bean{

	
	public Game(JSONObject json) {
		this.jsonRep = json;
	}
	
	public Game() {
		super();
		JSONObject config = new JSONObject();
		config.put("type", new JSONString("org.celstec.arlearn2.beans.game.Config"));
		config.put("mapAvailable", JSONBoolean.getInstance(false));
		this.jsonRep.put("config", config);
	}
	
	public String getType() {
		return "org.celstec.arlearn2.beans.game.Game";
	}
	
	public JSONObject getJSON() {
		return jsonRep;
	}
	
	public String[] getRoles() {
		String [] roles = null;
		if (jsonRep.containsKey("config")) {
			if (jsonRep.get("config").isObject().containsKey("roles")){
				JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
				roles = new String[array.size()];
				for (int i = 0; i< array.size();i++) {
					roles[i] = array.get(i).isString().stringValue();
				}
			}
		}
		return roles;
	}
	
	public void deleteRole(String role) {
		if (jsonRep.containsKey("config")) {
			if (jsonRep.get("config").isObject().containsKey("roles")){
				JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
				JSONArray newArray = new JSONArray();
				boolean elementFound = false;
				for (int i = 0; i< array.size();i++) {
					if (role.equals(array.get(i).isString().stringValue())) {
						elementFound = true;
					} else 
					if (elementFound) {
						newArray.set(i-1, array.get(i));
					} else {
						newArray.set(i, array.get(i));
					}
				}
				jsonRep.get("config").isObject().put("roles", newArray);
			}
		}
		
	}
	
	public void addRole(String value) {
		if (!jsonRep.containsKey("config")) jsonRep.put("config", new JSONObject());
		if (!jsonRep.get("config").isObject().containsKey("roles")) jsonRep.get("config").isObject().put("roles", new JSONArray());
		JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
		array.set(array.size(), new JSONString(value));
	}
	
	public long getGameId() {
		return (long) jsonRep.get("gameId").isNumber().doubleValue();
	}
	
	public int getSharing() {
		int sharing = 1;
		if (jsonRep.containsKey("sharing")) {
			sharing = (int) jsonRep.get("sharing").isNumber().doubleValue();
		}
		return sharing;
	}

	public void setTitle(String title) {
		jsonRep.put(GameModel.GAME_TITLE_FIELD, new JSONString(title));
	}
	
	public String getDescription() {
		if (jsonRep.containsKey("description")) {
			return jsonRep.get("description").isString().stringValue();
		}
		return "";
	}
	
	public void setDescription(String description) {
		jsonRep.put("description", new JSONString(description));

	}
	
	public void writeToCloud(JsonCallback jsonCallback) {
		GameClient.getInstance().createGame(this, jsonCallback);
	}

	public void setMapAvailable(boolean b) {
		if (!jsonRep.containsKey("config")) {
			jsonRep.put("config", new JSONObject());
		}
		JSONObject config = jsonRep.get("config").isObject();
		config.put(GameModel.MAP_AVAILABLE, JSONBoolean.getInstance(b));
	}

    public void setMessageView(int messageView) {
        if (!jsonRep.containsKey("config")) {
            jsonRep.put("config", new JSONObject());
        }
        JSONObject config = jsonRep.get("config").isObject();
        config.put(GameModel.MESSAGE_VIEWS, new JSONNumber(messageView));
    }
	
	public boolean getMapAvailable() {
		if (!jsonRep.containsKey("config")) {
			return false;
		}
		if (!jsonRep.get("config").isObject().containsKey(GameModel.MAP_AVAILABLE)) { 
			return false;
		}
		return jsonRep.get("config").isObject().get(GameModel.MAP_AVAILABLE).isBoolean().booleanValue();
	}

    public final static int MESSAGE_LIST = 1;
    public final static int MESSAGE_MAP = 2;
    public final static int MAP_VIEW = 3;
    public final static int CUSTOM_HTML = 4;

    public int getMessageViews() {
        if (!jsonRep.containsKey("config")) {
            return MESSAGE_LIST;
        }
        if (!jsonRep.get("config").isObject().containsKey(GameModel.MESSAGE_VIEWS)) {
            return MESSAGE_LIST;
        }
        return (int) jsonRep.get("config").isObject().get(GameModel.MESSAGE_VIEWS).isNumber().doubleValue();
    }


    public void setHtmlMessageList(String html) {
        if (!jsonRep.containsKey("config")) {
            jsonRep.put("config", new JSONObject());
        }
        JSONObject config = jsonRep.get("config").isObject();
        config.put(GameModel.HTML_MESSAGE_LIST, new JSONString(html));
    }

    public String getHtmlMessageList() {
        if (!jsonRep.containsKey("config")) {
            return "";
        }
        if (!jsonRep.get("config").isObject().containsKey(GameModel.HTML_MESSAGE_LIST)) {
            return "";
        }
        return jsonRep.get("config").isObject().get(GameModel.HTML_MESSAGE_LIST).isString().stringValue();
    }
}
