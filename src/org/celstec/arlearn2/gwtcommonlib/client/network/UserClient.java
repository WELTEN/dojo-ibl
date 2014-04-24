package org.celstec.arlearn2.gwtcommonlib.client.network;

import org.celstec.arlearn2.gwtcommonlib.client.objects.User;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class UserClient extends GenericClient {

	private static UserClient instance;
	
	private UserClient() {
	}
	
	public static UserClient getInstance() {
		if (instance == null) instance = new UserClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "users";
	}
	
	public void createUser(User u, JsonCallback jcb) {
		invokeJsonPOST(null, u.getJsonRep(), jcb);		

	}
	
//	public void createUser(Long runId, String teamId, String local, String name, String[] roles, JsonCallback jcb) {
//		JSONObject object = new JSONObject();
//		if (teamId != null) object.put("teamId", new JSONString(teamId));
//		object.put("runId", new JSONNumber(runId));
//		object.put("email", new JSONString(email));
//		if (roles != null && roles.length != 0) {
//			JSONArray ar = new JSONArray();
//			object.put("roles", ar);
//			for (int i = 0; i <roles.length; i++) {
//				ar.set(i, new JSONString(roles[i]));
//			}
//		}
//		invokeJsonPOST(null, object, jcb);		
//	}
	
	public void getUsers(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
	public void deleteUser(Long runId, String email, JsonCallback jcb) {
		invokeJsonDELETE("/runId/"+runId+"/email/"+email, jcb);
	}
	
	
}
