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
package org.celstec.arlearn2.tasks.beans.cleanUp;

import java.util.List;

import javax.jdo.PersistenceManager;
import org.datanucleus.store.appengine.query.JDOCursorHelper;
import com.google.appengine.api.datastore.Cursor;

import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.tasks.beans.DeleteActions;
import org.celstec.arlearn2.tasks.beans.DeleteBlobs;
import org.celstec.arlearn2.tasks.beans.DeleteResponses;
import org.celstec.arlearn2.tasks.beans.DeleteTeams;
import org.celstec.arlearn2.tasks.beans.GenericBean;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItemsVisibility;
import org.celstec.arlearn2.util.RunsCache;

public class RunIterator extends GenericBean {

	String cursorString;

	public RunIterator() {
		super();
	}

	public RunIterator(String cursorString) {
		super();
		this.cursorString = cursorString;
	}

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

	@Override
	public void run() {
		if ("*".equals(cursorString)) {
			startIteration(null);
		} else {
			startIteration(cursorString);
		}
	}

//	private void startIteration() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> runs = RunManager.listAllRuns(pm, null);
//			processRuns(runs);
//			rescheduleIteration(runs);
//		} finally {
//			pm.close();
//		}
//	}

	private void rescheduleIteration(List<RunJDO> runs) {
		Cursor cCursor = JDOCursorHelper.getCursor(runs);
		String cursorString = cCursor.toWebSafeString();
		System.out.println("cursorout " + cursorString);
		(new RunIterator(cursorString)).scheduleTask();
	}

	private void startIteration(String cursorString) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> runs = RunManager.listAllRuns(pm, cursorString);
			processRuns(pm, runs);
			if (!runs.isEmpty()) rescheduleIteration(runs);
		} finally {
			pm.close();
		}

	}

	private void processRuns(PersistenceManager pm, List<RunJDO> runs) {
		for (RunJDO runJDO : runs) {
			processrun(pm, runJDO);
		}
	}
	
	private void processrun(PersistenceManager pm, RunJDO runJDO) {
		String authToken = null;
		GameDelegator gd = new GameDelegator();
//		if (gd.getGameWithoutAccount(runJDO.getGameId()) == null) {
        //switched this one off - > dangerous
        if (false) {
			RunManager.setStatusDeleted(runJDO.getRunId());
			RunsCache.getInstance().removeRun(runJDO.getRunId());
			(new UpdateGeneralItemsVisibility(authToken, null, runJDO.getRunId(), null, 2)).scheduleTask();

//			(new DeleteVisibleItems(authToken, r.getRunId())).scheduleTask();
			(new DeleteActions(authToken, null, runJDO.getRunId())).scheduleTask();
			(new DeleteTeams(authToken, null, runJDO.getRunId(), null)).scheduleTask();
			(new DeleteBlobs(authToken, null, runJDO.getRunId())).scheduleTask();
			(new DeleteResponses(authToken, null, runJDO.getRunId())).scheduleTask();
		}
		if (runJDO.getDeleted() != null && runJDO.getDeleted() && (runJDO.getLastModificationDate()+ (2592000000l)) < System.currentTimeMillis()) {
			RunManager.deleteRun(pm, runJDO);
		}

	}
}
