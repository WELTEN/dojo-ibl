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
 * Contributors: Stefaan Ternier, Roland Klemke
 ******************************************************************************/
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.cache.VariableCache;
import org.celstec.arlearn2.cache.cacheableobjects.GameVariablesCollector;
import org.celstec.arlearn2.jdo.classes.VariableEffectInstanceJDO;
import org.celstec.arlearn2.jdo.manager.VariableDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableEffectDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableEffectInstanceManager;
import org.celstec.arlearn2.jdo.manager.VariableInstanceManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import java.util.*;

public class VariableDelegator extends DependencyDelegator {

	public VariableDelegator(Service service) {
		super(service);
	}

    public VariableDelegator(GenericBean bean) {
        super(bean);
    }

    public VariableDelegator(GoogleDelegator googleDelegator) {
        super(googleDelegator);
    }

    public VariableDelegator(Account account, String token) {
        super(account, token);
    }

    public VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        if (variableDefinition.getGameId() == null) {
            variableDefinition.setError("gameId missing");
            return variableDefinition;
        } else {
            //use gameId to empty cache
            VariableCache.getInstance().removeGameVariablesCollector(variableDefinition.getGameId());
            return VariableDefinitionManager.createVariableDefinition(variableDefinition);
        }
	}

    public VariableEffectDefinition createVariableEffectDefinition(VariableEffectDefinition variableDef) {
        VariableCache.getInstance().removeGameVariablesCollector(variableDef.getGameId());

        return VariableEffectDefinitionManager.createVariableDefinition(variableDef);
    }

    public VariableInstance createVariableInstance(VariableInstance variableInstance) {
        if (variableInstance.getGameId() == null) {
            variableInstance.setError("gameId missing");
        } else if (variableInstance.getRunId() == null) {
            variableInstance.setError("runId missing");
        }else if (variableInstance.getName() == null) {
            variableInstance.setError("name missing");
        } else {
            VariableInstanceManager.createVariableInstance(variableInstance);
        }
        return  variableInstance;
    }

    public VariableInstance getVariableInstance(Long gameId, Long runId, String name) {
        VariableInstance returnValue = VariableInstanceManager.getVariableInstance(gameId, runId, name, account.getFullId(), null);
        if (returnValue == null) {
            UsersDelegator td = new UsersDelegator(this);
            User u = td.getUserByEmail(runId, account.getFullId());
            if (u != null) {
                returnValue = VariableInstanceManager.getVariableInstance(gameId, runId, name, null, u.getTeamId());
            }

        }
        if (returnValue == null) {
            returnValue = VariableInstanceManager.getVariableInstance(gameId, runId, name, null, null);
        }
        if (returnValue == null) {
            returnValue = new VariableInstance();
            returnValue.setError("no such variable instance");
        }
        return returnValue;
    }

    public List<VariableEffectDefinition> getVariableEffectDefinitions(Long gameId){
        GameVariablesCollector gameVariablesCollector = VariableCache.getInstance().getGameVariablesCollector(gameId);
        if (gameVariablesCollector == null) {
            gameVariablesCollector = initializeGameVariableCollector(gameId);
        }
        return gameVariablesCollector.getVariableEffectDefinitions();
        //return VariableEffectDefinitionManager.getVariableEffectDefinitions(gameId, null);
    }

    public VariableEffectDefinition getVariableEffectDefinition(Long id){
        return  VariableEffectDefinitionManager.getEffectDefinition(id);
    }

    public HashMap<String, VariableDefinition> getVariableDefinitions(Long gameId, int scope) {
        GameVariablesCollector gameVariablesCollector = VariableCache.getInstance().getGameVariablesCollector(gameId);
        if (gameVariablesCollector == null) {
            gameVariablesCollector = initializeGameVariableCollector(gameId);
        }
        return gameVariablesCollector.getVariableDefinitions(scope);
    }

    public List<VariableDefinition> getVariableDefinitionsList(Long gameId, int scope) {
        GameVariablesCollector gameVariablesCollector = VariableCache.getInstance().getGameVariablesCollector(gameId);
        if (gameVariablesCollector == null) {
            gameVariablesCollector = initializeGameVariableCollector(gameId);
        }
        return gameVariablesCollector.getVariableDefinitionsList(scope);
    }

    public VariableDefinition getVariableDefinition(Long gameId, String name) {
        GameVariablesCollector gameVariablesCollector = VariableCache.getInstance().getGameVariablesCollector(gameId);
        if (gameVariablesCollector == null) {
            gameVariablesCollector = initializeGameVariableCollector(gameId);
        }
        return gameVariablesCollector.getVariableDefinition(name);
    }

    public List<VariableEffectDefinition> getVariableEffectDefinitions(Long gameId, int scope){
        GameVariablesCollector gameVariablesCollector = VariableCache.getInstance().getGameVariablesCollector(gameId);
        if (gameVariablesCollector == null) {
            gameVariablesCollector = initializeGameVariableCollector(gameId);
        }
        //HashMap<String, VariableDefinition> stringVariableDefinitionMap = getVariableDefinitions(gameId, scope);
        HashMap<String, VariableDefinition> stringVariableDefinitionMap = gameVariablesCollector.getVariableDefinitions(scope);
        //List<VariableEffectDefinition> variableEffectDefinitionList = getVariableEffectDefinitions(gameId);
        List<VariableEffectDefinition> variableEffectDefinitionList = gameVariablesCollector.getVariableEffectDefinitions();
        Iterator<VariableEffectDefinition> iter = variableEffectDefinitionList.iterator();
        while (iter.hasNext()) {
            if (!stringVariableDefinitionMap.containsKey(iter.next().getName())) {
                iter.remove();
            }
        }
        return variableEffectDefinitionList;
    }

