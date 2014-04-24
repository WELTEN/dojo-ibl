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

import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.util.FusionCache;

import com.google.appengine.api.utils.SystemProperty;

public class ScoreDefinitionCache extends GameCache{

	private static ScoreDefinitionCache instance;

	private static final Logger logger = Logger.getLogger(UsersCache.class.getName());
	private static String SCOREDEFINITION_PREFIX = SystemProperty.applicationVersion.get()+"ScoreDefinition";

	private ScoreDefinitionCache() {
	}

	public static ScoreDefinitionCache getInstance() {
		if (instance == null)
			instance = new ScoreDefinitionCache();
		return instance;

	}
	
	public List<ScoreDefinition> getScoreDefinitionList(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (List<ScoreDefinition>) getCache().get(generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args));
	}
	
	public void putScoreDefinitionList(List<ScoreDefinition> scList, Long gameId, Object... args) {
		String cachekey = FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args), scList);
	}
	
	public void removeScoreDefinitonList(Long gameId) {
		removeKeysForGame(gameId);
	}


	@Override
	protected String getType() {
		return "ScoreDefinitionCache";
	}
}
