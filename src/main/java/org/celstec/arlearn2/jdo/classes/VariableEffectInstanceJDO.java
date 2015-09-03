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
public class VariableEffectInstanceJDO extends RunClass{

    @Persistent
    private Long variableEffectDefinitionIdentifier;

    @Persistent
    private String account;

    @Persistent
    private String teamId;

    @Persistent
    private Integer effectCount;

    public void setUniqueIdAccount() {
        this.id = KeyFactory.createKey(VariableEffectInstanceJDO.class.getSimpleName(), getRunId() + ":" + getAccount()+":" + getVariableEffectDefinitionIdentifier());
    }

    public void setUniqueIdTeam() {
        this.id = KeyFactory.createKey(VariableEffectInstanceJDO.class.getSimpleName(), getRunId() + ":" + getTeamId()+":" + getVariableEffectDefinitionIdentifier());
    }

    public void setUniqueIdGlobal() {
        this.id = KeyFactory.createKey(VariableEffectInstanceJDO.class.getSimpleName(), getRunId() + ":" + getVariableEffectDefinitionIdentifier());
    }

    public Long getVariableEffectDefinitionIdentifier() {
        return variableEffectDefinitionIdentifier;
    }

    public void setVariableEffectDefinitionIdentifier(Long variableEffectDefinitionIdentifier) {
        this.variableEffectDefinitionIdentifier = variableEffectDefinitionIdentifier;
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

    public Integer getEffectCount() {
        return effectCount;
    }

    public void setEffectCount(Integer effectCount) {
        this.effectCount = effectCount;
    }
}
