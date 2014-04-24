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

import java.util.HashSet;

import com.google.appengine.api.utils.SystemProperty;

public abstract class GameCache extends GenericCache {

	private static String GAMECACHE_CK_PREFIX = SystemProperty.applicationVersion.get()+"GameCache:CK";

	protected abstract String getType();

	protected void storeCacheKey(Long gameId, String cachekey) {
		HashSet<String> hs = getCacheKey(gameId);
		if (hs == null) {
			hs = new HashSet<String>();
		}  
		if (!hs.contains(cachekey)) {
			hs.add(cachekey);
			getCache().put(GAMECACHE_CK_PREFIX+getType()+gameId, hs);
		}
	}
	
	private HashSet<String> getCacheKey(Long gameId) {
		return (HashSet<String>) getCache().get(GAMECACHE_CK_PREFIX+getType()+gameId);
	}
	
	protected boolean cacheKeyExists(Long gameId, String cacheKey) {
		HashSet<String> hs = getCacheKey(gameId);
		if (hs == null) return false;
		return hs.contains(cacheKey);
	}
	
	protected void removeKeysForGame(Long gameId) {
		 getCache().remove(GAMECACHE_CK_PREFIX+getType()+gameId);
	}
	
}