//    public List<VariableEffectDefinition> getVariableEffectDefinitionsForTeam(Long gameId){
//        HashMap<String, VariableDefinition> stringVariableDefinitionMap = getVariableDefinitions(gameId, 1);
//        List<VariableEffectDefinition> variableEffectDefinitionList = getVariableEffectDefinitions(gameId);
//        Iterator<VariableEffectDefinition> iter = variableEffectDefinitionList.iterator();
//        while (iter.hasNext()) {
//            if (!stringVariableDefinitionMap.containsKey(iter.next().getName())) {
//                iter.remove();
//            }
//        }
//        return variableEffectDefinitionList;
//    }


    /**
     * initializes a GameVariablesCollector for the given gameId.
     * Stores the variableDefinitions and variableEffectDefinitions into it.
     * Caches the whole thing and returns it.
     *
     * @param gameId
     * @return
     */
    private GameVariablesCollector initializeGameVariableCollector(Long gameId) {
        GameVariablesCollector gameVariablesCollector = new GameVariablesCollector();
        List<VariableDefinition> variableDefinitions = VariableDefinitionManager.getVariableDefinitions(gameId, null, null);
        List<VariableEffectDefinition> effectDefinitions = VariableEffectDefinitionManager.getVariableEffectDefinitions(gameId, null);

        gameVariablesCollector.setVariableDefinitions(variableDefinitions);
        gameVariablesCollector.setVariableEffectDefinitions(effectDefinitions);

        VariableCache.getInstance().removeGameVariablesCollector(gameId);
        VariableCache.getInstance().putGameVariablesCollector(gameVariablesCollector, gameId);

        return gameVariablesCollector;
    }




    public void checkActionEffect(Action action, long runId, User u) {
        RunDelegator qr = new RunDelegator(this);
        Run run = qr.getRun(action.getRunId());

        UsersDelegator ud = new UsersDelegator(this);
        HashMap<String, User> uMap = ud.getUserMap(u.getRunId());

        ActionDelegator qa = new ActionDelegator(this);
        ActionList al1 = qa.getActionList(runId);
        ActionList al = new ActionList();
        al.setRunId(runId);

        ActionRelevancyPredictorForVariables arp = ActionRelevancyPredictorForVariables.getActionRelevancyPredictorForVariables(run.getGameId(), this);

        List<VariableEffectDefinition> vedList = arp.getRelevantVariableEffectDefinitions(action);
        System.out.println("relevant: "+vedList);

        VariableDefinition vd = null;
        //VariableInstance vi = null;
        //VariableEffectInstanceJDO vei = null;
        List<VariableInstance> varInstances = null;
        List<VariableEffectInstanceJDO> varEffectInstances = null;

        User user = null;

        Iterator<Action> iterator = al1.getActions().iterator();
        if (iterator != null) {
            //System.out.println("AC:  "+action);
            while (iterator.hasNext()) {
                Action ac = iterator.next();
                if (ac.getAction().equals(action.getAction()) && ac.getGeneralItemId().equals(action.getGeneralItemId())) {
                    if (ac.getUserEmail().equals(action.getUserEmail()) && ac.getTime().equals(action.getTime())) {
                        al.addAction(ac);
                        //System.out.println("IN1: "+ac);
                    } else {
                        //System.out.println("OUT: "+ac);
                    }
                } else {
                    al.addAction(ac);
                    //System.out.println("IN2: "+ac);
                }
            }
        }


        if (vedList != null) {
            for (VariableEffectDefinition ved: vedList) {
                if (influencedBy(ved.getDependsOn(), action)) {
                    //System.out.println("check ved: "+ved);
                    // first find the relevant variable definition belonging to ved
                    vd = getVariableDefinition(ved.getGameId(), ved.getName());
                    if (vd != null) {
                        //System.out.println("check vd: "+vd.getName());
                        varEffectInstances = VariableEffectInstanceManager.getVariableEffectInstances(null, runId, null, null, ved.getId());
                        varInstances = VariableInstanceManager.getVariableInstances(ved.getGameId(), runId, vd.getName(), null, null);

                        if (vd.getScope() == Dependency.USER_SCOPE) {
                            for (VariableInstance vi: varInstances) {
                                user = uMap.get(vi.getAccount());
                                //System.out.println("USER-SCOPE: check user: "+user.getName());
                                if (checkActions(ved.getDependsOn(), al, user, uMap) != -1) {
                                    for (VariableEffectInstanceJDO vei: varEffectInstances) {
                                        //System.out.println("check action vei: "+vei);
                                        if (vei.getAccount().equals(vi.getAccount())) {
                                            System.out.println("USER: "+user.getName()+", "+vi.getName()+", "+vei.getAccount());
                                            applyActionEffect(vd, vi, ved, vei, ud);
                                        }
                                    }
                                }
                            }
                        } else if (vd.getScope() == Dependency.TEAM_SCOPE) {
                            for (VariableInstance vi: varInstances) {
                                //System.out.println("TEAM-SCOPE: check user: "+vi.getTeamId());
                                user = null;
                                UserList userList = ud.getUsers(runId, vi.getTeamId());
                                for (User us: userList.getUsers()) {
                                    if  (vi.getTeamId().equals(us.getTeamId())) {
                                        user = us;
                                        if (user != null && checkActions(ved.getDependsOn(), al, user, uMap) != -1) {
                                            for (VariableEffectInstanceJDO vei: varEffectInstances) {
                                                if (vei.getTeamId().equals(vi.getTeamId())) {
                                                    System.out.println("TEAM: "+user.getName()+", "+vi.getName()+", "+vei.getTeamId());
                                                    applyActionEffect(vd, vi, ved, vei, ud);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (vd.getScope() == Dependency.ALL_SCOPE) {
                            if (checkActions(ved.getDependsOn(), al, u, uMap) != -1) {
                                for (VariableInstance vi: varInstances) {
                                    if (checkActions(ved.getDependsOn(), al, u, uMap) != -1) {
                                        for (VariableEffectInstanceJDO vei: varEffectInstances) {
                                            if (vei.getVariableEffectDefinitionIdentifier().equals(ved.getId())) {
                                            //if (vei.getAccount().equals(vi.getAccount())) {
                                                System.out.println("GLOBAL: "+u.getName()+", "+vi.getName()+", "+vei.getVariableEffectDefinitionIdentifier());
                                                applyActionEffect(vd, vi, ved, vei, ud);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    protected void applyActionEffect(VariableDefinition vd, VariableInstance vi, VariableEffectDefinition ved, VariableEffectInstanceJDO vei, UsersDelegator ud) {
        if (vd == null || vi == null || ved == null || vei == null) {
            System.out.println("Can't apply effect to variable. vd: "+vd+", vi: "+vi+", ved: "+ved+", vei: "+vei);
            return;
        }
        if (vei.getEffectCount() != null && vei.getEffectCount().intValue() != 0) {
            Long value = vi.getValue();

            // apply effect
            if ("abs".equals(ved.getEffectType())) {
                value = ved.getEffectValue();
            } else if ("add".equals(ved.getEffectType())) {
                value = value + ved.getEffectValue();
            } else if ("sub".equals(ved.getEffectType())) {
                value = value - ved.getEffectValue();
            } else if ("multi".equals(ved.getEffectType())) {
                value = value * ved.getEffectValue();
            } else if ("div".equals(ved.getEffectType())) {
                value = value / ved.getEffectValue();
            } else {
                System.out.println("Unknown effect type: "+ved.getEffectType());
                return;
            }

            // check boundaries
            if (vd.getMaxValue() != null && value > vd.getMaxValue()) {
                value = vd.getMaxValue();
            } else if (vd.getMinValue() != null && value < vd.getMinValue()) {
                value = vd.getMinValue();
            }

            // update variable instance
            vi.setValue(value);

            // save the instance
            createVariableInstance(vi);

            // inform all users involved with the update
            UserList userList = null;
            if (vd.getScope() == Dependency.USER_SCOPE) {
                User u = ud.getUserByEmail(vi.getRunId(), vi.getAccount());
                userList = new UserList();
                userList.addUser(u);
            } else if (vd.getScope() == Dependency.TEAM_SCOPE) {
                userList = ud.getUsers(vi.getRunId(), vi.getTeamId());
            } else if (vd.getScope() == Dependency.ALL_SCOPE) {
                userList = ud.getUsers(vi.getRunId());
            }
            for (User u: userList.getUsers()) {
                new NotificationDelegator(this).broadcast(vi, u.getFullId());
            }

            // update effect instance
            if (vei.getEffectCount() > 0) {
                vei.setEffectCount(vei.getEffectCount()-1);
                VariableEffectInstanceManager.updateVariableEffectInstance(vei);
            }

            System.out.println("Applied effect: "+vd.getName()+", "+ved.getEffectType()+"("+ved.getEffectValue()+")="+vi.getValue()+", "+vei.getEffectCount());
        }
    }

}