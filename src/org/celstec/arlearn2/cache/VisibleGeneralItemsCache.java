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

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

import com.google.appengine.api.utils.SystemProperty;

public class VisibleGeneralItemsCache extends GameCache {
	private static VisibleGeneralItemsCache instance;
	
	private static String VISIBLEGENERALITEM_PREFIX = SystemProperty.applicationVersion.get()+"VisibleGeneralItemDefinition";

	private VisibleGeneralItemsCache() {
	}

	public static VisibleGeneralItemsCache getInstance() {
		if (instance == null)
			instance = new VisibleGeneralItemsCache();
		return instance;
	}
	
	public GeneralItemList getVisibleGeneralitems(Long runId, Object... args) {
		String cacheKey = generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args); 
		if (!cacheKeyExists(runId, cacheKey)) return null;
		return (GeneralItemList) getCache().get(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args));
	}
	
	public void putVisibleGeneralItemList(GeneralItemList giList, Long runId, Object... args) {
		String cachekey = generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args); 
		storeCacheKey(runId, cachekey);
		getCache().put(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args), giList);
	}
	
	public void removeGeneralItemList(Long runId, Object... args) {
		 getCache().remove(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args));
	
	}
	
	public void removeGeneralItemList(Long runId) {
		removeKeysForGame(runId);
	}
	
	@Override
	protected String getType() {
		return "VisibleGeneralItems";
	}
	

}
