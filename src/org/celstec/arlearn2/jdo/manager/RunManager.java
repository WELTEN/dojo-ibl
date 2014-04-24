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

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.codehaus.jettison.json.JSONException;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Text;

public class RunManager {

	private static final String params[] = new String[] { "id", "gameId", "owner", "title", "tagId" };
	private static final String paramsNames[] = new String[] { "runParam", "gameParam", "ownerEmailParam", "titleParam", "tagIdParam" };
	private static final String types[] = new String[] { "Long", "Long", "String", "String", "String" };

	public static Long addRun(String title, String owner, Long gameId, Long runId, Long startTime, Long serverCreationTime, Run run) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunJDO runJdo = new RunJDO();
		runJdo.setGameId(gameId);
		runJdo.setRunId(runId);
		runJdo.setOwner(owner);
		runJdo.setTitle(title);
		runJdo.setStartTime(startTime);
		runJdo.setServerCreationTime(serverCreationTime);
		runJdo.setLastModificationDate(serverCreationTime);
		runJdo.setPayload(new Text(run.toString()));
		if (run.getRunConfig() != null) {
			runJdo.setTagId(run.getRunConfig().getNfcTag());
		}
		try {
			return pm.makePersistent(runJdo).getRunId();
		} finally {
			pm.close();
		}
	}
	
	public static Long addRun(Run run) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunJDO runJdo = new RunJDO();
		runJdo.setGameId(run.getGameId());
		runJdo.setRunId(run.getRunId());
		runJdo.setTitle(run.getTitle());
		runJdo.setOwner(run.getOwner());
		runJdo.setStartTime(run.getStartTime());
		runJdo.setServerCreationTime(run.getServerCreationTime());
		runJdo.setLastModificationDate(run.getServerCreationTime());
		runJdo.setPayload(new Text(run.toString()));
		if (run.getRunConfig() != null) {
			runJdo.setTagId(run.getRunConfig().getNfcTag());
		}
		try {
			return pm.makePersistent(runJdo).getRunId();
		} finally {
			pm.close();
		}
	}

	public static List<RunJDO> getRuns(PersistenceManager pm, Long runId, Long gameId, String owner, String title, String tagId) {
		Query query = pm.newQuery(RunJDO.class);
		Object args[] = { runId, gameId, owner, title, tagId };
		if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
			// query.setFilter("deleted == null");
			return (List<RunJDO>) query.execute();
		}
		String filter = ManagerUtil.generateFilter(args, params, paramsNames);
		String paramDecl = ManagerUtil.generateDeclareParameters(args, types, params, paramsNames);
		query.setFilter(filter);
		query.declareParameters(paramDecl);
		return (List<RunJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}

	public static List<Run> getRuns(Long runId, Long gameId, String owner, String title, String tagId) {
		ArrayList<Run> returnRuns = new ArrayList<Run>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<RunJDO> it = getRuns(pm, runId, gameId, owner, title, tagId).iterator();
			while (it.hasNext()) {
				returnRuns.add(toBean((RunJDO) it.next()));
			}
			return returnRuns;
		} finally {
			pm.close();
		}

	}

	public static void deleteRun(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> runsToDelete = getRuns(pm, runId, null, null, null, null);
			pm.deletePersistentAll(runsToDelete);
		} finally {
			pm.close();
		}

	}
	
	public static void deleteRun(PersistenceManager pm, RunJDO runJDO) {
		pm.deletePersistent(runJDO);
		
	}

	public static void setStatusDeleted(long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo : deleteList) {
				jdo.setDeleted(true);
				jdo.setLastModificationDate(System.currentTimeMillis());
			}
		} finally {
			pm.close();
		}
	}

	public static void setLastModificationDate(long runId, long timestamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo : deleteList) {
				jdo.setLastModificationDate(timestamp);
			}
		} finally {
			pm.close();
		}
	}

	public static void updateRun(long runId, Run run) {
		run.setGame(null);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> updateList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo : updateList) {
				jdo.setPayload(new Text(run.toString()));
				jdo.setLastModificationDate(System.currentTimeMillis());
				jdo.setTitle(run.getTitle());
				jdo.setGameId(run.getGameId());
				jdo.setOwner(run.getOwner());
				jdo.setStartTime(run.getStartTime());
				jdo.setServerCreationTime(run.getServerCreationTime());
				jdo.setDeleted(false);
				if (run.getRunConfig() != null) {
					jdo.setTagId(run.getRunConfig().getNfcTag());
				}
			}
		} finally {
			pm.close();
		}
	}

	private static Run toBean(RunJDO jdo) {
		if (jdo == null)
			return null;
		Run run;
		if (jdo.getPayload() != null) {
			try {
				run = (Run) JsonBeanDeserializer.deserialize(jdo.getPayload().getValue());
			} catch (JSONException e) {
				run = new Run();
			}
		} else {
			run = new Run();
		}
		run.setRunId(jdo.getRunId());
		run.setTitle(jdo.getTitle());
		run.setGameId(jdo.getGameId());
		run.setOwner(jdo.getOwner());
		run.setTagId(jdo.getTagId());
		run.setStartTime(jdo.getStartTime());
		run.setDeleted(jdo.getDeleted());
		run.setServerCreationTime(jdo.getServerCreationTime());
		run.setLastModificationDate(jdo.getLastModificationDate());
		return run;
	}

	private final static int LIMIT = 10;

	public static List<RunJDO> listAllRuns(PersistenceManager pm, String cursorString) {
		javax.jdo.Query query = pm.newQuery(RunJDO.class);
		if (cursorString != null) {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
		query.setRange(0, LIMIT);
		return (List<RunJDO>) query.execute();
	}
	
	private static String cursorString = null;
	public static void updateAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		Query query = pm.newQuery(RunJDO.class);
		if (cursorString != null) {

			Cursor c = Cursor.fromWebSafeString(cursorString);
			Map<String, Object> extendsionMap = new HashMap<String, Object>();
			extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
			query.setExtensions(extendsionMap);
		}
		query.setRange(0, 100);

		
//		query.setFilter("lastModificationDate == null");
		List<RunJDO> results = (List<RunJDO>) query.execute();
		Iterator<RunJDO> it = (results).iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			RunJDO object = it.next();
			if (object != null &&object.getLastModificationDate() == null) {
				object.setLastModificationDate(System.currentTimeMillis());

			}
		}
		Cursor c = JDOCursorHelper.getCursor(results);
		cursorString = c.toWebSafeString();
		} finally {
			pm.close();
		}
	}

	

	

}
