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

public abstract class RunCache extends GenericCache {

	private static String RUNCACHE_CK_PREFIX = SystemProperty.applicationVersion.get()+"RunCache:CK";

	protected abstract String getType();

	protected void storeCacheKeyForScope(Long runId, String scope, String cachekey) {
		super.storeCacheKey(runId, RUNCACHE_CK_PREFIX+getType()+scope, cachekey);
	}
	
	protected HashSet<String> getCacheKeyForScope(Long runId, String scope) {
		return super.getCacheKey(runId, RUNCACHE_CK_PREFIX+getType()+scope);
	}
	
	protected boolean cacheKeyExistsForScope(Long runId, String scope, String cacheKey) {
		return super.cacheKeyExists(runId, RUNCACHE_CK_PREFIX+getType()+scope, cacheKey);		
	}
	
	protected void removeKeysForRun(Long runId, String scope) {
		super.removeKeysForGame(runId, RUNCACHE_CK_PREFIX+getType()+scope);
	}
}
