package org.celstec.arlearn2.tasks.beans;

import com.google.appengine.api.datastore.Cursor;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.cache.CSVCache;
import org.celstec.arlearn2.cache.CSVEntry;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ResponseJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.celstec.arlearn2.jdo.manager.ResponseManager;
import org.codehaus.jettison.json.JSONException;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import javax.jdo.PersistenceManager;
import java.util.List;
import java.util.UUID;

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
public class CSVGeneration extends GenericBean {

    String cursorString;
    String csvId;
    Long runId;

    public CSVGeneration() {
        super();
    }

    public CSVGeneration(String cursorString, Long runId, String csvId) {
        super();
        this.cursorString = cursorString;
        this.runId = runId;
        this.csvId = csvId;
    }

    public String getCsvId() {
        return csvId;
    }

    public void setCsvId(String csvId) {
        this.csvId = csvId;
    }

    public String getCursorString() {
        return cursorString;
    }

    public void setCursorString(String cursorString) {
        this.cursorString = cursorString;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    @Override
    public void run() {
//        if ("*".equals(cursorString)) {
//            startIteration(null);
//        } else {
            startIteration(cursorString);
//        }
    }

//    private void rescheduleIteration(List<RunJDO> runs) {
//        Cursor cCursor = JDOCursorHelper.getCursor(runs);
//        String cursorString = cCursor.toWebSafeString();
//        System.out.println("cursorout " + cursorString);
//        (new CSVGeneration(cursorString)).scheduleTask();
//    }

    public CSVEntry firstIteration(){
        csvId = UUID.randomUUID().toString();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ResponseList rl = ResponseManager.getResponse(runId, 0l,null, cursorString);
            CSVEntry entry = processResponses(pm, rl.getResponses());
            if (!(rl.getResponses().isEmpty() || rl.getResumptionToken() == null)) {
                new CSVGeneration(rl.getResumptionToken(), runId, csvId).scheduleTask();
            } else {
                entry.setStatus(CSVEntry.FINISHED_STATUS);
            }

            CSVCache.getInstance().putCSV(csvId, entry);
            return entry;
        } finally {
            pm.close();
        }
    }

    private void startIteration(String cursorString) {
//        if (cursorString == null) {
//            CSVEntry entry = CSVCache.getInstance().getCSV(csvId);
//            if (entry != null) {
//                if (entry.getStatus() == CSVEntry.BUILDING_STATUS) {
//                    return;
//                }
//            }
//        }
        CSVEntry entry = CSVCache.getInstance().getCSV(csvId);

        if (entry != null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                ResponseList rl = ResponseManager.getResponse(runId, 0l, null, cursorString);
                entry = processResponses(pm, rl.getResponses());
                if (!(rl.getResponses().isEmpty() || rl.getResumptionToken() == null)) {
                    new CSVGeneration(rl.getResumptionToken(), runId, csvId).scheduleTask();
                } else {
                    entry.setStatus(CSVEntry.FINISHED_STATUS);
                }
                CSVCache.getInstance().putCSV(csvId, entry);
            } finally {
                pm.close();
            }
        }
    }

    private CSVEntry processResponses(PersistenceManager pm, List<Response> responseJDOs) {
        CSVEntry entry = CSVCache.getInstance().getCSV(csvId);
        if (entry == null) {
            entry = new CSVEntry("Response Identifier;Timestamp;Item Identifier;Response Item Identifier; Account; " +
                    "Image Url; Video Url; Width; Height; Latitude; Longitude; Audio Url;Text; Value \n", CSVEntry.BUILDING_STATUS);
        }
        String csv = entry.getCSV();
        for (Response responseJDO : responseJDOs) {
            String value = responseJDO.getResponseValue();
            String imageUrl ="";
            String audioUrl ="";
            String videoUrl ="";
            String width = "";
            String height = "";
            String text = "";
            String valueString = "";
            String lat = responseJDO.getLat() == null ?"":""+responseJDO.getLat().doubleValue();
            String lng = responseJDO.getLng() == null ?"":""+responseJDO.getLng().doubleValue();
            try {
                org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(value);
                if (json.has("imageUrl")) imageUrl = json.getString("imageUrl");
                if (json.has("audioUrl")) audioUrl = json.getString("audioUrl");
                if (json.has("videoUrl")) videoUrl = json.getString("videoUrl");
                if (json.has("width")) width = ""+json.getInt("width");
                if (json.has("height")) height = ""+json.getInt("height");
                if (json.has("text")) text = json.getString("text");
                if (json.has("value")) valueString = json.getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            {"imageUrl":"http:\/\/streetlearn.appspot.com\/\/uploadService\/2288002\/2:113116978440346588314\/image1537716519.jpg","width":3264,"height":1840}
//            {"answer":"3","isCorrect":true}
            csv += responseJDO.getResponseId()+";"+
                    responseJDO.getTimestamp()+";"+
                    responseJDO.getGeneralItemId()+";"+
                    responseJDO.getResponseItemId()+";"+
                    responseJDO.getUserEmail()+";"+
                    imageUrl+";"+
                    videoUrl+";"+
                    width+";"+
                    height+";"+
                    lat+";"+
                    lng+";"+
                    audioUrl+";"+
                    text+";"+
                    valueString+"\n";
        }
        entry.setCSV(csv);
        return entry;
    }

//    private void processResponse(PersistenceManager pm, Response runJDO) {
//        String authToken = null;
//        GameDelegator gd = new GameDelegator();
////		if (gd.getGameWithoutAccount(runJDO.getGameId()) == null) {
//        //switched this one off - > dangerous
//        if (false) {
//            RunManager.setStatusDeleted(runJDO.getRunId());
//            RunsCache.getInstance().removeRun(runJDO.getRunId());
//            (new UpdateGeneralItemsVisibility(authToken, null, runJDO.getRunId(), null, 2)).scheduleTask();
//
////			(new DeleteVisibleItems(authToken, r.getRunId())).scheduleTask();
//            (new DeleteActions(authToken, null, runJDO.getRunId())).scheduleTask();
//            (new DeleteTeams(authToken, null, runJDO.getRunId(), null)).scheduleTask();
//            (new DeleteBlobs(authToken, null, runJDO.getRunId())).scheduleTask();
//            (new DeleteResponses(authToken, null, runJDO.getRunId())).scheduleTask();
//        }
//        if (runJDO.getDeleted() != null && runJDO.getDeleted() && (runJDO.getLastModificationDate()+ (2592000000l)) < System.currentTimeMillis()) {
//            RunManager.deleteRun(pm, runJDO);
//        }
//
//    }
}
