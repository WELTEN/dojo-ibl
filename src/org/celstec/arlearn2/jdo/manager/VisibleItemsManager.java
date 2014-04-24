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

//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.jdo.PersistenceManager;
//import javax.jdo.Query;
//
//import org.celstec.arlearn2.beans.run.VisibleItem;
//import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
//import org.celstec.arlearn2.jdo.PMF;
//import org.celstec.arlearn2.jdo.classes.RunJDO;
//import org.celstec.arlearn2.jdo.classes.VisibleItemsJDO;;

@Deprecated
public class VisibleItemsManager {

//	private static final String params[] = new String[]{"runId", "generalItemId", "email", "teamId"};
//	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "emailParam", "teamIdParam"};
//	private static final String types[] = new String[]{"Long", "Long", "String", "String"};
//
//	public static VisibleItem addVisibleItem(Long runId, Long generalItemId, String email, String teamId) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		VisibleItemsJDO visibleItem = new VisibleItemsJDO();
//		visibleItem.setRunId(runId);
//		visibleItem.setGeneralItemId(generalItemId);
//		visibleItem.setEmail(email);
//		visibleItem.setTeamId(teamId);
//		try {
//			return toBean(pm.makePersistent(visibleItem));
//		} finally {
//			pm.close();
//		}
//
//	}
//	public static List<VisibleItem> getVisibleItems(Long runId, String generalItemId, String email, String team) {
//		ArrayList<VisibleItem> returnVisibleItems = new ArrayList<VisibleItem>();
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Iterator<VisibleItemsJDO> it = getVisibleItems(pm, runId, generalItemId, email, team).iterator();
//		while (it.hasNext()) {
//			returnVisibleItems.add(toBean((VisibleItemsJDO) it.next()));
//		}
//		return returnVisibleItems;
//	}
//	
//	public static void deleteVisibleItems(Long runId) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<VisibleItemsJDO> visToDelete = getVisibleItems(pm, runId, null, null, null);
//			pm.deletePersistentAll(visToDelete);
//		} finally {
//			pm.close();
//		}
//
//	}
//	
//	private static List<VisibleItemsJDO> getVisibleItems(PersistenceManager pm, Long runId, String generalItemId, String email, String team) {
//		Query query = pm.newQuery(VisibleItemsJDO.class);
//		Object args [] = new Object[]{runId, generalItemId, email, team};
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//		return ((List<VisibleItemsJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
//	}
//	
//	private static VisibleItem toBean(VisibleItemsJDO jdo) {
//		if (jdo == null) return null;
//		VisibleItem vi = new VisibleItem();
//		
//		vi.setRunId(jdo.getRunId());
//		vi.setGeneralItemId(jdo.getGeneralItemId());
//		vi.setTeamId(jdo.getTeamId());
//		vi.setEmail(jdo.getEmail());
//		
//		return vi;
//	}
	
}
