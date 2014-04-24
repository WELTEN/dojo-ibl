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

import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ProgressDefinition;

import com.google.appengine.api.utils.SystemProperty;


public class MyGamesCache extends GenericCache{
	private static MyGamesCache instance;

	private static final Logger logger = Logger.getLogger(MyGamesCache.class.getName());
	private static String MYGAMES_PREFIX = SystemProperty.applicationVersion.get()+"MyGamesCache";

	private MyGamesCache() {
	}

	public static MyGamesCache getInstance() {
		if (instance == null)
			instance = new MyGamesCache();
		return instance;

	}

    public void putFeaturedGameList(List<Game> gameList) {
        getCache().put(MYGAMES_PREFIX+"_featured", gameList);
    }

    public List<Game> getFeaturedGameList() {
        return (List<Game>) getCache().get(MYGAMES_PREFIX+"_featured");
    }

	public List<Game> getGameList(Long gameId, Object... args) {
		return (List<Game>) getCache().get(generateCacheKey(MYGAMES_PREFIX, gameId, args));
	}

	public void putGameList(List<Game> gameList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(MYGAMES_PREFIX, gameId, args); 
		getCache().put(cachekey, gameList);
	}
	
	public void removeGameList(Long gameId, Object... args) {
		removeGame(gameId);
		String cachekey = generateCacheKey(MYGAMES_PREFIX, gameId, args); 
		getCache().remove(cachekey);
	}
	
	public Game getGame(Long gameId) {
		return (Game) getCache().get(generateCacheKey(MYGAMES_PREFIX, gameId));
	}
	
	public void putGame(Game game, Long gameId) {
		String cachekey = generateCacheKey(MYGAMES_PREFIX, gameId); 
		getCache().put(cachekey, game);
	}
	
	public void removeGame(Long gameId) {
		getCache().remove(generateCacheKey(MYGAMES_PREFIX, gameId));
	}
	
}
