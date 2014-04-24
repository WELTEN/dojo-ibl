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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.ProgressRecord;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ProgressRecordJDO;

public class ProgressRecordManager {
	
	private static final String params[] = new String[]{"runId", "action", "generalItemId", "generalItemType", "scope", "userEmail","teamId"};
	private static final String paramsNames[] = new String[]{"runIdParam", "actionParam", "generalItemIdParam", "generalItemTypeParam", "scopeParam", "userEmailParam","teamIdParam"};
	private static final String types[] = new String[]{"Long", "String", "Long", "String", "String", "String", "String"};

	public static void addProgressRecord(String action, Long generalItemId, String generalItemType, Long runId, String scope, String teamId, String userEmail, Long time) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ProgressRecordJDO progresRecord = new ProgressRecordJDO();
		progresRecord.setAction(action);
		progresRecord.setGeneralItemId(generalItemId);
		progresRecord.setGeneralItemType(generalItemType);
		progresRecord.setRunId(runId);
		progresRecord.setScope(scope);
		progresRecord.setTeamId(teamId);
		progresRecord.setUserEmail(userEmail);
		progresRecord.setTime(time);
		try {
			pm.makePersistent(progresRecord);
		} finally {
			pm.close();
		}

	}
	
	public static List<ProgressRecord> getProgressRecord(Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		ArrayList<ProgressRecord> returnProgressDefinitions = new ArrayList<ProgressRecord>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<ProgressRecordJDO> it = getProgressRecord(pm, runId, action, generalItemId, generalItemType, scope, email, teamId).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((ProgressRecordJDO) it.next()));
		}
		return returnProgressDefinitions;
	}
	
	public static List<ProgressRecordJDO> getProgressRecord(PersistenceManager pm, Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		Query query = pm.newQuery(ProgressRecordJDO.class);
		Object args [] = new Object[]{runId, action, generalItemId, generalItemType, scope, email, teamId};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<ProgressRecordJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}
	
	private static ProgressRecord toBean(ProgressRecordJDO jdo) {
		if (jdo == null) return null;
		ProgressRecord pd = new ProgressRecord();
		pd.setAction(jdo.getAction());
		pd.setRunId(jdo.getRunId());
		pd.setGeneralItemId(jdo.getGeneralItemId());
		pd.setGeneralItemType(jdo.getGeneralItemType());
		pd.setScope(jdo.getScope());
		pd.setTeamId(jdo.getTeamId());
		pd.setEmail(jdo.getUserEmail());
		pd.setTimestamp(jdo.getTime());
		return pd;
	}

	public static void deleteProgressRecord(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<ProgressRecordJDO> toDelete = getProgressRecord(pm, runId, null, null, null, null, null, null);
		pm.deletePersistentAll(toDelete);
		pm.close();
	}
}
