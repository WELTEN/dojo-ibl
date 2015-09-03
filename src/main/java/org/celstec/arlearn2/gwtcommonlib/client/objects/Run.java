package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

public class Run {

	JSONObject jsonRep;

	public Run(JSONObject json) {
		this.jsonRep = json;
	}
	
	public Run() {
		jsonRep = new JSONObject();
	}
	
	public JSONObject getJSON() {
		return jsonRep;
	}
	
	public void setTitle(String title) {
		jsonRep.put(RunModel.RUNTITLE_FIELD, new JSONString(title));
	}

	public void setGameId(Long gameId) {
		jsonRep.put(RunModel.GAMEID_FIELD, new JSONNumber(gameId));
		
	}
	
	public long getRunId() {
		return (long) jsonRep.get(RunModel.RUNID_FIELD).isNumber().doubleValue();
	}
	
	public long getGameId() {
		return (long) jsonRep.get(GameModel.GAMEID_FIELD).isNumber().doubleValue();
	}

    public String getTitle() {
        return jsonRep.get(RunModel.RUNTITLE_FIELD).isString().stringValue();
    }

    public void writeToCloud(JsonCallback jsonCallback) {
        RunClient.getInstance().updateRun(getRunId(),getJSON(), jsonCallback);
    }
}
