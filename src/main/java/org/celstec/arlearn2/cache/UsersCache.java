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

import org.celstec.arlearn2.beans.run.User;

import com.google.appengine.api.utils.SystemProperty;

public class UsersCache  extends RunCache{

	private static UsersCache instance;

	private static String USER_PREFIX = SystemProperty.applicationVersion.get()+"UsersCache";

	private UsersCache() {
	}

	public static UsersCache getInstance() {
		if (instance == null)
			instance = new UsersCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "UsersCache";
	}
	
	public List<User> getUserList(Long runId, Object... args) {
		String cacheKey = generateCacheKey(USER_PREFIX, runId, args); 
		if (!cacheKeyExists(runId, USER_PREFIX, cacheKey)) return null;
		return (List<User>) getCache().get(cacheKey);
	}
	
	public void putUserList(List<User> pr, Long runId, Object... args) {
		String cacheKey = generateCacheKey(USER_PREFIX, runId, args); 
		storeCacheKey(runId, USER_PREFIX, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeUser(Long runId) {
		removeKeysForGame(runId, USER_PREFIX);
	}

}
