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

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ResponseJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
//import org.celstec.arlearn2.jdo.classes.UserJDO;

public class ResponseManager {

	public static final String params[] = new String[]{"runId",  "generalItemId", "userEmail", "timeStamp", "revoked"};
	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "userEmailParam", "timeStampParam", "revokedParam"};
	private static final String types[] = new String[]{"Long", "Long", "String", "String", "Long", "Boolean"};

	private static final int RESPONSES_IN_LIST = 20;
	
	public static long addResponse(Long generalItemId, String responseValue, Long runId, String userEmail, Long timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ResponseJDO responseRecord = new ResponseJDO();
		responseRecord.setGeneralItemId(generalItemId);
		responseRecord.setResponseValue(responseValue);
		responseRecord.setRunId(runId);
		responseRecord.setUserEmail(userEmail);
		responseRecord.setTimeStamp(timeStamp);
		responseRecord.setLastModificationDate(System.currentTimeMillis());
		responseRecord.setRevoked(false);
		try {
			pm.makePersistent(responseRecord);
		} finally {
			pm.close();
		}
        return responseRecord.getId();
	}
	
	public static Response revokeResponse(Long runId, Long generalItemId, String userEmail, Long timestamp){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ResponseJDO> respList = getResponse(pm, runId, generalItemId, userEmail, timestamp, false);
			if (respList.isEmpty()) {
				Response r = new Response();
				r.setError("could not retrieve response from database, hence revoking is not possible");
				return r;
			}
			Iterator<ResponseJDO> it = respList.iterator();
			ResponseJDO rjdo = null;
			if (it.hasNext()) {
				rjdo = ((ResponseJDO) it.next());
			}
			rjdo.setLastModificationDate(System.currentTimeMillis());
			rjdo.setRevoked(true);
			return toBean(rjdo);
		} finally {
			pm.close();
		}
	}
	
	public static List<Response> getResponse(Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {
		ArrayList<Response> returnProgressDefinitions = new ArrayList<Response>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<ResponseJDO> it = getResponse(pm, runId, generalItemId, userEmail, timestamp, revoked).iterator();
			while (it.hasNext()) {
				returnProgressDefinitions.add(toBean((ResponseJDO) it.next()));
			}
			return returnProgressDefinitions; 
		} finally {
			pm.close();
		}
	}
	
	public static ResponseList getResponse(Long runId, Long from, Long until, String cursorString) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ResponseList returnList = new ResponseList();
		try {
			Query query = pm.newQuery(ResponseJDO.class);
			if (cursorString != null) {

				Cursor c = Cursor.fromWebSafeString(cursorString);
				Map<String, Object> extendsionMap = new HashMap<String, Object>();
				extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
				query.setExtensions(extendsionMap);
			}
			query.setRange(0, RESPONSES_IN_LIST);
			String filter = null;
			String params = null;
			Object args[] = null;
			if (from == null) {
				filter = "runId == runIdParam & lastModificationDate <= untilParam";
				params = "Long runIdParam, Long untilParam";
				args = new Object[] { runId, until };
			} else if (until == null) {
				filter = "runId == runIdParam & lastModificationDate >= fromParam";
				params = "Long runIdParam, Long fromParam";
				args = new Object[] { runId, from };
			} else {
				filter = "runId == runIdParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
				params = "Long runIdParam, Long fromParam, Long untilParam";
				args = new Object[] { runId, from, until };
			}

			query.setFilter(filter);
			query.declareParameters(params);
//			Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
			List<ResponseJDO> results = (List<ResponseJDO>) query.executeWithArray(args);
			Iterator<ResponseJDO> it = (results).iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				ResponseJDO object = it.next();
				returnList.addResponse(toBean(object));
				
			}
			Cursor c = JDOCursorHelper.getCursor(results);
			cursorString = c.toWebSafeString();
			if (returnList.getResponses().size() == RESPONSES_IN_LIST) {
				returnList.setResumptionToken(cursorString);
			}
			returnList.setServerTime(System.currentTimeMillis());


		}finally {
			pm.close();
		}
		return returnList;
		
	}

    public static ResponseList getResponse(Long runId, Long itemId, Long from, Long until, String cursorString) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        ResponseList returnList = new ResponseList();
        try {
            Query query = pm.newQuery(ResponseJDO.class);
            if (cursorString != null) {

                Cursor c = Cursor.fromWebSafeString(cursorString);
                Map<String, Object> extendsionMap = new HashMap<String, Object>();
                extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
                query.setExtensions(extendsionMap);
            }
            query.setRange(0, RESPONSES_IN_LIST);
            String filter = null;
            String params = null;
            Object args[] = null;
            if (from == null) {
                filter = "runId == runIdParam & generalItemId == itemIdParam & lastModificationDate <= untilParam";
                params = "Long runIdParam, Long untilParam, Long itemIdParam";
                args = new Object[] { runId, until,itemId };
            } else if (until == null) {
                filter = "runId == runIdParam & generalItemId == itemIdParam & lastModificationDate >= fromParam";
                params = "Long runIdParam, Long fromParam, Long itemIdParam";
                args = new Object[] { runId, from ,itemId};
            } else {
                filter = "runId == runIdParam & generalItemId == itemIdParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
                params = "Long runIdParam, Long fromParam, Long untilParam, Long itemIdParam";
                args = new Object[] { runId, from, until ,itemId};
            }

            query.setFilter(filter);
            query.declareParameters(params);
//			Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
            List<ResponseJDO> results = (List<ResponseJDO>) query.executeWithArray(args);
            Iterator<ResponseJDO> it = (results).iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                ResponseJDO object = it.next();
                returnList.addResponse(toBean(object));

            }
            Cursor c = JDOCursorHelper.getCursor(results);
            cursorString = c.toWebSafeString();
            if (returnList.getResponses().size() == RESPONSES_IN_LIST) {
                returnList.setResumptionToken(cursorString);
            }
            returnList.setServerTime(System.currentTimeMillis());


        }finally {
            pm.close();
        }
        return returnList;

    }

    public static ResponseList getResponse(Long runId, Long itemId, String fullId, Long from, Long until, String cursorString) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        ResponseList returnList = new ResponseList();
        try {
            Query query = pm.newQuery(ResponseJDO.class);
            if (cursorString != null) {

                Cursor c = Cursor.fromWebSafeString(cursorString);
                Map<String, Object> extendsionMap = new HashMap<String, Object>();
                extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
                query.setExtensions(extendsionMap);
            }
            query.setRange(0, RESPONSES_IN_LIST);
            String filter = null;
            String params = null;
            Object args[] = null;
            if (from == null) {
                filter = "runId == runIdParam & generalItemId == itemIdParam & userEmail == emailParam & lastModificationDate <= untilParam";
                params = "Long runIdParam, Long untilParam, Long itemIdParam, String emailParam";
                args = new Object[] { runId, until,itemId, fullId };
            } else if (until == null) {
                filter = "runId == runIdParam & generalItemId == itemIdParam & userEmail == emailParam & lastModificationDate >= fromParam";
                params = "Long runIdParam, Long fromParam, Long itemIdParam, String emailParam";
                args = new Object[] { runId, from ,itemId, fullId};
            } else {
                filter = "runId == runIdParam & generalItemId == itemIdParam & userEmail == emailParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
                params = "Long runIdParam, Long fromParam, Long untilParam, Long itemIdParam, String emailParam";
                args = new Object[] { runId, from, until ,itemId, fullId};
            }

            query.setFilter(filter);
            query.declareParameters(params);
//			Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
            List<ResponseJDO> results = (List<ResponseJDO>) query.executeWithArray(args);
            Iterator<ResponseJDO> it = (results).iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                ResponseJDO object = it.next();
                returnList.addResponse(toBean(object));

            }
            Cursor c = JDOCursorHelper.getCursor(results);
            cursorString = c.toWebSafeString();
            if (returnList.getResponses().size() == RESPONSES_IN_LIST) {
                returnList.setResumptionToken(cursorString);
            }
            returnList.setServerTime(System.currentTimeMillis());


        }finally {
            pm.close();
        }
        return returnList;

    }
	
	private static List<ResponseJDO> getResponse(PersistenceManager pm, Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {
		Query query = pm.newQuery(ResponseJDO.class);
		Object args [] = new Object[]{runId, generalItemId, userEmail, timestamp, revoked};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<ResponseJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}
	
	private static Response toBean(ResponseJDO jdo) {
		if (jdo == null) return null;
		Response pd = new Response();
        pd.setResponseId(jdo.getId());
		pd.setRunId(jdo.getRunId());
		pd.setGeneralItemId(jdo.getGeneralItemId());
		pd.setTimestamp(jdo.getTimeStamp());
		pd.setUserEmail(jdo.getUserEmail());
		pd.setResponseValue(jdo.getResponseValue());
		return pd;
	}

	public static void deleteResponses(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ResponseJDO>  list = getResponse(pm, runId, null, email, null, null);
			pm.deletePersistentAll(list);
		} finally {
			pm.close();
		}
	}
	
	public static void updateAccount(String accountFrom, String toAccount, Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ResponseJDO.class);
			String filter = null;
			String params = null;
			Object args[] = null;
			filter = "runId == runIdParam & userEmail == emailParam";
			params = "Long runIdParam, String emailParam";
			args = new Object[] { runId, accountFrom };

			query.setFilter(filter);
			query.declareParameters(params);
			Iterator<ResponseJDO> it = ((List<ResponseJDO>) query
					.executeWithArray(args)).iterator();
			while (it.hasNext()) {
				it.next().setUserEmail(toAccount);
			}
		} finally {
			pm.close();
		}
		
	}
}
