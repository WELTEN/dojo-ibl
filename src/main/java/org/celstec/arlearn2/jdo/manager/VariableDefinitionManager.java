package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VariableDefinitionJDO;
import org.celstec.arlearn2.jdo.classes.VariableEffectDefinitionJDO;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */

public class VariableDefinitionManager {

    private static final String params[] = new String[] { "gameId", "name", "scope" };
    private static final String paramsNames[] = new String[] { "gameIdParam", "nameParam", "scopeName"};
    private static final String types[] = new String[] { "Long", "String", "Integer"};

	public static VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        if (variableDefinition.getScope() == null) variableDefinition.setScope(0);
        VariableDefinitionJDO variableDefinitionJDO = new VariableDefinitionJDO();
        variableDefinitionJDO.setName(variableDefinition.getName());
        variableDefinitionJDO.setGameId(variableDefinition.getGameId());
        variableDefinitionJDO.setMinValue(variableDefinition.getMinValue());
        variableDefinitionJDO.setMaxValue(variableDefinition.getMaxValue());
        variableDefinitionJDO.setStartValue(variableDefinition.getStartValue());
        variableDefinitionJDO.setScope(variableDefinition.getScope());

        variableDefinitionJDO.setUniqueId();
        try {
            pm.makePersistent(variableDefinitionJDO);
            return toBean(variableDefinitionJDO);
        } finally {
            pm.close();
        }
	}

    private static VariableDefinition toBean(VariableDefinitionJDO jdo) {
        if (jdo == null)
            return null;
        VariableDefinition bean = new VariableDefinition();
        bean.setName(jdo.getName());
        if (jdo.getMinValue() != null) bean.setMinValue(jdo.getMinValue());
        if (jdo.getMaxValue() != null) bean.setMaxValue(jdo.getMaxValue());
        if (jdo.getStartValue() != null) bean.setStartValue(jdo.getStartValue());
        bean.setScope(jdo.getScope());
        bean.setGameId(jdo.getGameId());
        return bean;
    }
    public static List<VariableDefinition> getVariableDefinitions(Long gameId,  String name, Integer scope) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ArrayList<VariableDefinition> returnScoreDefinitions = new ArrayList<VariableDefinition>();
            Iterator<VariableDefinitionJDO> it = getVariableDefinitions(pm, gameId, name, scope).iterator();
            while (it.hasNext()) {
                returnScoreDefinitions.add(toBean(it.next()));
            }
            return returnScoreDefinitions;
        } finally {
            pm.close();
        }
    }

    public static List<VariableDefinitionJDO> getVariableDefinitions(PersistenceManager pm, Long gameId, String name, Integer scope) {
        Query query = pm.newQuery(VariableDefinitionJDO.class);
        Object args[] = { gameId, name, scope };
        if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
//                query.setFilter("deleted == null");
            return (List<VariableDefinitionJDO>) query.execute();
        }
        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
        return ((List<VariableDefinitionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
    }
}
