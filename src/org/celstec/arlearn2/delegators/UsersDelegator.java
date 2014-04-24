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
package org.celstec.arlearn2.delegators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.cache.UserLoggedInCache;
import org.celstec.arlearn2.cache.UsersCache;
import org.celstec.arlearn2.delegators.notification.NotificationEngine;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.*;

public class UsersDelegator extends GoogleDelegator {

	public UsersDelegator(String authtoken) {
		super(authtoken);
	}

	public UsersDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public UsersDelegator(Account account, String token) {
		super(account, token);
	}

	public User createUser(User u) {
		User check = checkUser(u);
		if (check != null)
			return check;
		Run run = (new RunDelegator(this)).getRun(u.getRunId());

		u.setEmail(u.getAccountType()+":"+u.getLocalId());
		u.setGameId(run.getGameId());
		UserManager.addUser(u);
		UsersCache.getInstance().removeUser(u.getRunId()); // removing because
															// user might be
															// cached in a team
		// (new NotifyUpdateRun(authToken,u.getRunId(), true, false,
		// u.getEmail())).scheduleTask();

		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.CREATED);
		rm.setRun(run);
//		NotificationEngine.getInstance().notify(u.getEmail(), rm);
		new NotificationDelegator(this).broadcast(run, u.getFullId());
		if (this.account != null) {
			new NotificationDelegator(this).broadcast(u, u.getFullId());
		}
		(new UpdateGeneralItemsVisibility(authToken, this.account, u.getRunId(), u.getEmail(), 1)).scheduleTask();
        (new UpdateVariableInstancesForUser(authToken, this.account, u.getFullId(), u.getRunId(), run.getGameId(), 1)).scheduleTask();
        (new UpdateVariableEffectInstancesForUser(authToken, this.account, u.getFullId(), u.getRunId(), run.getGameId(), 1)).scheduleTask();

		return u;
	}

	private User checkUser(User u) {
		if (u.getRunId() == null) {
			u.setError("No run identifier specified");
			return u;
		}
		if (u.getTeamId() != null) {
		TeamList tl = (new TeamsDelegator(this)).getTeams(u.getRunId());
		if (!tl.getTeams().isEmpty()) {
			Team dbTeam = null;
			for (Team t : tl.getTeams()) {
				if (t.getTeamId().equals(u.getTeamId()))
					dbTeam = t;
			}
			if (dbTeam == null) {
				u.setError("teamId does not exist in db");
				return u;
			}
		}
		}
		return null;
	}

	public User selfRegister(User u, Run run) {
		User check = checkUser(u);
		if (check != null)
			return check;
		UsersCache.getInstance().removeUser(u.getRunId()); // removing because
															// user might be
															// cached in a team
		u.setEmail(User.normalizeEmail(u.getEmail()));

		UserManager.deleteUser(u.getRunId(), u.getEmail());

		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED);
		rm.setRun(run);
		UserManager.addUser(u);

