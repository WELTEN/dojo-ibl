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
package org.celstec.arlearn2.jdo.manager;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.TeamJDO;

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class TeamManager {

	public static Team addTeam(Long runId, String teamId, String name) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TeamJDO team = new TeamJDO();
		team.setTeamId(teamId);
		team.setName(name);
		team.setRunId(runId);
		Team result = toBean(team);
		try {
			pm.makePersistent(team);
		} finally {
			pm.close();
		}
		return result;
	}

	public static TeamList getTeams(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(TeamJDO.class);
			query.setFilter("runId == runIdParam");
			query.declareParameters("Long runIdParam");
			Iterator<TeamJDO> it = ((List<TeamJDO>) query.execute(runId)).iterator();
			TeamList returnList = new TeamList();
			returnList.setRunId(runId);
			while (it.hasNext()) {
				returnList.addTeam(toBean((TeamJDO) it.next()));
			}
			return returnList;
		} finally {
			pm.close();
		}
	}

	public static Team getTeam(String teamId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(getTeamJDO(pm, teamId));
		} finally {
			pm.close();
		}
	}

	private static TeamJDO getTeamJDO(PersistenceManager pm, String teamId) {
		try {
			Key k = KeyFactory.createKey(TeamJDO.class.getSimpleName(), teamId);
			TeamJDO teamJDO = pm.getObjectById(TeamJDO.class, k);
			return teamJDO;
		} catch (Exception e) {
			return null;
		}
	}

	private static Team toBean(TeamJDO jdo) {
		if (jdo == null) return null;
		Team teamBean = new Team();
		teamBean.setName(jdo.getName());
		teamBean.setRunId(jdo.getRunId());
		teamBean.setTeamId(jdo.getTeamIdString());
		return teamBean;
	}

	public static Team deleteTeam(String teamId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Team t = null;
		try {
			TeamJDO teamJDO = getTeamJDO(pm, teamId);
			t = toBean(teamJDO);
			if (teamJDO != null) {
				pm.deletePersistent(teamJDO);
			}
		} finally {
			pm.close();
		}
		return t;
	}
}
