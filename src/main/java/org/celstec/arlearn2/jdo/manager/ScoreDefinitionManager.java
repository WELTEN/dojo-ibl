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
package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ProgressDefinitionJDO;
import org.celstec.arlearn2.jdo.classes.ScoreDefinitionJDO;

import com.google.appengine.api.datastore.KeyFactory;

public class ScoreDefinitionManager {
	
	private static final String params[] = new String[]{"gameId", "action", "generalItemId", "generalItemType"};
	private static final String paramsNames[] = new String[]{"gameParam", "actionParam", "generalItemIdParam", "generalItemTypeParam"};
	private static final String types[] = new String[]{"Long", "String", "Long", "String"};
	
	public static void addScoreDefinition(String action, String generalItemId, String generalItemType, Long gameId, String scope, long value) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ScoreDefinitionJDO scoreDefinition = new ScoreDefinitionJDO();
		scoreDefinition.setAction(action);
		scoreDefinition.setGeneralItemId(generalItemId);
		scoreDefinition.setGameId(gameId);
		scoreDefinition.setGeneralItemType(generalItemType);
		scoreDefinition.setScope(scope);
		scoreDefinition.setValue(value);
		scoreDefinition.setScoreId();
		try {
			pm.makePersistent(scoreDefinition);
		} finally {
			pm.close();
		}
	}
	
	/*
	 * Queries Score Definitions using the only the parameters that are not null.
	 * At least one parameter must be not null
	 * 
	 */
	public static List<ScoreDefinition> getScoreDefinitions(Long gameId, String action, Long generalItemId, String generalItemType) {
		ArrayList<ScoreDefinition> returnScoreDefinitions = new ArrayList<ScoreDefinition>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ScoreDefinitionJDO.class);
		Object[] args = new Object[]{gameId, action, generalItemId, generalItemType};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		Iterator<ScoreDefinitionJDO> it = ((List<ScoreDefinitionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args))).iterator();
		while (it.hasNext()) {
			returnScoreDefinitions.add(toBean((ScoreDefinitionJDO) it.next()));
		}
		return returnScoreDefinitions;

	}
	
	private static ScoreDefinition toBean(ScoreDefinitionJDO jdo) {
		if (jdo == null) return null;
		ScoreDefinition scoreDefinitionBean = new ScoreDefinition();
		scoreDefinitionBean.setGameId(jdo.getGameId());
		scoreDefinitionBean.setAction(jdo.getAction());
		
		scoreDefinitionBean.setGeneralItemId(jdo.getGeneralItemId());
		scoreDefinitionBean.setGeneralItemType(jdo.getGeneralItemType());
		scoreDefinitionBean.setScope(jdo.getScope());
		scoreDefinitionBean.setValue(jdo.getValue());
		scoreDefinitionBean.setId(jdo.getScoreId());
		return scoreDefinitionBean;
	}

	public static void deleteScoreDefinition(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ScoreDefinitionJDO jdo = (ScoreDefinitionJDO) pm.getObjectById(ScoreDefinitionJDO.class, KeyFactory.createKey(ScoreDefinitionJDO.class.getSimpleName(), id));
			pm.deletePersistent(jdo);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pm.close();
		}
		
	}
	
}
