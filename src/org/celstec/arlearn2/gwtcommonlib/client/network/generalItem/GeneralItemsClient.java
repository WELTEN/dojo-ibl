package org.celstec.arlearn2.gwtcommonlib.client.network.generalItem;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.gwt.json.client.JSONObject;

public class GeneralItemsClient extends GenericClient {
	private static GeneralItemsClient instance;
	private GeneralItemsClient() {
	}
	
	public static GeneralItemsClient getInstance() {
		if (instance == null) instance = new GeneralItemsClient();
		return instance;
	}
	
	public void getGeneralItemsRun(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
	public void getDisappearedGeneralItemsRun(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId+"/disappeared", jcb);
	}
	
	public void getGeneralItemsGame(long gameId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId, jcb);
	}
	
	public void getGeneralItemsGame(long gameId, long from, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId+ "?from="+from, jcb);
	}
	
	public void getGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public void createGeneralItem(JSONObject object, JsonCallback jcb) {
		invokeJsonPOST(null, object, jcb);
	}

    public void createGeneralItem(JSONObject object, long gameId, long itemId, JsonCallback jcb) {
        invokeJsonPOST("/gameId/"+gameId+"/generalItem/"+itemId+"/", object, jcb);
    }
	
	public void createGeneralItem(GeneralItem gi, JsonCallback jcb) {
        if (gi.getJsonRep().containsKey(GameModel.GAMEID_FIELD) && gi.getJsonRep().containsKey(GeneralItemModel.ID_FIELD)) {
            createGeneralItem(gi.getJsonRep(), gi.getLong(GameModel.GAMEID_FIELD), gi.getLong(GeneralItemModel.ID_FIELD), jcb);
        } else {
            createGeneralItem(gi.getJsonRep(), jcb);
        }
	}
	
	public void deleteGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonDELETE("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public void search(String query, JsonCallback jsonCallback) {
		invokeJsonPOST("/search", query, jsonCallback);
	}
	
	public String getUrl() {
		return super.getUrl() + "generalItems";
	}
}

