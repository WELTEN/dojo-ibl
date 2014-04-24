/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.delegators;

import java.util.UUID;
import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.notification.TeamModification;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.jdo.manager.TeamManager;
import org.celstec.arlearn2.tasks.beans.*;
import org.celstec.arlearn2.cache.TeamsCache;
import org.celstec.arlearn2.cache.UsersCache;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;

public class TeamsDelegator extends GoogleDelegator {

	private static final Logger logger = Logger.getLogger(TeamsDelegator.class.getName());

	public TeamsDelegator(String authToken) {
		super(authToken);
	}
	
	public TeamsDelegator(GoogleDelegator gd) {
		super(gd);
	}

    public TeamsDelegator(Service service) {
        super(service);
    }

	public Team createTeam(Team team) {
		RunDelegator rd = new RunDelegator(this);
		if (team.getRunId() == null) {
			team.setError("No run identifier specified");
			return team;
		}
		if (rd.getRun(team.getRunId())== null) {
			team.setError("No run with given id exists");
			return team;
		}
		return createTeam(team.getRunId(), team.getTeamId(), team.getName());
	}
	
	public Team createTeam(long runId, String teamId, String name) {
		if (teamId == null) teamId = UUID.randomUUID().toString();
		TeamsCache.getInstance().removeTeams(runId);
		new NotifyUsersFromGame(getAuthToken(), runId, null, TeamModification.ALTERED).scheduleTask();
		
		TeamModification tm = new TeamModification();
		tm.setModificationType(TeamModification.ALTERED);
		tm.setRunId(runId);
		UsersDelegator ud = new UsersDelegator(this);
		
		ChannelNotificator.getInstance().notify(ud.getCurrentUserAccount(), tm);

        (new UpdateVariableInstancesForTeam(authToken, this.account, teamId, runId, null, 1)).scheduleTask();
        (new UpdateVariableEffectInstancesForTeam(authToken, this.account, teamId, runId, null, 1)).scheduleTask();


        return TeamManager.addTeam(runId, teamId, name);
	}

	
	
	public TeamList getTeams(Long runId) {
		TeamList tl = TeamsCache.getInstance().getTeamList(runId);
		if (tl != null)
			return tl;
		tl = TeamManager.getTeams(runId);
		TeamsCache.getInstance().putTeamList(runId, tl);
		return tl;
	}

	public Team getTeam(String teamId) {
		Team returnTeam = TeamsCache.getInstance().getTeam(teamId);
		if (returnTeam == null) {
			returnTeam = TeamManager.getTeam(teamId);
			if (returnTeam == null) {
				returnTeam = new Team();
				returnTeam.setError("no team exists with id " + teamId);
			}
			TeamsCache.getInstance().putTeam(teamId, returnTeam);
		}
		return returnTeam;
	}
	
	public void deleteTeam(Long runId) {
		TeamList tl = getTeams(runId);
		for (Team t : tl.getTeams()) {
			deleteTeam(t.getTeamId());
		}
	}
	
	public Team deleteTeam(String teamId) {
		TeamsCache.getInstance().removeTeam(teamId);
		Team t = TeamManager.deleteTeam(teamId);
		TeamsCache.getInstance().removeTeams(t.getRunId());
		UsersCache.getInstance().removeUser(t.getRunId());
		(new DeleteUsers(getAuthToken(), null, null, teamId)).scheduleTask();
		new NotifyUsersFromGame(getAuthToken(), t.getRunId(), null, TeamModification.ALTERED).scheduleTask();

		TeamModification tm = new TeamModification();
		tm.setModificationType(TeamModification.ALTERED);
		tm.setRunId(t.getRunId());
		UsersDelegator ud = new UsersDelegator(this);
		
		ChannelNotificator.getInstance().notify(ud.getCurrentUserAccount(), tm);

        (new UpdateVariableInstancesForTeam(authToken, this.account, teamId, t.getRunId(), null, 2)).scheduleTask();
        (new UpdateVariableEffectInstancesForTeam(authToken, this.account, teamId, t.getRunId(), null, 2)).scheduleTask();
		//TODO test if deleting users works...
		return t;
	}
}
