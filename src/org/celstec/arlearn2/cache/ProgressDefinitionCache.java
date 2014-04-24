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

import org.celstec.arlearn2.beans.game.ProgressDefinition;

import com.google.appengine.api.utils.SystemProperty;

public class ProgressDefinitionCache extends GameCache{

	private static ProgressDefinitionCache instance;

	private static final Logger logger = Logger.getLogger(ProgressDefinitionCache.class.getName());
	private static String PROGRESSDEFINITION_PREFIX = SystemProperty.applicationVersion.get()+"ProgressDefinition";

	private ProgressDefinitionCache() {
	}

	public static ProgressDefinitionCache getInstance() {
		if (instance == null)
			instance = new ProgressDefinitionCache();
		return instance;

	}
	
	public List<ProgressDefinition> getProgressDefinitionList(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(PROGRESSDEFINITION_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (List<ProgressDefinition>) getCache().get(cacheKey);
	}
	
	public void putProgressDefinitionList(List<ProgressDefinition> pdList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(PROGRESSDEFINITION_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(cachekey, pdList);
	}
	
	public void removeProgressDefinitonList(Long gameId) {
		removeKeysForGame(gameId);
	}


	@Override
	protected String getType() {
		return "ProgressDefinitionCache";
	}
}
