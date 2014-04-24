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

import java.util.List;

//import org.celstec.arlearn2.beans.run.VisibleItem;

import com.google.appengine.api.utils.SystemProperty;

@Deprecated
public class VisibleItemsCache {//extends RunCache{

	private static VisibleItemsCache instance;

	private static String INVENTORY_RECORD_PREFIX = SystemProperty.applicationVersion.get()+"InventoryRecord";

	private VisibleItemsCache() {
	}

	public static VisibleItemsCache getInstance() {
		if (instance == null)
			instance = new VisibleItemsCache();
		return instance;
	}

//	public List<VisibleItem> getVisibleItems(Long runId, Object... args) {
//		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId,  args); 
//		return (List<VisibleItem>) getCache().get(cacheKey);
//	}
//
//	public void putVisibleItems(List<VisibleItem> visibleItem, Long runId, Object... args) {
//		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, args); 
//		getCache().put(cacheKey, visibleItem);
//	}
//	
//	public void removeVisibleItems(Long runId, Object... args) {
//		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, args); 
//		getCache().remove(cacheKey);
//	}
//	
//	@Override
//	protected String getType() {
//		return "VisibleItemsCache";
//	}

}
