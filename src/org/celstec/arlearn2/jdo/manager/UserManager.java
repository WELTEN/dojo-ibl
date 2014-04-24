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
package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.UserJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class UserManager {

	private static final String params[] = new String[] { "name", "email", "teamId", "runId" };
	private static final String paramsNames[] = new String[] { "nameParam", "emailParam", "teamIdParam", "runIdParam" };
	private static final String types[] = new String[] { "String", "String", "String", "Long" };

	public static void addUser(User bean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserJDO user = new UserJDO();
		user.setTeamId(bean.getTeamId());
		user.setRunId(bean.getRunId());
		user.setEmail(bean.getFullId());
		user.setGameId(bean.getGameId());
		Long currentTime = System.currentTimeMillis();
		user.setLastModificationDate(currentTime);
		user.setLastModificationDateGame(currentTime);
		if (bean.getDeleted()!= null && bean.getDeleted()) user.setDeleted(bean.getDeleted());

		user.setId();
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
		user.setPayload(new Text(jbs.serialiseToJson().toString()));
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}

	}

	public static void gameChanged(User u) {
		Key key = KeyFactory.createKey(UserJDO.class.getSimpleName(), UserJDO.generateId(u.getFullId(), u.getRunId()));
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserJDO uJDO = pm.getObjectById(UserJDO.class, key);
			Long currentTime = System.currentTimeMillis();
			uJDO.setLastModificationDateGame(currentTime);
		} finally {
			pm.close();
		}
		
		if (u.getGameId() != null) {
//			GameModification gm = new GameModification();
//			gm.setModificationType(GameModification.ALTERED);
//			ChannelNotificator.getInstance().notify(u.getEmail(), gm);
			Game g =new Game();
			g.setGameId(u.getGameId());
			new NotificationDelegator().broadcast(g, u.getFullId());

		}

	}

	public static List<User> getUserList(String name, String email, String teamId, Long runId) {
		ArrayList<User> returnProgressDefinitions = new ArrayList<User>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<UserJDO> it = getUsers(pm, name, email, teamId, runId).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((UserJDO) it.next()));
		}
		return returnProgressDefinitions;
	}

	public static List<User> getUserList(String email, Long from, Long until) {
		ArrayList<User> returnProgressDefinitions = new ArrayList<User>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(UserJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "email == emailParam & lastModificationDate <= untilParam";
			params = "String emailParam, Long untilParam";
			args = new Object[] { email, until };
		} else if (until == null) {
			filter = "email == emailParam & lastModificationDate >= fromParam";
			params = "String emailParam, Long fromParam";
			args = new Object[] { email, from };
		} else {
			filter = "email == emailParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
			params = "String emailParam, Long fromParam, Long untilParam";
			args = new Object[] { email, from, until };
		}

		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((UserJDO) it.next()));
		}
		return returnProgressDefinitions;
	}

	public static List<User> getUserListGameParticipate(String email, Long from, Long until) {
		ArrayList<User> returnProgressDefinitions = new ArrayList<User>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(UserJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "email == emailParam & lastModificationDateGame <= untilParam";
			params = "String emailParam, Long untilParam";
			args = new Object[] { email, until };
		} else if (until == null) {
			filter = "email == emailParam & lastModificationDateGame >= fromParam";
			params = "String emailParam, Long fromParam";
			args = new Object[] { email, from };
		} else {
			filter = "email == emailParam & lastModificationDateGame >= fromParam & lastModificationDateGame <= untilParam";
			params = "String emailParam, Long fromParam, Long untilParam";
			args = new Object[] { email, from, until };
		}

		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((UserJDO) it.next()));
		}
		return returnProgressDefinitions;
	}

	public static List<UserJDO> getUsers(PersistenceManager pm, String name, String email, String teamId, Long runId) {
		Query query = pm.newQuery(UserJDO.class);
		Object args[] = { name, email, teamId, runId };
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<UserJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}

	public static User getUser(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(getUserJDO(pm, runId, email));
		} finally {
			pm.close();
		}
	}

	private static UserJDO getUserJDO(PersistenceManager pm, Long runId, String email) {
		try {
			Key k = KeyFactory.createKey(UserJDO.class.getSimpleName(), UserJDO.generateId(email, runId));
			UserJDO userJDO = pm.getObjectById(UserJDO.class, k);
			return userJDO;
		} catch (Exception e) {
			return null;
		}
	}

	private static User toBean(UserJDO jdo) {
		if (jdo == null)
			return null;
		User userBean = null;
		try {
			JsonBeanDeserializer jbd = new JsonBeanDeserializer(jdo.getPayload().getValue());
			userBean = (User) jbd.deserialize(User.class);
		} catch (Exception e) {
			e.printStackTrace();
			userBean = new User();
		}
		userBean.setRunId(jdo.getRunId());
		userBean.setTeamId(jdo.getTeamId());
		userBean.setFullIdentifier(jdo.getEmail());
		userBean.setDeleted(jdo.getDeleted());
		userBean.setGameId(jdo.getGameId());
		userBean.setLastModificationDateGame(jdo.getLastModificationDateGame());
		return userBean;
	}

	public static void deleteUser(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserJDO userJDO = getUserJDO(pm, runId, email);
			if (userJDO != null) {
				pm.deletePersistent(userJDO);
			}
		} finally {
			pm.close();
		}
	}

	public static void deleteUser(PersistenceManager pm, UserJDO userJDO) {
		pm.deletePersistent(userJDO);
	}

	public static void setStatusDeleted(long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<UserJDO> deleteList = getUsers(pm, null, email, null, runId);
			for (UserJDO jdo : deleteList) {
				jdo.setDeleted(true);
				jdo.setLastModificationDate(System.currentTimeMillis());
			}
		} finally {
			pm.close();
		}
	}

	private final static int LIMIT = 20;

	public static List<UserJDO> listAllUsers(PersistenceManager pm, String cursorString) {
		javax.jdo.Query query = pm.newQuery(UserJDO.class);
		if (cursorString != null) {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
		query.setRange(0, LIMIT);
		return (List<UserJDO>) query.execute();
	}

	// private static User toBean(UserJDO jdo) {
	// if (jdo == null) return null;
	// User pd = new User();
	// pd.setName(jdo.getName());
	// pd.setEmail(jdo.getEmail());
	// pd.setRunId(jdo.getRunId());
	// pd.setTeamId(jdo.getTeamId());
	//
	// return pd;
	// }
	
	private static String cursorString = null;

	public static void updateAll(GoogleDelegator gd) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		Query query = pm.newQuery(UserJDO.class);
		if (cursorString != null) {

			Cursor c = Cursor.fromWebSafeString(cursorString);
			Map<String, Object> extendsionMap = new HashMap<String, Object>();
			extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
			query.setExtensions(extendsionMap);
		}
		query.setRange(0, 20);

		RunDelegator rd = new RunDelegator(gd);
		GameDelegator gad = new GameDelegator(gd);
		List<UserJDO> results = (List<UserJDO>) query.execute();
		Iterator<UserJDO> it = (results).iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			UserJDO object = it.next();
			if (object != null && (object.getLastModificationDateGame() == null || object.getGameId() == null)) {
				
//				rd.get
				long runId = object.getRunId();
				Run r = rd.getRun(runId);
				Game game = gad.getNotOwnedGame(r.getGameId());
				object.setGameId(game.getGameId());
				object.setLastModificationDateGame(System.currentTimeMillis());

			}
		}
		Cursor c = JDOCursorHelper.getCursor(results);
		cursorString = c.toWebSafeString();
		} finally {
			pm.close();
		}		
	}

	public static void updateAccount(String accountFrom, String toAccount, Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserJDO.class);
			String filter = null;
			String params = null;
			Object args[] = null;
			filter = "runId == runIdParam & email == emailParam";
			params = "Long runIdParam, String emailParam";
			args = new Object[] { runId, accountFrom };

			query.setFilter(filter);
			query.declareParameters(params);
			Iterator<UserJDO> it = ((List<UserJDO>) query
					.executeWithArray(args)).iterator();
			while (it.hasNext()) {
				UserJDO oldUser = it.next();
				User bean = toBean(oldUser);
				UserJDO user = new UserJDO();
				user.setTeamId(bean.getTeamId());
//				user.setName(bean.getName());
				user.setRunId(bean.getRunId());
				user.setEmail(toAccount);
				user.setGameId(bean.getGameId());
				Long currentTime = System.currentTimeMillis();
				user.setLastModificationDate(currentTime);
				user.setLastModificationDateGame(currentTime);

				user.setId();
				JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
				user.setPayload(new Text(jbs.serialiseToJson().toString()));
				pm.makePersistent(user);
				pm.deletePersistent(oldUser);

			}
		} finally {
			pm.close();
		}
		
	}
}
