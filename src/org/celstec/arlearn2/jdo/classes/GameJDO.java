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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.celstec.arlearn2.beans.game.Game;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class GameJDO extends GameClass {
	
	@Persistent
    private String title;

	@Persistent
    private String owner;
	
	@Persistent
    private String creatorEmail;
	
	@Persistent
    private String feedUrl;
	
	@Persistent
	private Text config;
	
	@Persistent
	private Text description;
	
	@Persistent
	private Integer sharing;
	
	@Persistent
	private String licenseCode;

    @Persistent
    private Double lat;

    @Persistent
    private Double lng;

    @Persistent
    private Boolean featured;

    @Persistent
    private String language;

	public Long getGameId() {
		return id.getId();
	}
	
	public void setGameId(Long gameId) {
		if (gameId != null) 
			setGameId(KeyFactory.createKey(GameJDO.class.getSimpleName(), gameId));
	}
	
	public void setGameId(Key gameId) {
		this.id = gameId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getConfig() {
		if (config == null) return null;
		return config.getValue();
	}

	public void setConfig(String config) {
		this.config = new Text(config);
	}
	
	public String getDescription() {
		if (description == null) return null;
		return description.getValue();
	}

	public void setDescription(String description) {
		if (description != null) this.description = new Text(description);
	}

	public Integer getSharing() {
		if (sharing == null) return Game.PRIVATE;
		return sharing;
	}

	public void setSharing(Integer sharing) {
		this.sharing = sharing;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
