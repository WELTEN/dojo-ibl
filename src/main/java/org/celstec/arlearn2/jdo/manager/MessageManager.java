package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.MessageList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.MessageJDO;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.*;

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
public class MessageManager {

    private static final String params[] = new String[] { "threadId", "runId" };
    private static final String paramsNames[] = new String[] { "threadIdParam", "runIdParam" };
    private static final String types[] = new String[] { "Long", "Long" };

    private static final int MESSAGES_IN_LIST = 20;


    public static Message createMessage(Message message) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        MessageJDO messageJDO = new MessageJDO();
        messageJDO.setDeleted(message.getDeleted());
        messageJDO.setThreadId(message.getThreadId());
        messageJDO.setMessageId(message.getMessageId());
        messageJDO.setSubject(message.getSubject());
        messageJDO.setBody(message.getBody());
        messageJDO.setDate(message.getDate());
        messageJDO.setRunId(message.getRunId());
        messageJDO.setSenderId(message.getSenderId());
        messageJDO.setSenderProviderId(message.getSenderProviderId());
        messageJDO.setLastModificationDate(System.currentTimeMillis());
        try {
            pm.makePersistent(messageJDO);
            return toBean(messageJDO);
        } finally {
            pm.close();
        }
    }

    public static MessageList getMessagesByThreadId(long threadId) {
        MessageList ml = new MessageList();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Iterator<MessageJDO> it = getMessages(pm, threadId, null).iterator();
        while (it.hasNext()) {
            ml.addMessage(toBean(it.next()));
        }
        ml.setServerTime(System.currentTimeMillis());
        return ml;
    }

    private static Message toBean(MessageJDO jdo) {
        if (jdo == null) return null;
        Message bean = new Message();
        bean.setThreadId(jdo.getThreadId());
        bean.setRunId(jdo.getRunId());
        bean.setSubject(jdo.getSubject());
        bean.setBody(jdo.getBody());
        bean.setMessageId(jdo.getMessageId());
        bean.setDate(jdo.getDate());
        bean.setDeleted(jdo.getDeleted());
        bean.setSenderId(jdo.getSenderId());
        bean.setSenderProviderId(jdo.getSenderProviderId());
        return bean;
    }

    private static List<MessageJDO> getMessages(PersistenceManager pm, Long threadId, Long runId) {
        Query query = pm.newQuery(MessageJDO.class);
        query.setOrdering("lastModificationDate asc");
        Object args[] = {threadId, runId};
        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
        return (List<MessageJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));

    }

    public static MessageList getMessagesByThreadId(long threadId, Long from, Long until, String cursorString) {
        MessageList returnList = new MessageList();
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            Query query = pm.newQuery(MessageJDO.class);
            query.setOrdering("lastModificationDate asc");
            if (cursorString != null) {

                Cursor c = Cursor.fromWebSafeString(cursorString);
                Map<String, Object> extensionMap = new HashMap<String, Object>();
                extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
                query.setExtensions(extensionMap);
            }
            query.setRange(0, MESSAGES_IN_LIST);
            String filter = null;
            String params = null;
            Object args[] = null;
            if (from == null) {
                filter = "threadId == threadIdParam & lastModificationDate <= untilParam";
                params = "Long threadIdParam, Long untilParam";
                args = new Object[] { threadId, until };
            } else if (until == null) {
                filter = "threadId == threadIdParam & lastModificationDate >= fromParam";
                params = "Long threadIdParam, Long fromParam";
                args = new Object[] { threadId, from };
            } else {
                filter = "threadId == threadIdParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
                params = "Long threadIdParam, Long fromParam, Long untilParam";
                args = new Object[] { threadId, from, until };
            }
            query.setFilter(filter);
            query.declareParameters(params);
            List<MessageJDO> results = (List<MessageJDO>) query.executeWithArray(args);
            Iterator<MessageJDO> it = (results).iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                MessageJDO object = it.next();
                returnList.addMessage(toBean(object));

            }
            Cursor c = JDOCursorHelper.getCursor(results);
            cursorString = c.toWebSafeString();
            if (returnList.getMessages().size() == MESSAGES_IN_LIST) {
                returnList.setResumptionToken(cursorString);
            }
            returnList.setServerTime(System.currentTimeMillis());

        }finally {
            pm.close();
        }
        return returnList;
    }

    public static void updateDate(Long identifier) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        MessageJDO messageJDO = pm.getObjectById(MessageJDO.class, KeyFactory.createKey(MessageJDO.class.getSimpleName(), identifier));
        System.out.println("to update message "+messageJDO);

    }

    public static Message getMessage(Long identifier) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        MessageJDO messageJDO = pm.getObjectById(MessageJDO.class, KeyFactory.createKey(MessageJDO.class.getSimpleName(), identifier));
        return toBean(messageJDO);
    }

    public static MessageList getRecentMessagesByThreadId(Long threadId, int amount) {
        MessageList returnList = new MessageList();
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            Query query = pm.newQuery(MessageJDO.class);
            query.setOrdering("lastModificationDate desc");

            query.setRange(0, amount);
            String filter = null;
            String params = null;
            Object args[] = null;

                filter = "threadId == threadIdParam";
                params = "Long threadIdParam";
                args = new Object[] { threadId};

            query.setFilter(filter);
            query.declareParameters(params);
            List<MessageJDO> results = (List<MessageJDO>) query.executeWithArray(args);
            Iterator<MessageJDO> it = (results).iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                MessageJDO object = it.next();
                returnList.addMessage(toBean(object));

            }
            returnList.setServerTime(System.currentTimeMillis());

        }finally {
            pm.close();
        }
        return returnList;
    }
}
