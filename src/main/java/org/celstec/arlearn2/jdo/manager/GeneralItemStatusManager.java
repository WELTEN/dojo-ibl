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

import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.GeneralItemsStatus;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GeneralItemStatusJDO;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralItemStatusManager {


    private static final String params[] = new String[]{"runId", "generalItemId", "status"};
    private static final String paramsNames[] = new String[]{"runParam", "generalItemIdParam", "statusParam"};
    private static final String types[] = new String[]{"Long", "Long", "Integer"};


    public static GeneralItemsStatus addGeneralItemStatus(GeneralItemsStatus generalItemsStatus ) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        GeneralItemStatusJDO gis = new GeneralItemStatusJDO();

        gis.setRunId(generalItemsStatus.getRunId());
        gis.setGeneralItemId(generalItemsStatus.getGeneralItemId());
        gis.setStatus(generalItemsStatus.getStatus());
        gis.setServerCreationTime(System.currentTimeMillis());

        try {
            gis.setIdentifier(pm.makePersistent(gis).getGeneralItemStatusId());
            return toBean(gis);
        } finally {
            pm.close();
        }
    }

    public static GeneralItemsStatus updateGeneralItemStatus(GeneralItemsStatus generalItemsStatus ) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        GeneralItemStatusJDO gis;
//        gis = getGeneralitemsStatusFromUntil(generalItemsStatus.getRunId(), generalItemsStatus.getGeneralItemId(), 0l, 0l).get(0);
        gis = pm.getObjectById(GeneralItemStatusJDO.class, KeyFactory.createKey(GeneralItemStatusJDO.class.getSimpleName(), generalItemsStatus.getId()));
        gis.setStatus(generalItemsStatus.getStatus());
        gis.setServerCreationTime(System.currentTimeMillis());

        try {
            gis.setIdentifier(pm.makePersistent(gis).getGeneralItemStatusId());
            return toBean(gis);
        } finally {
            pm.close();
        }
    }


    public static GeneralItemsStatus getGeneralItemStatus(Long itemStatusId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return toBean(pm.getObjectById(GeneralItemStatusJDO.class, KeyFactory.createKey(GeneralItemStatusJDO.class.getSimpleName(), itemStatusId)));
        } catch (Exception e) {
            return null;
        } finally {
            pm.close();
        }
    }

    private static GeneralItemsStatus toBean(GeneralItemStatusJDO jdo) {
        if (jdo == null) return null;
        JsonBeanDeserializer jbd;
        GeneralItemsStatus gis = new GeneralItemsStatus();

//		gis.setIdentifier(jdo.getGeneralItemStatusId());
        gis.setGeneralItemId(jdo.getGeneralItemId());
        gis.setRunId(jdo.getRunId());
        gis.setStatus(jdo.getStatus());
        gis.setLastModificationDate(jdo.getLastModificationDate());
        gis.setServerCreationTime(jdo.getServerCreationTime());
        return gis;
    }


    public static List<GeneralItemsStatus> getGeneralitemsStatusFromUntil(Long runId, Long generalItemId, Long from, Long until) {
        ArrayList<GeneralItemsStatus> returnProgressDefinitions = new ArrayList<GeneralItemsStatus>();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(GeneralItemStatusJDO.class);
        String filter = null;
        String params = null;
        Object args[] = null;
        if (from == null) {
            filter = "runId == runParam & generalItemId == generalItemIdParam";
            params = "Long runParam, Long generalItemIdParam";
            args = new Object[]{runId, generalItemId};
        }

        query.setFilter(filter);
        query.declareParameters(params);
        Iterator<GeneralItemStatusJDO> it = ((List<GeneralItemStatusJDO>) query.executeWithArray(args)).iterator();
        while (it.hasNext()) {
            returnProgressDefinitions.add(toBean((GeneralItemStatusJDO) it.next()));
        }
        return returnProgressDefinitions;
    }

//
//	public static List<GeneralItem> getGeneralitems(Long gameId, String generalItemId, String type, String section) {
//		ArrayList<GeneralItem> returnProgressDefinitions = new ArrayList<GeneralItem>();
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Iterator<GeneralItemJDO> it = getGeneralitems(pm, gameId, generalItemId, type, section).iterator();
//		while (it.hasNext()) {
//			returnProgressDefinitions.add(toBean((GeneralItemJDO) it.next()));
//		}
//		return returnProgressDefinitions;
//	}
//
//	public static List<GeneralItem> getGeneralitemsFromUntil(Long gameId, Long from, Long until) {
//		ArrayList<GeneralItem> returnProgressDefinitions = new ArrayList<GeneralItem>();
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Query query = pm.newQuery(GeneralItemJDO.class);
//		String filter = null;
//		String params = null;
//		Object args[] = null;
//		if (from == null) {
//			filter = "gameId == gameParam & lastModificationDate <= untilParam";
//			params = "Long gameParam, Long untilParam";
//			args = new Object[]{gameId, until};
//		} else if (until == null) {
//			filter = "gameId == gameParam & lastModificationDate >= fromParam";
//			params = "Long gameParam, Long fromParam";
//			args = new Object[]{gameId, from};
//		} else {
//			filter = "gameId == gameParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
//			params = "Long gameParam, Long fromParam, Long untilParam";
//			args = new Object[]{gameId, from, until};
//		}
//
//		query.setFilter(filter);
//		query.declareParameters(params);
//		Iterator<GeneralItemJDO> it = ((List<GeneralItemJDO>) query.executeWithArray(args)).iterator();
//		while (it.hasNext()) {
//			returnProgressDefinitions.add(toBean((GeneralItemJDO) it.next()));
//		}
//		return returnProgressDefinitions;
//	}
//
//	private static List<GeneralItemJDO> getGeneralitems(PersistenceManager pm, Long gameId, String generalItemId, String type, String section) {
//		Query query = pm.newQuery(GeneralItemJDO.class);
//		Object args [] ={gameId, generalItemId!=null?KeyFactory.createKey(GeneralItemJDO.class.getSimpleName(), Long.parseLong(generalItemId)):null, type, section};
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//		query.setOrdering("name asc");
//		return (List<GeneralItemJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
//	}
//
//	public static void deleteGeneralItem(long gameId) {
//		delete(gameId, null, null, null);
//	}
//
//	public static GeneralItem setStatusDeleted(long gameId, String itemId) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<GeneralItemJDO> deleteList = getGeneralitems(pm, gameId, itemId, null, null);
//			for (GeneralItemJDO jdo: deleteList) {
//				jdo.setDeleted(true);
//				jdo.setLastModificationDate(System.currentTimeMillis());
//				return toBean(jdo);
//			}
//		} finally {
//			pm.close();
//		}
//		return null;
//	}
//
//	public static void deleteGeneralItem(long gameId, String itemId) {
//		delete(gameId, itemId, null, null);
//	}
//
//	private static void delete(long gameId, String generalItemId, String type, String section){
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<GeneralItemJDO> deleteList = getGeneralitems(pm, gameId, generalItemId, type, null);
//			pm.deletePersistentAll(deleteList);
//		} finally {
//			pm.close();
//		}
//	}
}
