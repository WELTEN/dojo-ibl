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
 * Contributors: Roland Klemke
 ******************************************************************************/
package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: klemke
 * Date: 18.07.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class UpdateVariables extends GenericBean {
    private Long runId;
    private String action;
    private String userEmail;
    private Long generalItemId;
    private String generalItemType;
    private Long timestamp;

    private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

    public UpdateVariables() {

    }

    public UpdateVariables(String token, Long runId, String action, String userEmail, Long generalItemId, String generalItemType, Long timestamp) {
        super(token);
        this.runId = runId;
        this.action = action;
        this.userEmail = userEmail;
        this.generalItemId = generalItemId;
        this.generalItemType = generalItemType;
        this.timestamp = timestamp;

    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getGeneralItemId() {
        return generalItemId;
    }

    public void setGeneralItemId(Long generalItemId) {
        this.generalItemId = generalItemId;
    }

    public String getGeneralItemType() {
        return generalItemType;
    }

    public void setGeneralItemType(String generalItemType) {
        this.generalItemType = generalItemType;
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
        // prepare objects

        UsersDelegator qu = null;
        User u = null;
            qu = new UsersDelegator("auth=" + getToken());
            u = qu.getUserByEmail(runId, getUserEmail());


        Action a = new Action();
        a.setRunId(runId);
        a.setAction(getAction());
        a.setGeneralItemId(getGeneralItemId());
        a.setGeneralItemType(getGeneralItemType());
        a.setUserEmail(getUserEmail());
        a.setTimestamp(getTimestamp());
        a.setTime(getTimestamp());
        RunDelegator qr = new RunDelegator(qu);
        Run run = qr.getRun(a.getRunId());

        VariableDelegator vd = new VariableDelegator(qu);

        vd.checkActionEffect(a, runId, u);


        // get relevant variable effect instances for this action/user
        // get variable instances for this user
        // apply applicable variable effect instances to variables

    }

}
