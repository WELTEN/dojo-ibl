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
package org.celstec.arlearn2.tasks.beans.cleanUp;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.celstec.arlearn2.jdo.classes.UserJDO;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;

public class UserIterator extends GenericBean {

	String cursorString;

	public UserIterator() {
		super();
	}

	public UserIterator(String cursorString) {
		super();
		this.cursorString = cursorString;
	}

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

	@Override
	public void run() {
		if ("*".equals(cursorString)) {
			startIteration(null);
		} else {
			startIteration(cursorString);
		}
	}
	
	private void startIteration(String cursorString) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<UserJDO> users = UserManager.listAllUsers(pm, cursorString);
			processUser(pm, users);
			if (!users.isEmpty()) rescheduleIteration(users);
		} finally {
			pm.close();
		}

	}
	
	private void rescheduleIteration(List users) {
		Cursor cCursor = JDOCursorHelper.getCursor(users);
		String cursorString = cCursor.toWebSafeString();
		System.out.println("cursorout " + cursorString);
		(new UserIterator(cursorString)).scheduleTask();
	}
	
	private void processUser(PersistenceManager pm, List<UserJDO> users) {
		for (UserJDO userJDO : users) {
			processUser(pm, userJDO);
		}
	}
	
	private void processUser(PersistenceManager pm, UserJDO userJDO) {
		System.out.println("dealing with user "+userJDO.getEmail() +" "+userJDO.getRunId());
		if (userJDO.getDeleted() != null && userJDO.getDeleted() && userJDO.getLastModificationDate() != null && (userJDO.getLastModificationDate()+ (2592000000l)) < System.currentTimeMillis()) {
			System.out.println("*** deleted with user "+userJDO.getEmail() +" "+userJDO.getRunId());
			UserManager.deleteUser(pm, userJDO);
			
		} else {
			List<RunJDO> list = RunManager.getRuns(pm, userJDO.getRunId(), null, null, null, null);
			if (list.isEmpty()) {
				UserManager.deleteUser(pm, userJDO);
			}
		}
		
	}
	

}
