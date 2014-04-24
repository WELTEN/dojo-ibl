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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class GeneralItemJDO extends GameClass {


	@Persistent
	private String name;
	
	@Persistent
	private Text description;

	@Persistent
	private Text dependsOn;

	@Persistent
	private Text payload;
	
	@Persistent
	private String type;
	
	@Persistent
	private Integer radius;
	
	@Persistent
	private String scope;
	
	@Persistent
	private Long showAtTimeStamp;

	@Persistent
	private Double lng;
	
	@Persistent
	private Double lat;
	
	@Persistent
	private Long timeStamp;

	@Persistent
	private String iconUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public Text getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(Text dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Long getShowAtTimeStamp() {
		return showAtTimeStamp;
	}

	public void setShowAtTimeStamp(Long showAtTimeStamp) {
		this.showAtTimeStamp = showAtTimeStamp;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void setIdentifier(Long id) {
		if (id != null) 
			this.id = KeyFactory.createKey(GeneralItemJDO.class.getSimpleName(), id);
	}
	
	public void setRunId(Key giId) {
		this.id = giId;
	}
	
	public Long getIdentier() {
		return id.getId();
	}

	public Text getPayload() {
		return payload;
	}

	public void setPayload(Text payload) {
		this.payload = payload;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}
