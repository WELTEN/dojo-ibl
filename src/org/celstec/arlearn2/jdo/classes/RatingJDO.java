package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
public class RatingJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key id;

    @Persistent
    private Integer userProviderId;

    @Persistent
    private String userId;

    @Persistent
    private Integer rating;

    @Persistent
    private Long gameId;

    public void setId(){
        String key = getGameId()+":"+getUserProviderId()+":"+getUserId();
        setId(KeyFactory.createKey(RatingJDO.class.getSimpleName(), key));
    }

    public String getId() {
        if (id.getName()!=null) return id.getName();
        return ""+id.getId();
    }

    public void setId(Key id) {
        this.id = id;
    }

    public Integer getUserProviderId() {
        return userProviderId;
    }

    public void setUserProviderId(Integer userProviderId) {
        this.userProviderId = userProviderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
