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
package org.celstec.arlearn2.cache;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.run.InventoryRecord;

import com.google.appengine.api.utils.SystemProperty;

public class InventoryRecordCache extends RunCache{

	private static InventoryRecordCache instance;

	private static String INVENTORY_RECORD_PREFIX = SystemProperty.applicationVersion.get()+"InventoryRecord";

	private InventoryRecordCache() {
	}

	public static InventoryRecordCache getInstance() {
		if (instance == null)
			instance = new InventoryRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "InventoryRecordCache";
	}

	public List<InventoryRecord> getInventoryRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<InventoryRecord>) getCache().get(cacheKey);
	}

	public void putInventoryRecordList(List<InventoryRecord> inventoryRecord, Long runId, String scope,  Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, inventoryRecord);
	}
	
	public void removeInventoryRecords(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	public void putInventoryRecordList(InventoryRecord ir, Long runId, String scope, Long generalItemId, String email, String teamId) {
		ArrayList<InventoryRecord> irs = new ArrayList<InventoryRecord>();
		irs.add(ir);
		putInventoryRecordList(irs, runId, scope, generalItemId, email, teamId);
		
	}
}
