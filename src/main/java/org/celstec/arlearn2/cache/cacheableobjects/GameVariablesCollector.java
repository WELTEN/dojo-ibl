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
package org.celstec.arlearn2.cache.cacheableobjects;

import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * cacheable class to cache variable definitions and variable effect definitions
 * in one data structure per game.
 */
public class GameVariablesCollector implements Serializable {

    private HashMap<String, VariableDefinition> userVariablesMap = new HashMap<String, VariableDefinition>();
    private HashMap<String, VariableDefinition> teamVariablesMap = new HashMap<String, VariableDefinition>();
    private HashMap<String, VariableDefinition> globalVariablesMap = new HashMap<String, VariableDefinition>();
    private HashMap<String, VariableDefinition> allVariablesMap = new HashMap<String, VariableDefinition>();
    private List<VariableDefinition> userVariablesList = new ArrayList<VariableDefinition>();
    private List<VariableDefinition> teamVariablesList = new ArrayList<VariableDefinition>();
    private List<VariableDefinition> globalVariablesList = new ArrayList<VariableDefinition>();
    private List<VariableDefinition> allVariablesList = new ArrayList<VariableDefinition>();
    private List<VariableEffectDefinition> effectDefinitions = new ArrayList<VariableEffectDefinition>();

    public GameVariablesCollector() {
    }

    public HashMap<String, VariableDefinition> getVariableDefinitions(int scope) {
        switch (scope) {
            case Dependency.USER_SCOPE:
                return userVariablesMap;
            case Dependency.TEAM_SCOPE:
                return teamVariablesMap;
            case Dependency.ALL_SCOPE:
                return globalVariablesMap;
            default:
                return null;
        }
    }

    public List<VariableDefinition> getVariableDefinitionsList(int scope) {
        switch (scope) {
            case Dependency.USER_SCOPE:
                return userVariablesList;
            case Dependency.TEAM_SCOPE:
                return teamVariablesList;
            case Dependency.ALL_SCOPE:
                return globalVariablesList;
            default:
                return null;
        }
    }

    public List<VariableDefinition> getVariableDefinitions() {
        return allVariablesList;
    }

    public VariableDefinition getVariableDefinition(String name, int scope) {
        HashMap<String, VariableDefinition> variableDefinitions = getVariableDefinitions(scope);
        if (variableDefinitions != null) {
            return variableDefinitions.get(name);
        }
        return null;
    }

    public VariableDefinition getVariableDefinition(String name) {
        if (allVariablesMap.containsKey(name)) {
            return allVariablesMap.get(name);
        }
        return null;
    }

    public void setVariableDefinitions(List<VariableDefinition> variableDefinitions) {
        allVariablesList.clear();
        allVariablesMap.clear();
        userVariablesMap.clear();
        userVariablesList.clear();
        teamVariablesMap.clear();
        teamVariablesList.clear();
        globalVariablesMap.clear();
        globalVariablesList.clear();
        for (VariableDefinition variableDefinition : variableDefinitions ) {
            this.addVariableDefinition(variableDefinition);
        }
    }

    public void addVariableDefinition(VariableDefinition variableDefinition) {
        if (variableDefinition != null) {
            allVariablesMap.put(variableDefinition.getName(), variableDefinition);
            allVariablesList.add(variableDefinition);
            switch (variableDefinition.getScope()) {
                case Dependency.USER_SCOPE:
                    userVariablesMap.put(variableDefinition.getName(), variableDefinition);
                    userVariablesList.add(variableDefinition);
                    break;
                case Dependency.TEAM_SCOPE:
                    teamVariablesMap.put(variableDefinition.getName(), variableDefinition);
                    teamVariablesList.add(variableDefinition);
                    break;
                case Dependency.ALL_SCOPE:
                    globalVariablesMap.put(variableDefinition.getName(), variableDefinition);
                    globalVariablesList.add(variableDefinition);
                    break;
                default:
                    break;
            }

        }
    }


    public List<VariableEffectDefinition> getVariableEffectDefinitions() {
        return effectDefinitions;
    }

    public void setVariableEffectDefinitions(List<VariableEffectDefinition> effectDefinitions) {
        this.effectDefinitions = effectDefinitions;
    }

    public void addVariableEffectDefinition(VariableEffectDefinition variableEffectDefinition) {
        if (variableEffectDefinition != null) {
            effectDefinitions.add(variableEffectDefinition);
        }
    }
}
