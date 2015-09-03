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

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import net.sf.jsr107cache.Cache;

public class UsersCache {

	
	private static UsersCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(UsersCache.class
			.getName());

	private UsersCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static UsersCache getInstance() {
		if (instance == null)
			instance = new UsersCache();
		return instance;
	}
	
	private static String USERS_PREFIX = "Users";

	public int getUsersTableIdForRun(String authToken, String runId) {
		Integer returnInt = (Integer) cache.get(USERS_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putUsersTableIdForRun(String authToken, String runId, Integer tableId) {
		cache.put(USERS_PREFIX+authToken+runId, tableId);
	}
	
	
	public User getUser(Long runId, String email) {
		return (User) cache.get(USERS_PREFIX+runId+User.normalizeEmail(email));
	}
	
	public void putUser(Long runId, User u) {
		if (u.getEmail() != null && !u.getEmail().equals("")) {
			cache.put(USERS_PREFIX+runId+User.normalizeEmail(u.getEmail()), u);
		}
	}

	public void removeUser(Long runId, User u) {
		if (u.getEmail() != null && !u.getEmail().equals("")) {
			cache.remove(USERS_PREFIX+runId+User.normalizeEmail(u.getEmail()));
		}
	}

	
}
