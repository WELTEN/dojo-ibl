package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.notification.MessageNotification;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.codehaus.jettison.json.JSONException;

import java.util.logging.Logger;

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
public class NotifyUsersForMessage extends GenericBean {

    private String message;
    private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

    public NotifyUsersForMessage() {

    }

    public NotifyUsersForMessage(String token, Message message) {
        super(token);
        if (message != null) this.message = message.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            log.severe("about to broadcast  ");

            Message messageBean = (Message) JsonBeanDeserializer.deserialize(message);
            NotificationDelegator nd = new NotificationDelegator(getToken());
            log.severe("messageBean  " +messageBean.getTeamIds());
            log.severe("messageBean  " +messageBean.getUserIds());
            log.severe("condition  " +(messageBean.getTeamIds() == null & messageBean.getUserIds() == null));
            if (messageBean.getTeamIds() == null & messageBean.getUserIds() == null) {


                UsersDelegator ud = new UsersDelegator(getToken());
                UserList ul = ud.getUsers(messageBean.getRunId());
                MessageNotification messagesNotification = new MessageNotification(messageBean.getRunId(), messageBean.getThreadId(), messageBean.getMessageId());
                for (User u : ul.getUsers()) {
                    log.severe("user  " +u.getFullId());
                    nd.broadcast(messagesNotification, u.getFullId());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}