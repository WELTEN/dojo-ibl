package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.MessageList;
import org.celstec.arlearn2.jdo.manager.MessageManager;
import org.celstec.arlearn2.jdo.manager.ThreadManager;
import org.celstec.arlearn2.tasks.beans.NotifyUsersForMessage;

import java.util.logging.Logger;

public class MessageDelegator extends GoogleDelegator {

    public MessageDelegator(Service service) {
        super(service);
    }


    public Message sendMessage(Message message, String userId) {
        new NotificationDelegator(this).broadcast(message, account.getFullId());
        return message;
    }

    public Message createMessage(Message message) {
        message.setDate(System.currentTimeMillis());
        Message returnMessage = MessageManager.createMessage(message);
        (new NotifyUsersForMessage(authToken, returnMessage)).scheduleTask();

        return returnMessage;
    }

    public MessageList getMessagesForThread(long threadId) {
        return MessageManager.getMessagesByThreadId(threadId);
    }

    public MessageList getMessagesForThread(long threadId, Long from, Long until, String cursor) {
        return MessageManager.getMessagesByThreadId(threadId, from, until, cursor);
    }


    public MessageList getMessagesForDefaultThread(Long runId) {
        org.celstec.arlearn2.beans.run.Thread thread = ThreadManager.getDefaultThread(runId);
        if (thread == null) thread = new ThreadDelegator(this).getDefaultThread(runId);
        if (thread != null) {
            return getMessagesForThread(thread.getThreadId());
        }
        return null;
    }

    public MessageList getMessagesForDefaultThread(Long runId, Long from, Long until, String cursor) {
        org.celstec.arlearn2.beans.run.Thread thread = ThreadManager.getDefaultThread(runId);
        if (thread == null) thread = new ThreadDelegator(this).getDefaultThread(runId);
        if (thread != null) {
            return getMessagesForThread(thread.getThreadId());
        }
        return null;
    }
}
