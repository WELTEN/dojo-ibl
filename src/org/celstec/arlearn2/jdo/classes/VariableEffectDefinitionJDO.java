package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
public class VariableEffectDefinitionJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key id;

    @Persistent
    private Long gameId;

    @Persistent
    private String name;

    @Persistent
    private String effectType;

    @Persistent
    private Long effectValue;

    @Persistent
    private Integer effectCount;


    @Persistent
    private Text dependsOn;

    public void setIdentifier(Long id) {
        if (id != null)
            this.id = KeyFactory.createKey(VariableEffectDefinitionJDO.class.getSimpleName(), id);
    }

    public Long getIdentifier() {
        return id.getId();
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

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public Long getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(Long effectValue) {
        this.effectValue = effectValue;
    }

    public Integer getEffectCount() {
        return effectCount;
    }

    public void setEffectCount(Integer effectCount) {
        this.effectCount = effectCount;
    }

    public Text getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(Text dependsOn) {
        this.dependsOn = dependsOn;
    }
}
