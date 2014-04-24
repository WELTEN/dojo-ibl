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

import java.util.Collections;
import java.util.Map;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

import java.util.logging.Logger;


public class FusionCache {
	
	private static final Logger logger = Logger.getLogger(FusionCache.class.getName());

	private static FusionCache instance;
	private Cache cache;
	
	private FusionCache(){
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	logger.severe(e.getMessage());
        }
	}
	
	public static FusionCache getInstance() {
		if (instance == null) instance = new FusionCache();
		return instance;
	}
	
	public Cache getCache() {
		return cache;
	}
	
	public void clearAll() {
		cache.clear();
	}
	
	private static String MY_GAMES_TABLE_ID_PREFIX = "mygamestid";
	private static String GAME_DATA_TABLES_ID = "gamedatatablesid";

	public void putMyGamesTableId(String authToken, Integer id) {
		cache.put(MY_GAMES_TABLE_ID_PREFIX+authToken, id);
	}
	
	public Integer getMyGamesTableId(String authToken) {
		return (Integer) cache.get(MY_GAMES_TABLE_ID_PREFIX+authToken);
	}
	
	public void putGameDataTableIds(String authToken, Map<String, Integer> map) {
		//System.out.println("FusionCache.putGameDataTableIds: "+map+", "+authToken);
		cache.put(GAME_DATA_TABLES_ID+authToken, map);
	}
	
	public Map<String, Integer> getGameDataTableIds(String authToken) {
		//System.out.println("FusionCache.getGameDataTableIds: "+authToken);
		//System.out.println("FusionCache.getGameDataTableIds: "+cache.get(GAME_DATA_TABLES_ID+authToken));
		return (Map<String, Integer>) cache.get(GAME_DATA_TABLES_ID+authToken);
	}
	
	public static String getCacheKey(java.lang.Object... parameters) {
		return getCacheKeyAr(parameters);
	}
	
	protected static String getCacheKeyAr(Object parameters[]) {
		String key = "";
		for (Object p: parameters) {
			if (p == null) {
				key += ":null";
			} else {
				if (p instanceof Object[]) {
					key += getCacheKeyAr((Object[])p);
				} else {
					key += ":"+p.toString();	
				}
					
			}
		}
		return key;
	}
}
