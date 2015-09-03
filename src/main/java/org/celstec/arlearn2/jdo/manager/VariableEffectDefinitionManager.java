package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.*;

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
public class VariableEffectDefinitionManager {

    private static final String params[] = new String[] { "gameId", "name", };
    private static final String paramsNames[] = new String[] { "gameIdParam", "nameParam"};
    private static final String types[] = new String[] { "Long", "String"};


    public static VariableEffectDefinition createVariableDefinition(VariableEffectDefinition variableDefinition) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableEffectDefinitionJDO variableEffectDefinitionJDO = new VariableEffectDefinitionJDO();
        variableEffectDefinitionJDO.setName(variableDefinition.getName());
        variableEffectDefinitionJDO.setGameId(variableDefinition.getGameId());
        variableEffectDefinitionJDO.setEffectValue(variableDefinition.getEffectValue());
        variableEffectDefinitionJDO.setEffectType(variableDefinition.getEffectType());
        if (variableDefinition.getEffectCount() == null) variableDefinition.setEffectCount(1);
        variableEffectDefinitionJDO.setEffectCount(variableDefinition.getEffectCount());
        if (variableDefinition.getId() != null) variableEffectDefinitionJDO.setIdentifier(variableDefinition.getId());
        JsonBeanSerialiser jbs = new JsonBeanSerialiser(variableDefinition.getDependsOn());
        if (variableDefinition.getDependsOn() != null) variableEffectDefinitionJDO.setDependsOn(new Text(jbs.serialiseToJson().toString()));

        try {
            pm.makePersistent(variableEffectDefinitionJDO);
            return toBean(variableEffectDefinitionJDO);
        } finally {
            pm.close();
        }
    }

    public static VariableEffectDefinition getEffectDefinition(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Key key = KeyFactory.createKey(VariableEffectDefinitionJDO.class.getSimpleName(), id);
        return toBean(pm.getObjectById(VariableEffectDefinitionJDO.class, key));

    }

    private static VariableEffectDefinition toBean(VariableEffectDefinitionJDO jdo) {
        if (jdo == null)
            return null;
        VariableEffectDefinition bean = new VariableEffectDefinition();
        bean.setGameId(jdo.getGameId());
        bean.setName(jdo.getName());
        bean.setId(jdo.getIdentifier());
        bean.setEffectValue(jdo.getEffectValue());
        bean.setEffectType(jdo.getEffectType());
        bean.setEffectCount(jdo.getEffectCount());
        try {
            if (jdo.getDependsOn() != null) {
                JsonBeanDeserializer jbd = new JsonBeanDeserializer(jdo.getDependsOn().getValue());
                bean.setDependsOn((Dependency) jbd.deserialize(Dependency.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;

    }

    public static List<VariableEffectDefinition> getVariableEffectDefinitions(Long gameId,  String name) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ArrayList<VariableEffectDefinition> returnScoreDefinitions = new ArrayList<VariableEffectDefinition>();
             Iterator<VariableEffectDefinitionJDO> it = getVariableEffectDefinitions(pm, gameId, name).iterator();
            while (it.hasNext()) {
                returnScoreDefinitions.add(toBean(it.next()));
            }
            return returnScoreDefinitions;
        } finally {
            pm.close();
        }
    }
    public static List<VariableEffectDefinitionJDO> getVariableEffectDefinitions(PersistenceManager pm, Long gameId, String name) {
            Query query = pm.newQuery(VariableEffectDefinitionJDO.class);
            Object args[] = { gameId, name };
            if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
//                query.setFilter("deleted == null");
                return (List<VariableEffectDefinitionJDO>) query.execute();
            }
            query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
            query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
            return ((List<VariableEffectDefinitionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
        }
}
