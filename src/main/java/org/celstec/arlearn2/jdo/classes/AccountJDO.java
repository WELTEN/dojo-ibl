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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class AccountJDO {

	public final static int FBCLIENT = 1;
	public final static int GOOGLECLIENT = 2;
	public final static int LINKEDINCLIENT = 3;
    public final static int TWITTERCLIENT = 4;
    public final static int WESPOTCLIENT = 5;
    public final static int ECOCLIENT = 6;

	public final static int ADMINISTRATOR = 1;
	public final static int USER = 2;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key uniqueId;
	
	@Persistent
	private String localId;
	
	@Persistent
	private Integer accountType;
	
	@Persistent
	private String email;
	
	@Persistent
	private String name;
	
	@Persistent
	private String given_name;
	
	@Persistent
	private String family_name;
	
	@Persistent
	private String picture;
	
	@Persistent
	private Long lastModificationDate;
	
	@Persistent
	private Integer accountLevel;

    @Persistent
    private Boolean allowTrackLocation;

	public String getUniqueId() {
		return uniqueId.getName();
	}

	public void setUniqueId() {
		this.uniqueId = KeyFactory.createKey(AccountJDO.class.getSimpleName(), getAccountType()+":"+getLocalId());
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Integer getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(Integer accountLevel) {
		this.accountLevel = accountLevel;
	}

    public Boolean getAllowTrackLocation() {
        return allowTrackLocation;
    }

    public void setAllowTrackLocation(Boolean allowTrackLocation) {
        this.allowTrackLocation = allowTrackLocation;
    }
}
