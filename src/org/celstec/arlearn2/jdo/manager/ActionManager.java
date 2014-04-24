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

import java.util.*;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Cursor;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ActionJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

public class ActionManager {

	private static final String params[] = new String[]{"runId", "action", "userEmail", "generalItemId", "generalItemType"};
	private static final String paramsNames[] = new String[]{"runIdParam", "actionParam", "userEmailParam", "generalItemIdParam", "generalItemTypeParam"};
	private static final String types[] = new String[]{"Long", "String", "String", "String", "String"};


    private static final int ACTIONS_IN_LIST = 20;

	public static List<Action> getActions(Long runId, String action, String userEmail, String generalItemId, String generalItemType) {
		ArrayList<Action> returnProgressDefinitions = new ArrayList<Action>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<ActionJDO> it = getActionsJDO(pm, runId,action, userEmail, generalItemId, generalItemType).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((ActionJDO) it.next()));
		}
		return returnProgressDefinitions;

	}
	
	private static List<ActionJDO> getActionsJDO(PersistenceManager pm, Long runId, String action, String userEmail, String generalItemId, String generalItemType) {
		Query query = pm.newQuery(ActionJDO.class);
		Object args [] ={runId,action, userEmail, generalItemId, generalItemType};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<ActionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	
	public static void addAction(Long runId, String action, String userEmail, Long generalItemId, String generalItemType, Long time) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActionJDO actionJDO = new ActionJDO();
		actionJDO.setAction(action);
		actionJDO.setGeneralItemId(generalItemId);
		actionJDO.setGeneralItemType(generalItemType);
		actionJDO.setRunId(runId);
		actionJDO.setTime(time);
		actionJDO.setUserEmail(userEmail);
		try {
			pm.makePersistent(actionJDO);
		} finally {
			pm.close();
		}
	}

	public static ActionList runActions(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<ActionJDO> it = runActions(pm, runId).iterator();
			ActionList returnList = new ActionList();
			returnList.setRunId(runId);
			while (it.hasNext()) {
				returnList.addAction(toBean((ActionJDO) it.next()));
			}
			return returnList;
		} finally {
			pm.close();
		}
	}
	
	public static List<ActionJDO> runActions(PersistenceManager pm, Long runId) {
		Query query = pm.newQuery(ActionJDO.class);
		query.setFilter("runId == runIdParam ");
		query.declareParameters("Long runIdParam");
		return (List<ActionJDO>) query.executeWithArray(runId);
	}

	private static Action toBean(ActionJDO jdo) {
		if (jdo == null)
			return null;
		Action actionBean = new Action();
		actionBean.setAction(jdo.getAction());
		actionBean.setGeneralItemId(jdo.getGeneralItemId());
		actionBean.setGeneralItemType(jdo.getGeneralItemType());
		actionBean.setRunId(jdo.getRunId());
		actionBean.setTime(jdo.getTime());
		actionBean.setTimestamp(jdo.getTime());
		actionBean.setUserEmail(jdo.getUserEmail());
		return actionBean;
	}

	public static void deleteActions(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ActionJDO> visToDelete =runActions(pm, runId);
			pm.deletePersistentAll(visToDelete);
		} finally {
			pm.close();
		}
	}
	
	public static void deleteActions(Long runId, String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ActionJDO> visToDelete =getActionsJDO(pm, runId, null, userId, null, null);
			pm.deletePersistentAll(visToDelete);
		} finally {
			pm.close();
		}
	}


    public static ActionList getActions(Long runId, Long from, Long until, String cursorString) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        ActionList returnList = new ActionList();
        try {
            Query query = pm.newQuery(ActionJDO.class);
            if (cursorString != null) {

                Cursor c = Cursor.fromWebSafeString(cursorString);
                Map<String, Object> extensionMap = new HashMap<String, Object>();
                extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
                query.setExtensions(extensionMap);
            }
            query.setRange(0, ACTIONS_IN_LIST);
            String filter = null;
            String params = null;
            Object args[] = null;
            if (from == null) {
                filter = "runId == runIdParam & time <= untilParam";
                params = "Long runIdParam, Long untilParam";
                args = new Object[] { runId, until };
            } else if (until == null) {
                filter = "runId == runIdParam & time >= fromParam";
                params = "Long runIdParam, Long fromParam";
                args = new Object[] { runId, from };
            } else {
                filter = "runId == runIdParam & time >= fromParam & time <= untilParam";
                params = "Long runIdParam, Long fromParam, Long untilParam";
                args = new Object[] { runId, from, until };
            }

            query.setFilter(filter);
            query.declareParameters(params);
            List<ActionJDO> results = (List<ActionJDO>) query.executeWithArray(args);
            Iterator<ActionJDO> it = (results).iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                ActionJDO object = it.next();
                returnList.addAction(toBean(object));

            }
            Cursor c = JDOCursorHelper.getCursor(results);
            cursorString = c.toWebSafeString();
            if (returnList.getActions().size() == ACTIONS_IN_LIST) {
                returnList.setResumptionToken(cursorString);
            }
            returnList.setServerTime(System.currentTimeMillis());


        }finally {
            pm.close();
        }
        return returnList;

    }
}
