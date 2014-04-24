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
package org.celstec.arlearn2.delegators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.Action;

/**
 * Predicts, if a received action is relevant for a certain set of variable effect definitions.
 * Does so, by checking, if the dependencies of the variable effect definitions contain the action.
 *
 * Created with IntelliJ IDEA.
 * User: klemke
 * Date: 23.07.13
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class ActionRelevancyPredictorForVariables {

    private HashMap<ActionDependency, List<VariableEffectDefinition>> actionDependencies = new HashMap<ActionDependency, List<VariableEffectDefinition>>();

//    private HashMap<String, List<ActionDependency>> roleDependencies = new HashMap<String, List<ActionDependency>>();
//    private List<ActionDependency> userDependencies = new ArrayList<ActionDependency>();
//    private List<ActionDependency> teamDependencies = new ArrayList<ActionDependency>();
//    private List<ActionDependency> allDependencies = new ArrayList<ActionDependency>();
//
//    private List<ActionDependency> dependencies = new ArrayList<ActionDependency>();

    private ActionRelevancyPredictorForVariables() {

    }

    public static ActionRelevancyPredictorForVariables getActionRelevancyPredictorForVariables(long gameId, GoogleDelegator gd) {
        ActionRelevancyPredictorForVariables arp = null; //GeneralitemsCache.getInstance().getActionsPredicator(gameId);
        if (arp == null) {
            VariableDelegator vd = new VariableDelegator(gd);
            arp = new ActionRelevancyPredictorForVariables();
            arp.init(gameId, vd);
            //GeneralitemsCache.getInstance().putActionsPredicator(arp, gameId);
        }
        return arp;
    }

    private void init(long gameId, VariableDelegator vd) {
        for (VariableEffectDefinition ved: vd.getVariableEffectDefinitions(gameId)) {
            init(ved.getDependsOn(), ved);
        }
    }
    private void init(Dependency dep, VariableEffectDefinition ved) {
        if (dep != null) {
            List<ActionDependency> itemDependencies = getActionDependencies(dep);
//            dependencies.addAll(itemDependencies);
            for (ActionDependency aDep: itemDependencies) {
                List<VariableEffectDefinition> variableEffectDefinitionList = null;
                if (actionDependencies.containsKey(aDep)) {
                    variableEffectDefinitionList = actionDependencies.get(aDep);
                } else {
                    variableEffectDefinitionList = new ArrayList<VariableEffectDefinition>();
                    actionDependencies.put(aDep, variableEffectDefinitionList);
                }
                variableEffectDefinitionList.add(ved);

//                if (aDep.getRole() != null) addRoleDependencies(aDep.getRole(), aDep);
//                if (aDep.getScope() != null) {
//                    switch (aDep.getScope()) {
//                        case Dependency.USER_SCOPE:
//                            userDependencies.add(aDep);
//                            break;
//                        case Dependency.TEAM_SCOPE:
//                            teamDependencies.add(aDep);
//                            break;
//                        case Dependency.ALL_SCOPE:
//                            allDependencies.add(aDep);
//                            break;
//                        default:
//                            break;
//                    }
//                }else {
//                    userDependencies.addAll(itemDependencies);
//                }
            }

        }
    }

//    private void addRoleDependencies(String role, ActionDependency adep) {
//        if (!roleDependencies.containsKey(role)) {
//            roleDependencies.put(role, new ArrayList<ActionDependency>());
//        }
//        roleDependencies.get(role).add(adep);
//    }

    private List<ActionDependency> getActionDependencies(ActionDependency dep) {
        ArrayList<ActionDependency> deps = new ArrayList<ActionDependency>();
        deps.add(dep);
        return deps;
    }

    private List<ActionDependency> getActionDependencies(Dependency dep) {
        if (dep instanceof ActionDependency) return getActionDependencies((ActionDependency) dep);
        if (dep instanceof BooleanDependency) return getActionDependencies((BooleanDependency) dep);
        if (dep instanceof TimeDependency) return getActionDependencies((TimeDependency) dep);
        return  new ArrayList<ActionDependency>();
    }

    private List<ActionDependency> getActionDependencies(BooleanDependency depsIn) {
        ArrayList<ActionDependency> deps = new ArrayList<ActionDependency>();
        List<Dependency> depList = depsIn.getDependencies();
        if (depList == null) return deps;
        for (Dependency dep: depsIn.getDependencies()) {
            deps.addAll(getActionDependencies(dep));
        }
        return  deps;
    }

    private List<ActionDependency> getActionDependencies(TimeDependency depsIn) {
        return  getActionDependencies( depsIn.getOffset());
    }

    public String toString() {
        String toReturn = "";
        toReturn += "actionDependencies\n";
        toReturn += actionDependencies+"\n";
//        toReturn += "roleDependencies\n";
//        toReturn += roleDependencies+"\n";
//        toReturn += "userDependencies\n";
//        toReturn += userDependencies+ "\n";
//        toReturn += "teamDependencies\n";
//        toReturn += teamDependencies+"\n";
//        toReturn += "allDependencies\n";
//        toReturn += allDependencies+"\n";
        return toReturn;
    }

//    public boolean isRelevant(Action action) {
//        return isRelevant(action, dependencies);
//    }
//
//    public boolean isRelevantForUser(Action action) {
//        return isRelevant(action, userDependencies);
//    }
//
//    public boolean isRelevantForTeam(Action action) {
//        return isRelevant(action, teamDependencies);
//    }
//
//    public boolean isRelevantForAll(Action action) {
//        return isRelevant(action, allDependencies);
//    }
//

//    private boolean isRelevant(Action action, List<ActionDependency> dependencies) {
//        for (ActionDependency dep: dependencies) {
//            boolean soFar = true;
//            if (dep.getAction() != null && !dep.getAction().equals(action.getAction())) soFar = false;
//            if (dep.getGeneralItemId() != null && !dep.getGeneralItemId().equals(action.getGeneralItemId()))soFar = false;
//            if (dep.getGeneralItemType() != null && !dep.getGeneralItemType().equals(action.getGeneralItemType())) soFar = false;
//            if (soFar) return true;
//        }
//        return false;
//    }


    /**
     * Returns the list of potentially affected VariableEffectDefinitions for a given action.
     * Potentially relevant are those actions, that contain an ActionDependency, which matches
     * the given Action.
     *
     * @param action
     * @return
     */
    public List<VariableEffectDefinition> getRelevantVariableEffectDefinitions(Action action) {
        for (ActionDependency dep: actionDependencies.keySet()) {
            boolean soFar = true;
            if (dep.getAction() != null && !dep.getAction().equals(action.getAction())) soFar = false;
            if (dep.getGeneralItemId() != null && !dep.getGeneralItemId().equals(action.getGeneralItemId()))soFar = false;
            if (dep.getGeneralItemType() != null && !dep.getGeneralItemType().equals(action.getGeneralItemType())) soFar = false;
            if (soFar) return actionDependencies.get(dep);
        }
        return null;
    }

}
