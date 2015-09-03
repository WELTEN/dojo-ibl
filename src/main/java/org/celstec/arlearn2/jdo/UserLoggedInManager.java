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
package org.celstec.arlearn2.jdo;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.beans.run.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserLoggedInManager {

	public static void submitUser(String email, String authToken) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UsersLoggedIn uli = new UsersLoggedIn();
		uli.setKey(authToken.hashCode());
		uli.setUsername(User.normalizeEmail(email));
		try {
			pm.makePersistent(uli);
		} finally {
			pm.close();
		}
	}
	
	public static void submitOauthUser(String id, String authToken) {
		PersistenceManager	 pm = PMF.get().getPersistenceManager();
		UsersLoggedIn uli = new UsersLoggedIn();
		uli.setKey(authToken.hashCode());
		uli.setUsername(id);
        uli.setAuthToken(authToken);
		try {
			pm.makePersistent(uli);
		} finally {
			pm.close();
		}
	}
	
	public static String getUser(String authToken) {
		if (authToken == null || authToken.equals("")) return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (authToken.startsWith("GoogleLogin")) authToken = authToken.substring(authToken.indexOf("auth=")+5);
		Key key = KeyFactory.createKey(UsersLoggedIn.class.getSimpleName(), authToken.hashCode());
		try {
			return ((UsersLoggedIn)pm.getObjectById(UsersLoggedIn.class, key)).getUsername();
		} catch (Exception e) {
			return null;
		}

	}
	
	

}
