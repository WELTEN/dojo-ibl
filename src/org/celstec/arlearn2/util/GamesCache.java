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

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import net.sf.jsr107cache.Cache;

public class GamesCache {

	private static GamesCache instance;
	private Cache cache;

	private GamesCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static GamesCache getInstance() {
		if (instance == null)
			instance = new GamesCache();
		return instance;
	}
	private static String GAMES_PREFIX = "Games";

//	public void removeGames(String authToken) {
//		cache.remove(GAMES_PREFIX+authToken);
//	}
//	
//	public GamesList getGames(String authToken) {
//		return (GamesList) cache.get(GAMES_PREFIX+authToken);
//	}
//	
//	public void putGames(String authToken, GamesList gl) {
//		cache.put(GAMES_PREFIX+authToken, gl);
//	}
//	
//	public Game getGame(String authToken, long gameId) {
//		return (Game) cache.get(GAMES_PREFIX+gameId+authToken);
//	}
//	
//	public void putGame(String authToken, long gameId, Game g) {
//		cache.put(GAMES_PREFIX+gameId+authToken, g);
//	}
}
