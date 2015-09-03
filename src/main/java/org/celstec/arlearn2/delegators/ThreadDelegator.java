package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.beans.run.ThreadList;
import org.celstec.arlearn2.jdo.manager.ThreadManager;

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
public class ThreadDelegator extends GoogleDelegator {

    public ThreadDelegator(String authToken) {
        super(authToken);
    }

    public ThreadDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public ThreadDelegator(Account account, String token) {
        super(account, token);
    }

    public ThreadDelegator(Service service) {
        super(service);
    }

    public Thread createThread(Thread inThread) {
        if (inThread.getName() == null) {
            inThread.setError("set a name for this thread");
            inThread.setErrorCode(1);
            return inThread;
        }
        if (inThread.getRunId() == null) {
            inThread.setError("set a runId for this thread");
            inThread.setErrorCode(1);
            return inThread;
        }
        return ThreadManager.createThread(inThread, false);
    }


    public ThreadList getThreads(long runId) {
        ThreadList threadList = new ThreadList();
        threadList.setThreads(ThreadManager.getThreads(runId));
        return threadList;
    }

    public Thread getDefaultThread(long runId) {
        Thread thread = ThreadManager.getDefaultThread(runId);
        if (thread == null) {
            thread = new Thread();
            thread.setRunId(runId);
            thread.setName("Default");
            thread = ThreadManager.createThread(thread, true);
        }
        return thread;
    }

}


