package org.celstec.arlearn2.jdo.classes;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

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
public class LomJDO extends GameClass {

    @Persistent
    private Text lom;

    @Persistent
    private Long generateTimestamp;

    @Persistent
    private Boolean deleted;

    public Long getGameId() {
        return id.getId();
    }

    public void setGameId(Long gameId) {
        super.setGameId(gameId);
        if (gameId != null)
            setGameId(KeyFactory.createKey(LomJDO.class.getSimpleName(), gameId));
    }

    public void setGameId(Key gameId) {
        this.id = gameId;
    }

    public Long getGenerateTimestamp() {
        return generateTimestamp;
    }

    public void setGenerateTimestamp(Long generateTimestamp) {
        this.generateTimestamp = generateTimestamp;
    }

    public String getLom() {
        return lom.getValue();
    }

    public void setLom(String lom) {
        this.lom = new Text(lom);
    }

    public void setLom(Text lom) {
        this.lom = lom;
    }

    public boolean isDeleted() {
        if (deleted == null) return false;
        return deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