//		rm = new RunModification();
//		rm.setModificationType(RunModification.CREATED);
//		rm.setRun(run);
//		ChannelNotificator.getInstance().notify(u.getEmail(), rm);
        (new NotificationDelegator(this)).broadcast(u, u.getFullId());
		return u;
	}

	public String getCurrentUserAccount() {
		if (account != null) return account.getFullId();
		String accountName = UserLoggedInCache.getInstance().getUser(this.authToken);
		if (accountName == null) {
			accountName = UserLoggedInManager.getUser(this.authToken);
			if (accountName != null)
				UserLoggedInCache.getInstance().putUser(this.authToken, accountName);
		}
		return accountName;
	}
	
	public Account getCurrentAccount() {
		if (account != null) return account;
		String accountName = UserLoggedInCache.getInstance().getUser(this.authToken);
		if (accountName == null) {
			accountName = UserLoggedInManager.getUser(this.authToken);
			
				
		}
		if (accountName != null){
			UserLoggedInCache.getInstance().putUser(this.authToken, accountName);
			if (accountName.contains(":")) {
				account = AccountManager.getAccount(accountName);
			}
		}
		return account;
	}

	public Account getCurrentUserAccountObject() {
		String accountName = getCurrentUserAccount();
		if (accountName == null || !accountName.contains(":")) {
			return null;
		}
		Account ac = new Account();
		StringTokenizer st = new StringTokenizer(accountName, ":");
		if (st.hasMoreTokens()) {
			ac.setAccountType(Integer.parseInt(st.nextToken()));
		}
		if (st.hasMoreTokens()) {
			ac.setLocalId(st.nextToken());
		}
		return ac;
	}

	public List<User> getUserList(Long runId, String name, String email, String teamId) {
		List<User> users = UsersCache.getInstance().getUserList(runId, name, email, teamId);
		if (users == null) {
			users = UserManager.getUserList(name, email, teamId, runId);
			AccountDelegator ad = new AccountDelegator(this);
			Iterator<User> it = users.iterator();
			while (it.hasNext()) {
				User u =  it.next();
				Account ac = ad.getContactDetails(u.getFullId());
				if (ac.getError()!= null) it.remove();
				u.setAccountData(ac);
			}
			UsersCache.getInstance().putUserList(users, runId, name, email, teamId);
		}
		return users;
	}

	public UserList getUsers(Long runId) {
		return getUsers(runId, null);
	}

	public UserList getUsers(Long runId, String teamId) {
		List<User> users = getUserList(runId, null, null, teamId);
		UserList returnList = new UserList();
		returnList.setRunId(runId);
		returnList.setUsers(users);
		return returnList;
	}

	public User getUserByEmail(Long runId, String account) {
		account = User.normalizeEmail(account); //TODO delete this line
		List<User> users = getUserList(runId, null, account, null);
		if (users.isEmpty())
			return null;
		return users.get(0);
	}

	public HashMap<String, User> getUserMap(Long runId) {
		HashMap<String, User> map = new HashMap<String, User>();
		for (User u : getUserList(runId, null, null, null)) {
			map.put(u.getFullId(), u);
		}
		return map;
	}

	public User deleteUser(Long runId, String email) {
		email = User.normalizeEmail(email);
		User user = getUserByEmail(runId, email);
		// UserManager.deleteUser(runId, email);
		UserManager.setStatusDeleted(runId, email);
		UsersCache.getInstance().removeUser(runId); // removing because user
													// might be cached in a team
		(new DeleteActions(authToken, this.account, runId, email)).scheduleTask();
		(new DeleteBlobs(authToken, this.account, runId, email)).scheduleTask();
		(new DeleteResponses(authToken,this.account,  runId, email)).scheduleTask();
		(new UpdateGeneralItemsVisibility(authToken, this.account, runId, email, 2)).scheduleTask();
        (new UpdateVariableInstancesForUser(authToken, this.account, email, runId, null, 2)).scheduleTask();
        (new UpdateVariableEffectInstancesForUser(authToken, this.account, email, runId, null, 2)).scheduleTask();

		if (this.account != null) {
            // new bean is created, because apn can not handle large pieces of data
            User notificationUser = new User();
            notificationUser.setRunId(user.getRunId());
            notificationUser.setFullIdentifier(user.getFullId());
			new NotificationDelegator(this).broadcast(notificationUser, user.getFullId());
		}
		return user;
	}

	public void deleteUser(long runId) {
		List<User> userList = getUserList(runId, null, null, null);
		for (User u : userList) {
			deleteUser(runId, u.getEmail());
		}
	}

	public void deleteUser(String teamId) {
		List<User> userList = getUserList(null, null, null, teamId);
		Long runId = null;
		for (User u : userList) {
			runId = u.getRunId();
			// UserManager.deleteUser(runId, u.getEmail());
			UserManager.setStatusDeleted(runId, u.getEmail());

			notifyRunDeleted(runId, u.getEmail());
		}
		if (runId != null)
			UsersCache.getInstance().removeUser(runId);
	}

	private void notifyRunDeleted(long runId, String email) {
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED);
		rm.setRun(new Run());
		rm.getRun().setRunId(runId);
		NotificationEngine.getInstance().notify(email, rm);
		new NotificationDelegator(this).broadcast(rm.getRun(), email);

		// ChannelNotificator.getInstance().notify(email, rm);
	}

	public void updateAllUsers() {
		UserManager.updateAll(this);
	}

//	public static class Account {
//		int type;
//		String localId;
//
//		public int getType() {
//			return type;
//		}
//
//		public void setType(int type) {
//			this.type = type;
//		}
//
//		public String getLocalId() {
//			return localId;
//		}
//
//		public void setLocalId(String localId) {
//			this.localId = localId;
//		}
//
//	}
}
