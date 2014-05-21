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
package org.celstec.arlearn2.delegators.notification;

import com.google.appengine.api.xmpp.*;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class Notification {

    private static final Logger log = Logger.getLogger(Notification.class.getName());

    public final static int TEAM = 1;
    public final static int USER = 2;
    public final static int ALL = 3;
    public final static String SCORE = "score";

    private int scope;
    private long runId;
    private String authToken;

    protected String user;
    private XMPPService xmppService;

    public Notification(int scope, long runId, String authToken) {
        this(runId, authToken);
        this.scope = scope;
    }

    public Notification(String scope, long runId, String authToken) {
        this(runId, authToken);
        this.scope = scopeToIntConstant(scope);
    }

    public static int scopeToIntConstant(String scope) {
        if ("team".equalsIgnoreCase(scope)) return TEAM;
        if ("user".equalsIgnoreCase(scope)) return USER;
        return ALL;
    }

    private Notification(long runId, String authToken) {
        this.runId = runId;
        this.authToken = authToken;
        if (!this.authToken.contains("auth="))
            this.authToken += "auth=";

        user = UserLoggedInManager.getUser(authToken);
        xmppService = XMPPServiceFactory.getXMPPService();
    }


    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }


    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public long getRunId() {
        return runId;
    }

    public void setRunId(long runId) {
        this.runId = runId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    protected void notify(String type, HashMap<String, String> params) {
        UsersDelegator qu = new UsersDelegator(getAuthToken());
        User me = qu.getUserByEmail(getRunId(), user);
        if (me == null) {
            log.log(Level.SEVERE, "could not retrieve corresponding user");
            return;
        }
        notify(type, params, qu, me);

    }

    protected void notify(String type, HashMap<String, String> params, UsersDelegator qu, User me) {
//		try {

        if (getScope() == USER) {
            notify(me.getEmail(), type, params);
        } else {
            //TODO scope with team instead of null value
            Iterator<User> it = qu.getUsers(getRunId(), null).getUsers().iterator();
            while (it.hasNext()) {
                User u = (User) it.next();
                switch (getScope()) {
                    case TEAM:
                        if (u.getTeamId().equals(me.getTeamId()))
                            notify(u.getEmail(), type, params);
                        break;
                    case ALL:
                        notify(u.getEmail(), type, params);
                        break;

                }
            }
        }

//		} catch (AuthenticationException e) {
//			log.log(Level.SEVERE, "AuthenticationException " + e.getMessage(), e);
//		}
    }

    public void notify(String email, String type, HashMap<String, String> params) {
//		log.severe("hashmap "+params);

//		String email = u.getEmail();
        if (!email.contains("@"))
            email += "@gmail.com";
        JID toJid = new JID(email);
        String messageBody = "<message to='" + email + "' from='streetlearn@appspot.com' type='chat' xml:lang='en'>" + "<body>Notification</body>" + "<type>"
                + type + "</type>";
        log.severe("messageBody " + messageBody);

        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            messageBody += "<" + key + ">" + params.get(key) + "</" + key + ">";
            log.severe("hashmap key " + key + " : " + params.get(key));


        }
        messageBody += "</message>";
        log.severe("messageBody " + messageBody);

        Message msg = new MessageBuilder().asXml(true).withRecipientJids(toJid).withBody(messageBody).build();
        xmppService.sendMessage(msg);
//		debug("arlearn1@gmail.com/arlearn");
//		debug("stefaan.ternier@gmail.com/arlearn");
//		log.severe("after send message");
    }

    public static void debug(String email) {
        JID jid = new JID(email);
        String msgBody = "Someone has sent you a gift on Example.com. To view: http://example.com/gifts/";
        Message msg = new MessageBuilder()
                .withRecipientJids(jid)
                .withBody(msgBody)
                .build();

        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        SendResponse status = xmpp.sendMessage(msg);
        messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        log.severe("message was sent successfully " + messageSent + " " + status.getStatusMap().toString());
    }

}
