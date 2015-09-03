package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameAccessJDO;

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
