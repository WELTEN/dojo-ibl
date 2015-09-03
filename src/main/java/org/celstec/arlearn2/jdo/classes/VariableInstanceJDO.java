package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
@PersistenceCapable
public class VariableInstanceJDO extends RunClass {

    @Persistent
    private String account;

    @Persistent
    private String teamId;

    @Persistent
    private Long value;

    @Persistent
    private Long gameId;

    @Persistent
    private String name;

    public void setUniqueId() {
        if (getAccount() != null && !("".equals(getAccount()))) {
            setUniqueIdAccount();
        } else if (getTeamId() != null && !("".equals(getTeamId()))) {
            setUniqueIdTeam();
        } else {
            setUniqueIdGlobal();
        }
    }

    public void setUniqueIdAccount() {
        this.id = KeyFactory.createKey(VariableInstanceJDO.class.getSimpleName(), getGameId() + ":" + getRunId()+":"+ getName() + ":" + getAccount());
    }

    public void setUniqueIdTeam() {
        this.id = KeyFactory.createKey(VariableInstanceJDO.class.getSimpleName(), getGameId() + ":" + getRunId()+":"+ getName() + ":" + getTeamId());
    }

    public void setUniqueIdGlobal() {
        this.id = KeyFactory.createKey(VariableInstanceJDO.class.getSimpleName(), getGameId() + ":" + getRunId()+":"+ getName());
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
