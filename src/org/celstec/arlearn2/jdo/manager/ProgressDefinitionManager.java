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

import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ProgressDefinitionJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;

import com.google.appengine.api.datastore.KeyFactory;

public class ProgressDefinitionManager {
	
	private static final String params[] = new String[]{"gameId", "action", "generalItemId", "generalItemType"};
	private static final String paramsNames[] = new String[]{"gameParam", "actionParam", "generalItemIdParam", "generalItemTypeParam"};
	private static final String types[] = new String[]{"Long", "String", "Long", "String"};


	public static void addProgressDefinition(String action, String generalItemId, String generalItemType, Long gameId, String scope) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ProgressDefinitionJDO progressDefinition = new ProgressDefinitionJDO();
		progressDefinition.setAction(action);
		progressDefinition.setGeneralItemId(generalItemId);
		progressDefinition.setGameId(gameId);
		progressDefinition.setGeneralItemType(generalItemType);
		progressDefinition.setScope(scope);
		progressDefinition.setProgressId();
		try {
			pm.makePersistent(progressDefinition);
		} finally {
			pm.close();
		}
	}

	public static List<ProgressDefinition> getProgressDefinitions(Long gameId, String action, Long generalItemId, String generalItemType) {
		ArrayList<ProgressDefinition> returnProgressDefinitions = new ArrayList<ProgressDefinition>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ProgressDefinitionJDO.class);
		Object args [] ={gameId, action, generalItemId, generalItemType};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		Iterator<ProgressDefinitionJDO> it = ((List<ProgressDefinitionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args))).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((ProgressDefinitionJDO) it.next()));
		}
		return returnProgressDefinitions;

	}
	
	private static ProgressDefinition toBean(ProgressDefinitionJDO jdo) {
		if (jdo == null) return null;
		ProgressDefinition pd = new ProgressDefinition();
		pd.setId(jdo.getProgressId());
		pd.setAction(jdo.getAction());
		pd.setGameId(jdo.getGameId());
		pd.setGeneralItemId(jdo.getGeneralItemId());
		pd.setGeneralItemType(jdo.getGeneralItemType());
		pd.setScope(jdo.getScope());
		return pd;
	}

	public static void deleteProgressDefinition(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ProgressDefinitionJDO jdo = (ProgressDefinitionJDO) pm.getObjectById(ProgressDefinitionJDO.class, KeyFactory.createKey(ProgressDefinitionJDO.class.getSimpleName(), id));
			pm.deletePersistent(jdo);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pm.close();
		}
		
	}

	
}
