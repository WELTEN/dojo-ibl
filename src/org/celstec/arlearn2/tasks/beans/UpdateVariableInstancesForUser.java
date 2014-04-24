package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.VariableDelegator;
import org.celstec.arlearn2.jdo.manager.VariableInstanceManager;

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
 * Contributors: Roland Klemke
 * ****************************************************************************
 */
public class UpdateVariableInstancesForUser extends GenericBean {

    private Long runId;
    private Long gameId;
    private String userAccount;
    private Integer updateType;

    public UpdateVariableInstancesForUser() {

    }

    public UpdateVariableInstancesForUser(String token, Account accountAuth, String account, Long runId, Long gameId, Integer updateType) {
        super(token, accountAuth);
        this.runId = runId;
        this.gameId = gameId;
        this.updateType = updateType;
        this.userAccount = account;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    @Override
    public void run() {
        switch (updateType) {
            case 1:
                create();
                break;
            case 2:
                delete();
                break;
            default:
                break;
        }
    }

    private void delete() {
        VariableInstanceManager.delete(getGameId(), getRunId(), null, getUserAccount(), null);
    }

    private void create() {
        if (getRunId()!= null & getGameId() == null) {
            RunDelegator rd = new RunDelegator(this);
            setGameId(rd.getRun(getRunId()).getGameId());
        }
        VariableDelegator vad = new VariableDelegator(this);
        List<VariableDefinition> list = vad.getVariableDefinitionsList(getGameId(), 0);
        for (VariableDefinition variableDefinition : list) {
            System.out.println("variable "+variableDefinition);
            VariableInstanceManager.createVariableInstanceForAccount(userAccount, getGameId(), getRunId(), variableDefinition.getName(), variableDefinition.getStartValue());
        }
    }

}
