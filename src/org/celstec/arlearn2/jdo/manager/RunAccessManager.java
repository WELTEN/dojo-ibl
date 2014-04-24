package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunAccessJDO;

public class RunAccessManager {
	
	public static RunAccessJDO addRunAccess(String localID, int accountType, long runId, int accesRights) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunAccessJDO runAccess = new RunAccessJDO();
		runAccess.setAccessRights(accesRights);
		runAccess.setLocalId(localID);
		runAccess.setAccountType(accountType);
		runAccess.setRunId(runId);
		runAccess.setUniqueId();
		runAccess.setLastModificationDateRun(System.currentTimeMillis());
		try {
			pm.makePersistent(runAccess);
			return runAccess;
		} finally {
			pm.close();
		}
	}
	
	public static void resetGameAccessLastModificationDate(long runId) {
		long lastModifiation = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(RunAccessJDO.class);
			query.setFilter("runId == "+runId);
			Iterator<RunAccessJDO> it = ((List<RunAccessJDO>) query.execute()).iterator();
			while (it.hasNext()) {
				it.next().setLastModificationDateRun(lastModifiation);
			}
		} finally {
			pm.close();
		}
	}

	public static List<RunAccess> getRunList(int accountType, String localId, Long from, Long until) {
		ArrayList<RunAccess> accessDefinitions = new ArrayList<RunAccess>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(RunAccessJDO.class);
		String filter = null;
		String params = null;
		Object args[] = null;
		if (from == null) {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDateRun <= untilParam";
			params = "String localIdParam, Integer accountTypeParam, Long untilParam";
			args = new Object[] { localId, accountType, until };
		} else if (until == null) {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDateRun >= fromParam";
			params = "String localIdParam, Integer accountTypeParam, Long fromParam";
			args = new Object[] { localId, accountType, from };
		} else {
			filter = "localId == localIdParam && accountType == accountTypeParam & lastModificationDate >= fromParam & lastModificationDateRun <= untilParam";
			params = "String localIdParam, Integer accountTypeParam, Long fromParam, Long untilParam";
			args = new Object[] { localId, accountType, from, until };
		}

		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<RunAccessJDO> it = ((List<RunAccessJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			accessDefinitions.add(toBean((RunAccessJDO) it.next()));
		}
		return accessDefinitions;
	}
	
	public static List<RunAccess> getRunAccessList(long runId) {
		ArrayList<RunAccess> accessDefinitions = new ArrayList<RunAccess>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(RunAccessJDO.class);
			String filter = "runId == runIdParam ";
			String params = "Long runIdParam";
			Object args[] = new Object[] { runId};
		
		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<RunAccessJDO> it = ((List<RunAccessJDO>) query.executeWithArray(args)).iterator();
		while (it.hasNext()) {
			accessDefinitions.add(toBean((RunAccessJDO) it.next()));
		}
		return accessDefinitions;
	}
	
	private static RunAccess toBean(RunAccessJDO jdo) {
		if (jdo == null)
			return null;
		RunAccess runAccess = new RunAccess();
		runAccess.setAccount(jdo.getAccountType()+":"+jdo.getLocalId());
		runAccess.setAccessRights(jdo.getAccessRights());
		runAccess.setRunId(jdo.getRunId());
		return runAccess;
	}

	public static void removeRunAccess(String localID, int accountType, Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			RunAccessJDO jdo = pm.getObjectById(RunAccessJDO.class, accountType+":"+localID+":"+runId);
			pm.deletePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	public static RunAccessJDO getAccessById(String accessId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return pm.getObjectById(RunAccessJDO.class,accessId);
		} finally {
			pm.close();
		}
	}
}
