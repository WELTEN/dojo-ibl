package org.celstec.arlearn2.gwtcommonlib.client.network.action;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class ActionClient extends GenericClient {

	private static ActionClient instance;
	private ActionClient() {
	}
	
	public static ActionClient getInstance() {
		if (instance == null) instance = new ActionClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "actions";
	}
	
	public void notify(long runId, String email, String itemId, final JsonCallback jsonCallback) {
		invokeJsonPOST("/notify/"+email+"/"+runId+"/"+itemId, "", jsonCallback);
		
	}
	
	public void createAction(long id,long runId,String action,String email, String type, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("generalItemId", new JSONNumber(id));
		object.put("runId", new JSONNumber(runId));
		object.put("generalItemType", new JSONString(type));
		if (email != null) object.put("userEmail", new JSONString(email));
		object.put("action", new JSONString(action));
		invokeJsonPOST(null, object, jcb);
	}

    public void getActions(long runId, long from, String resumptionToken, final JsonCallback jcb) {
        if (resumptionToken == null) {
            invokeJsonGET("/runId/"+runId+"?from="+from, jcb);
        } else {
            invokeJsonGET("/runId/"+runId+"?from="+from +"&resumptionToken="+resumptionToken, jcb);
        }
    }
}
