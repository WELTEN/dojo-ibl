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
package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;

import net.sf.jsr107cache.Cache;

public class ActionCache {

	private static ActionCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ActionCache.class.getName());

	private ActionCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ActionCache getInstance() {
		if (instance == null)
			instance = new ActionCache();
		return instance;
	}
	
//	private static String ACTIONS_PREFIX = "Actions";
//
//	public long getActionsTableIdForRun(String authToken, Long runId) {
//		Long returnInt = (Long) cache.get(ACTIONS_PREFIX+authToken+runId);
//		if (returnInt == null) returnInt = -1l;
//		return returnInt;
//	}
//	
//	public void putActionsTableIdForRun(String authToken, Long runId, Long tableId) {
//		cache.put(ACTIONS_PREFIX+authToken+runId, tableId);
//	}
	
	private static String ACTIONS_HM = "ActionsRun";
	
	public ActionList getRunActions(Long runId) {
		return (ActionList) cache.get(ACTIONS_HM+runId);
	}
	
//	public HashMap<String, List<Action>> getAllActions(Long runId) {
//		return (HashMap<String, List<Action>>) cache.get(ACTIONS_HM+runId);
//	}
	
	public void putRunActions(Long runId, ActionList actionList) {
		cache.put(ACTIONS_HM+runId, actionList);
	}
	
	public void removeRunAction(Long runId) {
		cache.remove(ACTIONS_HM+runId);
	}
	
//	public void setAllActions(Long runId, HashMap<String, List<Action>> hm) {
//		cache.put(ACTIONS_HM+runId, hm);
//	}
	
//	public void addActionToHashMap(Long runId, Action action) {
//		HashMap<String, List<Action>> hm = getAllActions(runId);
//		if (hm == null) {
//			hm = new HashMap<String, List<Action>>();
//		}
//		List<Action> la = hm.get(action.getAction());
//		if (la == null) {
//			hm.put(action.getAction(), new ArrayList<Action>());
//			la = hm.get(action.getAction());
//		}
//		la.add(action);
//		setAllActions(runId, hm);
//	}
	
}
