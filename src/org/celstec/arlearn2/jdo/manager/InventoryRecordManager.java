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

import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.InventoryRecordJDO;

public class InventoryRecordManager {

	private static final String params[] = new String[]{"runId", "generalItemId", "scope", "email", "teamId", "status"};
	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "scopeParam", "emailParam", "teamIdParam", "statusParam"};
	private static final String types[] = new String[]{"Long", "Long", "String", "String", "String", "String"};

	public static InventoryRecord addInventoryRecord(Long runId, Long generalItemId, String scope, String userEmail, String teamId,  Double lat, Double lng, String status, Long timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		InventoryRecordJDO inventoryRecord= new InventoryRecordJDO();
		inventoryRecord.setRunId(runId);
		inventoryRecord.setGeneralItemId(generalItemId);
		inventoryRecord.setScope(scope);
		inventoryRecord.setEmail(userEmail);
		inventoryRecord.setTeamId(teamId);
		inventoryRecord.setLat(lat);
		inventoryRecord.setLng(lng);
		inventoryRecord.setStatus(status);
		inventoryRecord.setTimeStamp(timeStamp);
		try {
			return toBean(pm.makePersistent(inventoryRecord));
		} finally {
			pm.close();
		}
	}
	
	public static void updateInventoryRecord(Long runId, Long generalItemId, String scope, String userEmail, String teamId, Double lat, Double lng, String status) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<InventoryRecordJDO> it =getInventoryRecord(pm, runId, generalItemId, scope, userEmail, teamId, null).iterator();
			while (it.hasNext()) {
				InventoryRecordJDO inventoryRecord = (InventoryRecordJDO) it.next();
				inventoryRecord.setLat(lat);
				inventoryRecord.setLng(lng);
				inventoryRecord.setStatus(status);
				inventoryRecord.setTeamId(teamId);
				inventoryRecord.setEmail(userEmail);
			}
		} finally {
			pm.close();
		}
	}
	
	public static List<InventoryRecord> getInventoryRecord(Long runId, Long generalItemId, String scope, String userEmail, String teamId, String status) {
		ArrayList<InventoryRecord> returnProgressDefinitions = new ArrayList<InventoryRecord>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<InventoryRecordJDO> it = getInventoryRecord(pm, runId, generalItemId, scope, userEmail, teamId, status).iterator();
			while (it.hasNext()) {
				returnProgressDefinitions.add(toBean((InventoryRecordJDO) it.next()));
			}
			return returnProgressDefinitions;
		} finally {
			pm.close();
		}
	}
	
	private static List<InventoryRecordJDO> getInventoryRecord(PersistenceManager pm, Long runId, Long generalItemId, String scope, String userEmail, String teamId, String status) {
		ArrayList<InventoryRecord> returnProgressDefinitions = new ArrayList<InventoryRecord>();
		Query query = pm.newQuery(InventoryRecordJDO.class);
		Object args [] = new Object[]{runId, generalItemId, scope, userEmail, teamId, status};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<InventoryRecordJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
//		while (it.hasNext()) {
//			returnProgressDefinitions.add(toBean((InventoryRecordJDO) it.next()));
//		}
//		
//		return returnProgressDefinitions;
	}
	
	private static InventoryRecord toBean(InventoryRecordJDO jdo) {
		if (jdo == null) return null;
		InventoryRecord pd = new InventoryRecord();
		
		pd.setRunId(jdo.getRunId());
		pd.setEmail(jdo.getEmail());
		pd.setScope(jdo.getScope());
		pd.setTeamId(jdo.getTeamId());
		pd.setGeneralItemId(jdo.getGeneralItemId());
		pd.setLat(jdo.getLat());
		pd.setLng(jdo.getLng());
		pd.setStatus(jdo.getStatus());
		pd.setTimestamp(jdo.getTimeStamp());
		return pd;
	}
	
	public static void deleteInventoryRecords(long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<InventoryRecordJDO> toDelete = getInventoryRecord(pm, runId, null, null, null, null, null);
		pm.deletePersistentAll(toDelete);
		pm.close();
	}
	
}
