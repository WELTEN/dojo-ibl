package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.account.Account;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
public class SendToLearningLocker extends GenericBean {

    private Long runId;
    private String action;
    private String userEmail;
    private Long timestamp;

    private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

    public SendToLearningLocker() {

    }

    public SendToLearningLocker(String token, Long runId, String action, String userEmail, Long timestamp) {
        super(token);
        this.runId = runId;
        this.action = action;
        this.userEmail = userEmail;
        this.timestamp = timestamp;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        JSONObject lockerStatement = new JSONObject();
        try {
            lockerStatement.put("actor", createActorStatement("Stefaan Ternier", "stefaan.ternier@gmail.com"));
            lockerStatement.put("verb", createVerbStatement(action));
            lockerStatement.put("object", createObjectStatement());
            lockerStatement.put("timeStamp", "2014-09-02T16:11:38.8815770+02:00");
            lockerStatement.put("version", "1.0.1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject createActorStatement(String agent, String mail) throws JSONException {
        JSONObject actorStatement = new JSONObject();
        actorStatement.put("objectType", "Agent");
        actorStatement.put("name", agent);
        actorStatement.put("mbox", "mailto: "+mail);
        return actorStatement;
    }

    private JSONObject createVerbStatement(String action) throws JSONException {
        JSONObject verbStatement = new JSONObject();
        if (action.equals("startRun")){
            verbStatement.put("id", "http://ou.nl/arlearn/action/startRun");
            JSONObject display = new JSONObject();
            display.put("en-US", "User started run");
            verbStatement.put("display", display);
        }
        return verbStatement;
    }

    private JSONObject createObjectStatement() throws JSONException {
        JSONObject objectStatement = new JSONObject();
        objectStatement.put("objectType", "Activity");
        objectStatement.put("id", "http://ou/nl/arlearn/1234");
        JSONObject definition = new JSONObject();
        JSONObject name = new JSONObject();
        name.put("en-US", "Run name");
        definition.put("name", name);

        objectStatement.put("display", definition);
        return objectStatement;
    }

}
