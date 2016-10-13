package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameAccessJDO;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.*;

public class GameAccessManager {

	public static GameAccessJDO addGameAccess(String localID, int accountType, long gameId, int accesRights) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameAccessJDO gameAccess = new GameAccessJDO();
		gameAccess.setAccessRights(accesRights);
		gameAccess.setLocalId(localID);
		gameAccess.setAccountType(accountType);
		gameAccess.setGameId(gameId);
		gameAccess.setUniqueId();
		gameAccess.setLastModificationDateGame(System.currentTimeMillis());
		try {
			pm.makePersistent(gameAccess);
			return gameAccess;
		} finally {
			pm.close();
		}
	}
	
	public static void resetGameAccessLastModificationDate(long gameId) {
		long lastModifiation = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(GameAccessJDO.class);
			query.setFilter("gameId == "+gameId);
			Iterator<GameAccessJDO> it = ((List<GameAccessJDO>) query.execute()).iterator();
			while (it.hasNext()) {
				it.next().setLastModificationDateGame(lastModifiation);
			}
		} finally {
			pm.close();
		}
	}

	public static GameAccessList getGameList(int accountType, String localId, String cursorString, long from) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameAccessList returnList = new GameAccessList();
		try {
			Query query = pm.newQuery(GameAccessJDO.class);
			if (cursorString != null) {
				Cursor c = Cursor.fromWebSafeString(cursorString);

				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
				query.setExtensions(extensionMap);
			}
			query.setRange(0, 5);
			String filter = null;
			String params = null;
			Object args[] = null;
			filter = "localId == localIdParam && accountType == accountTypeParam && lastModificationDateGame >= lastModificationDateGameParam";
			params = "String localIdParam, Integer accountTypeParam, Long lastModificationDateGameParam";
			args = new Object[] { localId, accountType, from };

			query.setFilter(filter);
			query.setOrdering("lastModificationDateGame desc");
			query.declareParameters(params);
			List<GameAccessJDO> results = (List<GameAccessJDO>) query.executeWithArray(args);
			Iterator<GameAccessJDO> it = (results).iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				GameAccessJDO object = it.next();
				returnList.addGameAccess(toBean(object));

			}
			Cursor c = JDOCursorHelper.getCursor(results);
			cursorString = c.toWebSafeString();
			if (returnList.getGameAccess().size() == 5) {
				returnList.setResumptionToken(cursorString);
			}
			returnList.setServerTime(System.currentTimeMillis());


		}finally {
			pm.close();
		}
		return returnList;

	}


	public static List<GameAccess> getGameList(int accountType, String localId, Long from, Long until) {
		ArrayList<GameAccess> accessDefinitions = new ArrayList<GameAccess>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(GameAccessJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDateGame <= untilParam";
			params = "String localIdParam, Integer accountTypeParam, Long untilParam";
			args = new Object[] { localId, accountType, until };
		} else if (until == null) {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDateGame >= fromParam";
			params = "String localIdParam, Integer accountTypeParam, Long fromParam";
			args = new Object[] { localId, accountType, from };
		} else {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDate >= fromParam & lastModificationDateGame <= untilParam";
			params = "String localIdParam, Integer accountTypeParam, Long fromParam, Long untilParam";
			args = new Object[] { localId, accountType, from, until };
		}

		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<GameAccessJDO> it = ((List<GameAccessJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			accessDefinitions.add(toBean((GameAccessJDO) it.next()));
		}
		return accessDefinitions;
	}
	
	public static List<GameAccess> getGameList(long gameId) {
		ArrayList<GameAccess> accessDefinitions = new ArrayList<GameAccess>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(GameAccessJDO.class);
			String filter = "gameId == gameIdParam ";
			String params = "Long gameIdParam";
			Object args[] = new Object[] { gameId};
		
		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<GameAccessJDO> it = ((List<GameAccessJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			accessDefinitions.add(toBean((GameAccessJDO) it.next()));
		}
		return accessDefinitions;
	}

	
	private static GameAccess toBean(GameAccessJDO jdo) {
		if (jdo == null)
			return null;
		GameAccess gameAccess = new GameAccess();
		gameAccess.setAccount(jdo.getAccountType()+":"+jdo.getLocalId());
		gameAccess.setAccessRights(jdo.getAccessRights());
		gameAccess.setGameId(jdo.getGameId());
		return gameAccess;
	}

	public static void removeGameAccess(String localID, int accountType, Long gameIdentifier) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GameAccessJDO jdo = pm.getObjectById(GameAccessJDO.class, accountType+":"+localID+":"+gameIdentifier);
//			GameAccessJDO jdo = (GameAccessJDO) pm.getObjectById(KeyFactory.createKey(GameAccessJDO.class.getSimpleName(), accountType+":"+localID+":"+gameIdentifier)); 
			pm.deletePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	public static GameAccessJDO getAccessById(String accessId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return pm.getObjectById(GameAccessJDO.class,accessId);
		} finally {
			pm.close();
		}
	}
}
