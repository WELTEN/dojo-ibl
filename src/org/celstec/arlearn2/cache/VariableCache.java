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
 * Contributors: Roland Klemke
 ******************************************************************************/
package org.celstec.arlearn2.cache;

import com.google.appengine.api.utils.SystemProperty;
import org.celstec.arlearn2.cache.cacheableobjects.GameVariablesCollector;

/**
 * Created with IntelliJ IDEA.
 * User: klemke
 * Date: 23.07.13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class VariableCache extends GameCache {

    private static VariableCache instance;

    private static String GAMEVARIABLES_PREFIX = SystemProperty.applicationVersion.get()+"GameVariablesCollector";

    private VariableCache() {
    }

    public static VariableCache getInstance() {
        if (instance == null)
            instance = new VariableCache();
        return instance;

    }

    public GameVariablesCollector getGameVariablesCollector(Long gameId, Object... args) {
        String cacheKey = generateCacheKey(GAMEVARIABLES_PREFIX, gameId, args);
        if (!cacheKeyExists(gameId, cacheKey)) return null;
        return (GameVariablesCollector) getCache().get(generateCacheKey(GAMEVARIABLES_PREFIX, gameId, args));
    }

    public void putGameVariablesCollector(GameVariablesCollector gameVariablesCollector, Long gameId, Object... args) {
        String cachekey = generateCacheKey(GAMEVARIABLES_PREFIX, gameId, args);
        storeCacheKey(gameId, cachekey);
        getCache().put(generateCacheKey(GAMEVARIABLES_PREFIX, gameId, args), gameVariablesCollector);
    }

    public void removeGameVariablesCollector(Long gameId) {
        removeKeysForGame(gameId);
    }


    @Override
    protected String getType() {
        return "GameVariablesCollector";
    }

}
