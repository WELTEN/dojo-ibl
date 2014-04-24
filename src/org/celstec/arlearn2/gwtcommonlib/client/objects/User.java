package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

public class User extends Account {

	public  final static String RUN_ID = "runId";

	public void setRunId(long runId) {
		setLong(RUN_ID, runId);
	}
	
	
	public void setFullIdentifier(String id) {
		if (!id.contains(":")) return;
		setAccountType(Integer.parseInt(id.substring(0, id.indexOf(":"))));
		setLocalId(id.substring(id.indexOf(":")+1));
	}
	
	public void setRoles(String[] roles) {
		if (roles != null && roles.length != 0) {
			JSONArray ar = new JSONArray();
			getJsonRep().put("roles", ar);
			for (int i = 0; i <roles.length; i++) {
				ar.set(i, new JSONString(roles[i]));
			}
		}
	}
	@Override
	public String getType() {
		return "org.celstec.arlearn2.beans.run.User";
	}


	public void setTeam(String teamId) {
		setString("teamId", teamId);
		
	}
}
