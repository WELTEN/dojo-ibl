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
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.cache.ProgressDefinitionCache;
import org.celstec.arlearn2.jdo.manager.ProgressDefinitionManager;

import java.util.List;


public class ProgressDefinitionDelegator extends GoogleDelegator {

    public ProgressDefinitionDelegator(String authToken) {
        super(authToken);
    }

    public ProgressDefinitionDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public ProgressDefinition createProgressDefinition(ProgressDefinition pd) {
        if (pd.getGameId() == null) {
            pd.setError("No gameId identifier specified");
            return pd;
        }
        ProgressDefinitionManager.addProgressDefinition(pd.getAction(), pd.getGeneralItemId(), pd.getGeneralItemType(), pd.getGameId(), pd.getScope());
        ProgressDefinitionCache.getInstance().removeProgressDefinitonList(pd.getGameId());
        return pd;
    }

    public List<ProgressDefinition> getProgressDefinitionsList(Long gameId, Action action) {
        String actionString = null;
        Long generalItemId = null;
        String generalItemType = null;
        if (action != null) {
            actionString = action.getAction();
            generalItemId = action.getGeneralItemId();
            generalItemType = action.getGeneralItemType();
        }
        List<ProgressDefinition> pdList = ProgressDefinitionCache.getInstance().getProgressDefinitionList(gameId, actionString, generalItemId, generalItemType);
        if (pdList == null) {
            pdList = ProgressDefinitionManager.getProgressDefinitions(gameId, actionString, generalItemId, generalItemType);
            ProgressDefinitionCache.getInstance().putProgressDefinitionList(pdList, gameId, actionString, generalItemId, generalItemType);
        }
        return pdList;
    }

    public ProgressDefinition getProgressDefinitionsForAction(Long gameId, Action action) {
        List<ProgressDefinition> list = getProgressDefinitionsList(gameId, action);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public Long getMaxUserProgressCount(Long gameId) {
        // TODO work out
        System.out.println("check if this method is called");
        Long result = 0l;
        return result;
    }

    public Long getMaxTeamProgressCount(Long gameId) {
        // TODO work out
        System.out.println("check if this method is called");
        Long result = 0l;
        return result;
    }

    public Long getMaxAllProgressCount(Long gameId) {
        System.out.println("check if this method is called");
        Long result = 0l;
        return result;
    }

    public void deleteProgressDefinition(Long gameId) {
        List<ProgressDefinition> list = getProgressDefinitionsList(gameId, null);
        for (ProgressDefinition pd : list) {
            deleteProgressDefinition(pd);
        }

    }

    private void deleteProgressDefinition(ProgressDefinition pd) {
        ProgressDefinitionManager.deleteProgressDefinition(pd.getId());
        ProgressDefinitionCache.getInstance().removeProgressDefinitonList(pd.getGameId());
    }

}
