package org.celstec.arlearn2.gwtcommonlib.client.network;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Team;
import org.celstec.arlearn2.gwtcommonlib.client.objects.User;

public class TeamClient extends GenericClient {

	private static TeamClient instance;
	
	private TeamClient() {
	}
	
	public static TeamClient getInstance() {
		if (instance == null) instance = new TeamClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "team";
	}
	
	public void getTeams(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}

	public void deleteTeam(String teamId, final JsonCallback jcb) {
		invokeJsonDELETE("/teamId/"+teamId, jcb);
	}

	public void createTeam(Team t, JsonCallback jcb) {
		invokeJsonPOST(null, t.getJsonRep(), jcb);		
		
	}
	
}
