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

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.beans.run.Team;

import net.sf.jsr107cache.Cache;

public class ProgressDefinitionCache {
	private static ProgressDefinitionCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ProgressDefinitionCache.class.getName());

	private ProgressDefinitionCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ProgressDefinitionCache getInstance() {
		if (instance == null)
			instance = new ProgressDefinitionCache();
		return instance;

	}

	private static String PROGRESSDEFINITION_PREFIX = "ProgressDefinition";

	public ProgressDefinition getProgressDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		return (ProgressDefinition) cache.get(FusionCache.getCacheKey(PROGRESSDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}

	public void putProgressDefinition(Long gameId, String action, String generalItemId, String generalItemType, ProgressDefinition pd) {
		cache.put(FusionCache.getCacheKey(PROGRESSDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType), pd);
	}
	
	public void removeProgressDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		cache.remove(FusionCache.getCacheKey(PROGRESSDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}

		

	public Long getMaxProgress(Long gameId, String scope) {
		return (Long) cache.get(PROGRESSDEFINITION_PREFIX + gameId + scope);
	}

	public void putMaxProgress(Long gameId, String scope, Long progress) {
		cache.put(PROGRESSDEFINITION_PREFIX + gameId + scope, progress);
	}

	// TODO delete below this line
	@Deprecated
	public ProgressDefinition getProgressDefinition(Long gameId, String actionId) {
		return (ProgressDefinition) cache.get(PROGRESSDEFINITION_PREFIX + gameId + actionId);
	}

	@Deprecated
	public void putProgressDefinition(Long gameId, ProgressDefinition pd) {
		if (pd.getAction() != null && !pd.getAction().equals("")) {
			cache.put(PROGRESSDEFINITION_PREFIX + gameId + pd.getAction(), pd);
		}

	}

	public int getProgressDefinitionTableIdForGame(String authToken, Long gameId) {
		Integer returnInt = (Integer) cache.get(PROGRESSDEFINITION_PREFIX + authToken + gameId);
		if (returnInt == null)
			returnInt = -1;
		return returnInt;
	}

	public void putProgressDefinitionTableIdForGame(String authToken, Long gameId, Integer tableId) {
		cache.put(PROGRESSDEFINITION_PREFIX + authToken + gameId, tableId);
	}

}
