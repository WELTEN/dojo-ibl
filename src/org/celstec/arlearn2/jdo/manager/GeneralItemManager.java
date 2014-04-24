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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GeneralItemJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class GeneralItemManager {

	
	private static final String params[] = new String[]{"gameId", "id", "type"};
	private static final String paramsNames[] = new String[]{"gameParam", "generalItemIdParam", "typeParam"};
	private static final String types[] = new String[]{"Long", "com.google.appengine.api.datastore.Key", "String"};


	public static void addGeneralItem(GeneralItem bean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GeneralItemJDO gi = new GeneralItemJDO();
		gi.setGameId(bean.getGameId());
		if (bean.getId() != null) gi.setIdentifier(bean.getId());
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean.getDependsOn());
		if (bean.getDependsOn() != null) gi.setDependsOn(new Text(jbs.serialiseToJson().toString()));
		if (bean.getDescription() == null) bean.setDescription("");
		gi.setDescription(new Text(bean.getDescription()));
		gi.setScope(bean.getScope());
		gi.setLat(bean.getLat());
		gi.setLng(bean.getLng());
		gi.setName(bean.getName());
		gi.setRadius(bean.getRadius());
//		gi.setShowAtTimeStamp(bean.getShowAtTimeStamp());
		gi.setType(bean.getType());
		gi.setIconUrl(bean.getIconUrl());
		gi.setDeleted(false);
		gi.setLastModificationDate(System.currentTimeMillis());
		jbs = new JsonBeanSerialiser(bean);
		gi.setPayload(new Text(jbs.serialiseToJson().toString()));
		try {
			pm.makePersistent(gi);
			bean.setId(gi.getIdentier());
		} finally {
			pm.close();
		}
	}

    public static GeneralItem getGeneralItem(Long itemId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return toBean(pm.getObjectById(GeneralItemJDO.class, KeyFactory
                    .createKey(GeneralItemJDO.class.getSimpleName(), itemId)));
        } catch (Exception e) {
           return null;
        } finally {
            pm.close();
        }
    }

	public static List<GeneralItem> getGeneralitems(Long gameId, String generalItemId, String type) {
		ArrayList<GeneralItem> returnProgressDefinitions = new ArrayList<GeneralItem>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<GeneralItemJDO> it = getGeneralitems(pm, gameId, generalItemId, type).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((GeneralItemJDO) it.next()));
		}
		return returnProgressDefinitions;
		
	}
	
	public static List<GeneralItem> getGeneralitemsFromUntil(Long gameId, Long from, Long until) {
		ArrayList<GeneralItem> returnProgressDefinitions = new ArrayList<GeneralItem>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(GeneralItemJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "gameId == gameParam & lastModificationDate <= untilParam";
			params = "Long gameParam, Long untilParam";
			args = new Object[]{gameId, until};
		} else if (until == null) {
			filter = "gameId == gameParam & lastModificationDate >= fromParam";
			params = "Long gameParam, Long fromParam";
			args = new Object[]{gameId, from};
		} else {
			filter = "gameId == gameParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
			params = "Long gameParam, Long fromParam, Long untilParam";
			args = new Object[]{gameId, from, until};
		}
		
		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<GeneralItemJDO> it = ((List<GeneralItemJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((GeneralItemJDO) it.next()));
		}
		return returnProgressDefinitions;
	}	
	
	private static List<GeneralItemJDO> getGeneralitems(PersistenceManager pm, Long gameId, String generalItemId, String type) {
		Query query = pm.newQuery(GeneralItemJDO.class);
		Object args [] ={gameId, generalItemId!=null?KeyFactory.createKey(GeneralItemJDO.class.getSimpleName(), Long.parseLong(generalItemId)):null, type};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<GeneralItemJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	private static GeneralItem toBean(GeneralItemJDO jdo) {
		if (jdo == null) return null;
		if (jdo.getType() == null) return null;
		JsonBeanDeserializer jbd;
		GeneralItem gi = new GeneralItem();


		Class artifactClass;
		try {
			jbd = new JsonBeanDeserializer(jdo.getPayload().getValue());
			gi = (GeneralItem) jbd.deserialize(Class.forName(jdo.getType()));
//			artifactClass = Class.forName(jdo.getType());
//			gi = (GeneralItem) artifactClass.getConstructor(String.class).newInstance(jdo.getPayload().getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			if (jdo.getDependsOn() != null) {
				jbd = new JsonBeanDeserializer(jdo.getDependsOn().getValue());
				gi.setDependsOn((Dependency) jbd.deserialize(Dependency.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		gi.setDescription(jdo.getDescription().getValue());
		gi.setId(jdo.getIdentier());
		gi.setLat(jdo.getLat());
		gi.setLng(jdo.getLng());
		gi.setGameId(jdo.getGameId());
		gi.setName(jdo.getName());
		gi.setRadius(jdo.getRadius());
		gi.setScope(jdo.getScope());
//		gi.setShowAtTimeStamp(jdo.getShowAtTimeStamp());
		gi.setType(jdo.getType());
		gi.setIconUrl(jdo.getIconUrl());
		gi.setDeleted(jdo.getDeleted());
		gi.setLastModificationDate(jdo.getLastModificationDate());
		return gi;
	}

	public static void deleteGeneralItem(long gameId) {
		delete(gameId, null, null);
	}
	
	public static GeneralItem setStatusDeleted(long gameId, String itemId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<GeneralItemJDO> deleteList = getGeneralitems(pm, gameId, itemId, null);
			for (GeneralItemJDO jdo: deleteList) {
				jdo.setDeleted(true);
				jdo.setLastModificationDate(System.currentTimeMillis());
				return toBean(jdo);
			}
		} finally {
			pm.close();
		}
		return null;
	}
	
	public static void deleteGeneralItem(long gameId, String itemId) {
		delete(gameId, itemId, null);
	}
	
	private static void delete(long gameId, String generalItemId, String type){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<GeneralItemJDO> deleteList = getGeneralitems(pm, gameId, generalItemId, type);
			pm.deletePersistentAll(deleteList);
		} finally {
			pm.close();
		}
	}
//	private static String cursorString = null;
//
//	public static void updateAll() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//		Query query = pm.newQuery(GeneralItemJDO.class);
//		if (cursorString != null) {
//
//			Cursor c = Cursor.fromWebSafeString(cursorString);
//			Map<String, Object> extendsionMap = new HashMap<String, Object>();
//			extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
//			query.setExtensions(extendsionMap);
//		}
//		query.setRange(0, 100);
//
//		
////		query.setFilter("lastModificationDate == null");
//		List<GeneralItemJDO> results = (List<GeneralItemJDO>) query.execute();
//		Iterator<GeneralItemJDO> it = (results).iterator();
//		int i = 0;
//		while (it.hasNext()) {
//			i++;
//			GeneralItemJDO object = it.next();
//			if (object != null &&object.getLastModificationDate() == null) {
//				object.setLastModificationDate(System.currentTimeMillis());
//
//			}
//		}
//		Cursor c = JDOCursorHelper.getCursor(results);
//		cursorString = c.toWebSafeString();
//		} finally {
//			pm.close();
//		}		
//	}

}
