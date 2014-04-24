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

import java.util.List;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.cache.ScoreDefinitionCache;
import org.celstec.arlearn2.jdo.manager.ScoreDefinitionManager;


public class ScoreDefinitionDelegator extends GoogleDelegator {

	public ScoreDefinitionDelegator(String authToken)  {
		super(authToken);
	}

	public ScoreDefinitionDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public ScoreDefinition createScoreDefinition(ScoreDefinition sd) {
		if (sd.getGameId() == null) {
			sd.setError("No game identifier specified");
			return sd;
		}
		ScoreDefinitionManager.addScoreDefinition(sd.getAction(), sd.getGeneralItemId(), sd.getGeneralItemType(), sd.getGameId(), sd.getScope(), sd.getValue());
		ScoreDefinitionCache.getInstance().removeScoreDefinitonList(sd.getGameId());
		return sd;
	}
	
	public List<ScoreDefinition> getScoreDefinitionsList(Long gameId, String action, Long generalItemId, String generalItemType) {
		List<ScoreDefinition> sdList = ScoreDefinitionCache.getInstance().getScoreDefinitionList(gameId, action, generalItemId, generalItemType);
		if (sdList == null) {
			sdList = ScoreDefinitionManager.getScoreDefinitions(gameId, action, generalItemId, generalItemType);
			ScoreDefinitionCache.getInstance().putScoreDefinitionList(sdList, gameId, action, generalItemId, generalItemType);
		}
		return sdList;
	}

	public ScoreDefinition getScoreDefinitionForAction(Long gameId, Action action) {
		List<ScoreDefinition> list = getScoreDefinitionsList(gameId, action.getAction(), action.getGeneralItemId(), action.getGeneralItemType());
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	public void deleteScoreDefinition(Long gameId) {
		List<ScoreDefinition> list = getScoreDefinitionsList(gameId, null, null, null);
		for (ScoreDefinition sd : list) {
			deleteScoreDefinition(sd);
		}
		
	}

	private void deleteScoreDefinition(ScoreDefinition sd) {
		ScoreDefinitionManager.deleteScoreDefinition(sd.getId());
		ScoreDefinitionCache.getInstance().removeScoreDefinitonList(sd.getGameId());
	}
	
}
