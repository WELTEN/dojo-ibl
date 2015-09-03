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

import java.util.HashSet;
import net.sf.jsr107cache.Cache;

public abstract class RunCache {
	
	private static String PROGRESSRECORD_CK_PREFIX = "RunCache:CK";

	protected abstract Cache getCache();
	protected abstract String getType();

	protected void storeCacheKey(Long runId, String cachekey) {
		HashSet<String> hs = getCacheKey(runId);
		if (hs == null) {
			hs = new HashSet<String>();
		}  
		if (!hs.contains(cachekey)) {
			hs.add(cachekey);
			getCache().put(PROGRESSRECORD_CK_PREFIX+getType()+runId, hs);
		}
	}
	
	private HashSet<String> getCacheKey(Long runId) {
		return (HashSet<String>) getCache().get(PROGRESSRECORD_CK_PREFIX+getType()+runId);
	}
	
	protected boolean cacheKeyExists(Long runId, String cacheKey) {
		HashSet<String> hs = getCacheKey(runId);
		if (hs == null) return false;
		return hs.contains(cacheKey);
	}
	
	public void removeKeysForRun(Long runId) {
		 getCache().remove(PROGRESSRECORD_CK_PREFIX+getType()+runId);
	}
}
