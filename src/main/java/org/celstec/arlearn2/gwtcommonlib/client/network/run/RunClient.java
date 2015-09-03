package org.celstec.arlearn2.gwtcommonlib.client.network.run;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class RunClient extends GenericClient {

	private static RunClient instance;

	
	private RunClient() {
	}
	
	public static RunClient getInstance() {
		if (instance == null) instance = new RunClient();
		return instance;
	}
	
	public void createRun(Long gameId, String title, JsonCallback jcb) {

		JSONObject object = new JSONObject();
		object.put("title", new JSONString(title));
		object.put("gameId", new JSONNumber(gameId));
		invokeJsonPOST(null, object, jcb);		
	}
	
	public void createRun(Run  newRun, JsonCallback jcb) {
		invokeJsonPOST(null, newRun.getJSON(), jcb);		
	}
	
	public void updateRun(long runId, JSONObject run, JsonCallback jcb) {
		invokeJsonPUT("/runId/"+runId, run.toString(), jcb);		

	}
	
	public void runsParticipate(final JsonCallback jcb) {
			invokeJsonGET("/participate", jcb);	
	}
	
	public void runsParticipate(long from, final JsonCallback jcb) {
		invokeJsonGET("/participate?from="+from, jcb);
	}
	
	public void myRuns(final JsonCallback jcb) {
		invokeJsonGET(null, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "myRuns";
	}

	public void getRunAccess(long from, JsonObjectListCallback jcb) {
		invokeJsonGET("/runAccess?from="+from, jcb);
		
	}

	public void getRun(long runId, JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
		
	}

}
