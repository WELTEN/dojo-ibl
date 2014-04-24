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

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.ScoreDefinition;

import net.sf.jsr107cache.Cache;

public class ScoreDefinitionCache {

	private static ScoreDefinitionCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ScoreDefinitionCache.class
			.getName());

	private ScoreDefinitionCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ScoreDefinitionCache getInstance() {
		if (instance == null)
			instance = new ScoreDefinitionCache();
		return instance;
	}
	
	private static String SCOREDEFINITION_PREFIX = "ScoreDefinition";

	public ScoreDefinition getScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		return (ScoreDefinition) cache.get(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}

	public void putScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType, ScoreDefinition sd) {
		cache.put(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType), sd);
	}
	
	public void removeScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		cache.remove(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}
	
	public int getScoreDefinitionTableIdForGame(String authToken, Long gameId) {
		Integer returnInt = (Integer) cache.get(SCOREDEFINITION_PREFIX+authToken+gameId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putScoreDefinitionTableIdForGame(String authToken, Long gameId, Integer tableId) {
		cache.put(SCOREDEFINITION_PREFIX+authToken+gameId, tableId);
	}
	
	
	public ScoreDefinition getScoreDefinition(Long gameId, String actionId) {
		return (ScoreDefinition) cache.get(SCOREDEFINITION_PREFIX+gameId+actionId);
	}
	
	public void putScoreDefinition(Long gameId, String actionId, ScoreDefinition sd) {
		cache.put(SCOREDEFINITION_PREFIX+gameId+actionId, sd);
	}
	


}
