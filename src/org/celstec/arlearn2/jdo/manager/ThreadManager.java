package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ThreadJDO;
import org.celstec.arlearn2.jdo.classes.UserJDO;

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
public class ThreadManager {

    private static final String params[] = new String[] { "threadId", "runId", "isDefault" };
    private static final String paramsNames[] = new String[] { "threadIdParam", "runIdParam", "isDefaultParam" };
    private static final String types[] = new String[] { "Long", "Long", "Boolean" };

    public static org.celstec.arlearn2.beans.run.Thread createThread(Thread thread, boolean isDefault) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        ThreadJDO threadJDO = new ThreadJDO();
        threadJDO.setName(thread.getName());
        threadJDO.setRunId(thread.getRunId());
        threadJDO.setIsDefault(isDefault);
        threadJDO.setLastModificationDate(System.currentTimeMillis());
        try {
            pm.makePersistent(threadJDO);
            return toBean(threadJDO);
        } finally {
            pm.close();
        }
    }


    private static Thread toBean(ThreadJDO jdo) {
        if (jdo == null)
            return null;
        Thread bean = new Thread();
        bean.setName(jdo.getName());
        bean.setDeleted(jdo.getDeleted());
        bean.setRunId(jdo.getRunId());
        bean.setThreadId(jdo.getThreadId());
        bean.setLastModificationDate(jdo.getLastModificationDate());
        return bean;
    }

    public static List<Thread> getThreads(Long runId) {
        ArrayList<Thread> threadArrayList = new ArrayList<Thread>();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Iterator<ThreadJDO> it = getThreads(pm, null, runId, null).iterator();
        while (it.hasNext()) {
            threadArrayList.add(toBean(it.next()));
        }
        return threadArrayList;
    }

    public static Thread getDefaultThread(Long runId) {
        ArrayList<Thread> threadArrayList = new ArrayList<Thread>();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Iterator<ThreadJDO> it = getThreads(pm, null, runId, true).iterator();
        while (it.hasNext()) {
            return toBean(it.next());
        }
        return null;
    }

    private static List<ThreadJDO> getThreads(PersistenceManager pm, Long threadId, Long runId, Boolean isDefault) {
        Query query = pm.newQuery(ThreadJDO.class);
        Object args[] = {threadId, runId , isDefault};
        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
        return (List<ThreadJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
    }
}
