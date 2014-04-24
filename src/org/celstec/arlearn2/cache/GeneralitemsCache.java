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

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.delegators.ActionRelevancyPredictor;

import com.google.appengine.api.utils.SystemProperty;

public class GeneralitemsCache extends GameCache {
	private static GeneralitemsCache instance;

	private static String GENERALITEM_PREFIX = SystemProperty.applicationVersion.get()+"GeneralItemDefinition";
	private static String ACTION_RELEVANCY_PREFIX = SystemProperty.applicationVersion.get()+"ActionRelevancy";

	private GeneralitemsCache() {
	}

	public static GeneralitemsCache getInstance() {
		if (instance == null)
			instance = new GeneralitemsCache();
		return instance;

	}
	
	public GeneralItemList getGeneralitems(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(GENERALITEM_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (GeneralItemList) getCache().get(generateCacheKey(GENERALITEM_PREFIX, gameId, args));
	}
	
	public void putGeneralItemList(GeneralItemList giList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(GENERALITEM_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(GENERALITEM_PREFIX, gameId, args), giList);
	}
	
	public ActionRelevancyPredictor getActionsPredicator(Long gameId) {
		String cacheKey = generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (ActionRelevancyPredictor) getCache().get(generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId));
	}
	
	public void putActionsPredicator(ActionRelevancyPredictor predictor, Long gameId) {
		String cachekey = generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId), predictor);
	}
	
	public void removeGeneralItemList(Long gameId) {
		removeKeysForGame(gameId);
	}
	
	@Override
	protected String getType() {
		return "GeneralItems";
	}

}
