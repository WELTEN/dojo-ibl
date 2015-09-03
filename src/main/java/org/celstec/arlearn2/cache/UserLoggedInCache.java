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

import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

import org.celstec.arlearn2.util.FusionCache;

import com.google.appengine.api.utils.SystemProperty;

public class UserLoggedInCache extends GenericCache{

	private static UserLoggedInCache instance;

	private static String PROGRESSDEFINITION_PREFIX = SystemProperty.applicationVersion.get()+"ProgressDefinition";

	private UserLoggedInCache() {
	}

	public static UserLoggedInCache getInstance() {
		if (instance == null)
			instance = new UserLoggedInCache();
		return instance;

	}
	
	private static String PREFIX = "UsersLoggedInCache";
	
	public String getUser(String authToken) {
		return (String) getCache().get(PREFIX+authToken);
	}
	
	public void putUser(String authToken, String account) {
		getCache().put(PREFIX+authToken, account);
	}
}
