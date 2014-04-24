package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Key;
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
public class TopGames extends GameClass {

    @Persistent
    private Long amountOfUsers;

    @Persistent
    private Long generateTimestamp;

    public Long getGameId() {
        return id.getId();
    }

    public void setGameId(Long gameId) {
        super.setGameId(gameId);
        if (gameId != null)
            setGameId(KeyFactory.createKey(TopGames.class.getSimpleName(), gameId));
    }

    public void setGameId(Key gameId) {
        this.id = gameId;
    }

    public Long getAmountOfUsers() {
        return amountOfUsers;
    }

    public void setAmountOfUsers(Long amountOfUsers) {
        this.amountOfUsers = amountOfUsers;
    }

    public Long getGenerateTimestamp() {
        return generateTimestamp;
    }

    public void setGenerateTimestamp(Long generateTimestamp) {
        this.generateTimestamp = generateTimestamp;
    }
}
