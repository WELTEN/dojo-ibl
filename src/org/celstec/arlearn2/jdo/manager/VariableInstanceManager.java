package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.jdo.PMF;

import org.celstec.arlearn2.jdo.classes.VariableInstanceJDO;
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
public class VariableInstanceManager {

    private static final String params[] = new String[] { "gameId", "runId", "name", "account", "teamId" };
    private static final String paramsNames[] = new String[] { "gameIdParam", "runIdParam", "nameParam", "accountParam", "teamIdParam" };
    private static final String types[] = new String[] { "Long", "Long", "String", "String", "String" };


    public static VariableInstance createVariableInstance(VariableInstance variableInstance) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableInstanceJDO jdo = new VariableInstanceJDO();
        jdo.setAccount(variableInstance.getAccount());
        jdo.setTeamId(variableInstance.getTeamId());
        jdo.setValue(variableInstance.getValue());
        jdo.setGameId(variableInstance.getGameId());
        jdo.setName(variableInstance.getName());
        jdo.setRunId(variableInstance.getRunId());
        jdo.setUniqueId();


        try {
            pm.makePersistent(jdo);
            return toBean(jdo);
        } finally {
            pm.close();
        }
    }

    public static VariableInstance createVariableInstanceForAccount(String account, long gameId, long runId, String name, long value) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableInstanceJDO jdo = new VariableInstanceJDO();
        jdo.setAccount(account);
        jdo.setGameId(gameId);
        jdo.setRunId(runId);
        jdo.setName(name);
        jdo.setValue(value);
        jdo.setUniqueIdAccount();
        try {
            pm.makePersistent(jdo);
            return toBean(jdo);
        } finally {
            pm.close();
        }
    }

    public static VariableInstance createVariableInstanceForTeam(String teamId, long gameId, long runId, String name, long value) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableInstanceJDO jdo = new VariableInstanceJDO();
        jdo.setTeamId(teamId);
        jdo.setGameId(gameId);
        jdo.setRunId(runId);
        jdo.setName(name);
        jdo.setValue(value);
        jdo.setUniqueIdTeam();
        try {
            pm.makePersistent(jdo);
            return toBean(jdo);
        } finally {
            pm.close();
        }
    }

    public static VariableInstance createVariableInstanceForAll(long gameId, long runId, String name, long value) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableInstanceJDO jdo = new VariableInstanceJDO();
        jdo.setGameId(gameId);
        jdo.setRunId(runId);
        jdo.setName(name);
        jdo.setValue(value);
        jdo.setUniqueIdGlobal();
        try {
            pm.makePersistent(jdo);
            return toBean(jdo);
        } finally {
            pm.close();
        }
    }

    public static void delete(Long gameId, Long runId, String name, String account, String teamId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<VariableInstanceJDO> deleteList = getVariableInstancesJDO(pm, gameId, runId, name, account, teamId);
            pm.deletePersistentAll(deleteList);
        } finally {
            pm.close();
        }
    }

    private static VariableInstance toBean(VariableInstanceJDO jdo) {
        if (jdo == null)
            return null;
        VariableInstance bean = new VariableInstance();
        bean.setName(jdo.getName());
        bean.setGameId(jdo.getGameId());
        bean.setRunId(jdo.getRunId());
        bean.setTeamId(jdo.getTeamId());
        bean.setValue(jdo.getValue());
        bean.setAccount(jdo.getAccount());
        return bean;
    }

    public static VariableInstance getVariableInstance(Long gameId, Long runId, String name, String account, String team) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<VariableInstanceJDO> list = getVariables(pm, gameId, runId, name, account, team);
            if (list.isEmpty()) return null;
            return toBean(list.get(0));
        } finally {
            pm.close();
        }
    }

    public static List<VariableInstanceJDO> getVariableInstancesJDO(PersistenceManager pm, Long gameId,  Long runId, String name, String account, String teamId) {
        boolean closePM = false;
        if (pm == null) {
            pm = PMF.get().getPersistenceManager();
            closePM = true; // makes sure, that pm will only be closed, if not passed from outside.
        }
        try {
            List<VariableInstanceJDO> returnInstances = getVariables(pm, gameId, runId, name, account, teamId);
            return returnInstances;
        } finally {
            if (closePM) {
                pm.close();
            }
        }
    }

    public static List<VariableInstance> getVariableInstances(Long gameId,  Long runId, String name, String account, String teamId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ArrayList<VariableInstance> returnInstances = new ArrayList<VariableInstance>();
            Iterator<VariableInstanceJDO> it = getVariables(pm, gameId, runId, name, account, teamId).iterator();
            while (it.hasNext()) {
                returnInstances.add(toBean(it.next()));
            }
            return returnInstances;
        } finally {
            pm.close();
        }
    }

    public static List<VariableInstanceJDO> getVariables(PersistenceManager pm, Long gameId, Long runId, String name, String account, String teamId) {
        Query query = pm.newQuery(VariableInstanceJDO.class);
        Object args[] = { gameId, runId, name, account, teamId };
        if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
            query.setFilter("deleted == null");
            return (List<VariableInstanceJDO>) query.execute();
        }
        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
        return ((List<VariableInstanceJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
    }
}
