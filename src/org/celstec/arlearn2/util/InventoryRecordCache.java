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

import org.celstec.arlearn2.beans.run.InventoryRecord;

import net.sf.jsr107cache.Cache;

public class InventoryRecordCache {

	private static InventoryRecordCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(InventoryRecordCache.class
			.getName());

	private InventoryRecordCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static InventoryRecordCache getInstance() {
		if (instance == null)
			instance = new InventoryRecordCache();
		return instance;
	}
	
	private static String INVENTORYRECORD_PREFIX = "InventoryRecord";


	public int getInventoryRecordTableIdForRun(String authToken, Long runId) {
		Integer returnInt = (Integer) cache.get(INVENTORYRECORD_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putInventoryRecordTableIdForRun(String authToken, Long runId, Integer tableId) {
		cache.put(INVENTORYRECORD_PREFIX+authToken+runId, tableId);
	}
	
	
	public InventoryRecord getInventoryRecord(Long runId, String generalItemId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (InventoryRecord)cache.get(INVENTORYRECORD_PREFIX+generalItemId+scope);
		} else if ("team".equals(scope)) {
			return (InventoryRecord)cache.get(INVENTORYRECORD_PREFIX+generalItemId+scope+teamId);
		} else if ("user".equals(scope)) {
			return (InventoryRecord)cache.get(INVENTORYRECORD_PREFIX+generalItemId+scope+email);
		}
		return null;
	}

	public void putInventoryRecord(Long runId, InventoryRecord pr) {
		if (pr != null) {
			if ("all".equals(pr.getScope())) {
				cache.put(INVENTORYRECORD_PREFIX+pr.getGeneralItemId()+pr.getScope(), pr);
			} else if ("team".equals(pr.getScope())) {
				cache.put(INVENTORYRECORD_PREFIX+pr.getGeneralItemId()+pr.getScope()+pr.getTeamId(), pr);
			} else if ("user".equals(pr.getScope())) {
				cache.put(INVENTORYRECORD_PREFIX+pr.getGeneralItemId()+pr.getScope()+pr.getEmail(), pr);
			}
		}
	}
	
	public void removeInventoryRecord(Long runId, String generalItemId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(INVENTORYRECORD_PREFIX+generalItemId+scope);
		} else if ("team".equals(scope)) {
			cache.remove(INVENTORYRECORD_PREFIX+generalItemId+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(INVENTORYRECORD_PREFIX+generalItemId+scope+email);
		}
	}
	
}
