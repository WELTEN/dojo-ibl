package org.celstec.arlearn2.tasks.beans;

import com.sun.jersey.core.util.Base64;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
    private String title;

    private static DateFormat df;
    static {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
    }
    private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

    public SendToLearningLocker() {

    }

    public SendToLearningLocker(String token, Long runId, String action, String userEmail, Long timestamp, String title) {
        super(token);
        this.runId = runId;
        this.action = action;
        this.userEmail = userEmail;
        this.timestamp = timestamp;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        JSONObject lockerStatement = new JSONObject();
        try {
            lockerStatement.put("actor", createActorStatement("Stefaan Ternier", "stefaan.ternier@gmail.com"));
            JSONObject verb = createVerbStatement(action);
            if (verb != null) lockerStatement.put("verb", verb);
            lockerStatement.put("object", createObjectStatement());
            lockerStatement.put("timestamp", df.format(new Date(getTimestamp())));
            lockerStatement.put("version", "1.0.1");
            if (verb != null) {
                publish(lockerStatement);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void publish(JSONObject lockerStatement) {
        System.out.println(lockerStatement);
        try {
            URL url = new URL("http://sharetec.celstec.org/learninglocker/public/data/xAPI/statements");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            String userCredentials = "b6320653969d79f453b748b6574b8de4085b8251:3185603aa50e6ba74e57568712a4ca6339ec5bd7";
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            connection.setRequestProperty("X-Experience-API-Version", "1.0.0");
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(lockerStatement.toString());
            writer.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("ok");
                // OK
            } else {
                System.out.println("not ok");
            }
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
    }

    private JSONObject createActorStatement(String agent, String mail) throws JSONException {
        JSONObject actorStatement = new JSONObject();
        actorStatement.put("objectType", "Agent");
        actorStatement.put("name", agent);
        actorStatement.put("mbox", "mailto: " + mail);
        return actorStatement;
    }

    private JSONObject createVerbStatement(String action) throws JSONException {
        JSONObject verbStatement = new JSONObject();
        if (action.equals("startRun")) {
            verbStatement.put("id", "http://ou.nl/arlearn/action/startRun");
            JSONObject display = new JSONObject();
            display.put("en-US", "User started run");
            verbStatement.put("display", display);
        }
        if (verbStatement.has("id")) {
            return verbStatement;
        } else {
            return null;
        }

    }

    private JSONObject createObjectStatement() throws JSONException {
        JSONObject objectStatement = new JSONObject();
        objectStatement.put("objectType", "Activity");
        objectStatement.put("id", "http://ou/nl/arlearn/run/"+runId);
        JSONObject definition = new JSONObject();
        JSONObject name = new JSONObject();
        name.put("en-US", getTitle());
        definition.put("name", name);

        objectStatement.put("definition", definition);
        return objectStatement;
    }

}
