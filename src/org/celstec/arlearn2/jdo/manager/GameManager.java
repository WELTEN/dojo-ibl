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
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.GameJDO;

public class GameManager {

	private static final String params[] = new String[] { "id", "creatorEmail", "owner", "feedUrl", "title" };
	private static final String paramsNames[] = new String[] { "gameParam", "creatorEmailParam", "ownerEmailParam", "feedUrlParam", "titleParam" };
	private static final String types[] = new String[] { "Long", "String", "String", "String", "String" };

//	@Deprecated
//	public static Long addGame(String title, String owner, String creatorEmail, String feedUrl, Long gameId, Config config, Double lat, Double lng) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		GameJDO gameJdo = new GameJDO();
//		gameJdo.setGameId(gameId);
//		gameJdo.setCreatorEmail(creatorEmail);
//		gameJdo.setOwner(owner);
//		gameJdo.setFeedUrl(feedUrl);
//		gameJdo.setTitle(title);
//        gameJdo.setLat(lat);
//        gameJdo.setLng(lng);
//		gameJdo.setLastModificationDate(System.currentTimeMillis());
//		if (config != null)  {
//			gameJdo.setConfig(config.toString());
//		}
//		try {
//			GameJDO persistentGame = pm.makePersistent(gameJdo);
//			return persistentGame.getGameId();
//
//		} finally {
//			pm.close();
//		}
//	}
	
	public static Long addGame(Game game, String myAccount) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO gameJdo = new GameJDO();
		gameJdo.setGameId(game.getGameId());
		gameJdo.setCreatorEmail(game.getCreator());
		gameJdo.setOwner(myAccount);
		gameJdo.setFeedUrl(game.getFeedUrl());
		gameJdo.setTitle(game.getTitle());
		gameJdo.setSharing(game.getSharing());
		gameJdo.setDescription(game.getDescription());
        gameJdo.setLat(game.getLat());
        gameJdo.setLng(game.getLng());
        gameJdo.setLanguage(game.getLanguage());
		if (game.getLicenseCode() !=null) gameJdo.setLicenseCode(game.getLicenseCode());
		gameJdo.setLastModificationDate(System.currentTimeMillis());
		if (game.getConfig() != null)  {
			gameJdo.setConfig(game.getConfig().toString());
		}
		try {
			GameJDO persistentGame = pm.makePersistent(gameJdo);
			return persistentGame.getGameId();

		} finally {
			pm.close();
		}
	}

	
	public static Long addGame(Game game) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO gameJdo = new GameJDO();
		gameJdo.setGameId(game.getGameId());
		gameJdo.setTitle(game.getTitle());
		gameJdo.setSharing(game.getSharing());
		gameJdo.setDescription(game.getDescription());
        gameJdo.setLat(game.getLat());
        gameJdo.setLng(game.getLng());
        gameJdo.setLanguage(game.getLanguage());
		if (game.getDeleted() != null) gameJdo.setDeleted(game.getDeleted());
		if (game.getLicenseCode() !=null) gameJdo.setLicenseCode(game.getLicenseCode());

		gameJdo.setLastModificationDate(System.currentTimeMillis());
		if (game.getConfig() != null)  {
			gameJdo.setConfig(game.getConfig().toString());
		}
		try {
			GameJDO persistentGame = pm.makePersistent(gameJdo);
			return persistentGame.getGameId();

		} finally {
			pm.close();
		}
	}
	
	public static List<Game> getGames(Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
		ArrayList<Game> returnGames = new ArrayList<Game>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<GameJDO> it = getGames(pm, gameId, creatorEmail, owner, feedUrl, title).iterator();
			while (it.hasNext()) {
				returnGames.add(toBean((GameJDO) it.next()));
			}
			return returnGames;
		} finally {
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public static List<GameJDO> getGames(PersistenceManager pm, Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
		Query query = pm.newQuery(GameJDO.class);
		Object args[] = { gameId, creatorEmail, owner, feedUrl, title };
		if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
			query.setFilter("deleted == null");
			return (List<GameJDO>) query.execute();
		}
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<GameJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}

	public static List<Game> getGames(String email, Long from, Long until) {
		ArrayList<Game> returnProgressDefinitions = new ArrayList<Game>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(GameJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "owner == emailParam & lastModificationDate <= untilParam";
			params = "String emailParam, Long untilParam";
			args = new Object[]{email, until};
		} else if (until == null) {
			filter = "owner == emailParam & lastModificationDate >= fromParam";
			params = "String emailParam, Long fromParam";
			args = new Object[]{email, from};
		} else {
			filter = "owner == emailParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
			params = "String emailParam, Long fromParam, Long untilParam";
			args = new Object[]{email, from, until};
		}
		
		query.setFilter(filter);
		query.declareParameters(params);
		@SuppressWarnings("unchecked")
		Iterator<GameJDO> it = ((List<GameJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((GameJDO) it.next()));
		}
		return returnProgressDefinitions;
	}

	
	public static void deleteGame(Long gameIdentifier) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<GameJDO> gamesToDelete = getGames(pm, gameIdentifier, null, null, null, null);
			pm.deletePersistentAll(gamesToDelete);
		} finally {
			pm.close();
		}
	}

	private static Game toBean(GameJDO jdo) {
		if (jdo == null)
			return null;
		Game game = new Game();
		game.setCreator(jdo.getCreatorEmail());
		game.setTitle(jdo.getTitle());
		game.setFeedUrl(jdo.getFeedUrl());
		game.setGameId(jdo.getGameId());
		game.setOwner(jdo.getOwner());
		game.setDescription(jdo.getDescription());
		game.setSharing(jdo.getSharing());
        game.setLng(jdo.getLng());
        game.setLat(jdo.getLat());
        game.setLanguage(jdo.getLanguage());
		if (jdo.getLicenseCode() !=null) game.setLicenseCode(jdo.getLicenseCode());
		if (jdo.getLastModificationDate() != null) {
			game.setLastModificationDate(jdo.getLastModificationDate());
		}
		if (jdo.getConfig() != null && !"".equals(jdo.getConfig())) {
			JsonBeanDeserializer jbd;
			try {
				jbd = new JsonBeanDeserializer(jdo.getConfig());
				Config config = (Config) jbd.deserialize(Config.class);
				game.setConfig(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (jdo.getDeleted() == null) {
			game.setDeleted(false);
		} else {
			game.setDeleted(jdo.getDeleted());
		}
		return game;
	}

    public static void makeGameFeatured(Long gameId, boolean featured) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            GameJDO game = pm.getObjectById(GameJDO.class, KeyFactory.createKey(GameJDO.class.getSimpleName(), gameId));
            game.setFeatured(featured);
        } finally {
             pm.close();;
        }
    }

    public static List<Game> getFeaturedGames() {
        ArrayList<Game> returnProgressDefinitions = new ArrayList<Game>();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(GameJDO.class);
        String filter = "featured == true & sharing == 3";

        query.setFilter(filter);
        @SuppressWarnings("unchecked")
        Iterator<GameJDO> it = ((List<GameJDO>) query.execute()).iterator();
        while (it.hasNext()) {
            returnProgressDefinitions.add(toBean((GameJDO) it.next()));
        }
        return returnProgressDefinitions;
    }


}
