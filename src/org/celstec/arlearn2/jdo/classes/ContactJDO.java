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
import com.google.appengine.api.search.GetException;

@PersistenceCapable
public class ContactJDO {

	public final static int PENDING = 1;
	public final static int ACCEPTED = 2;
	public final static int DELETED = 3;

	@Persistent
	private Long lastModificationDate;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key uniqueId;

	@Persistent
	private Integer fromAccountType;

	@Persistent
	private String fromLocalId;

	@Persistent
	private Integer toAccountType;

	@Persistent
	private String toLocalId;

	@Persistent
	private String toEmail;

	@Persistent
	private Integer status;

	public String getUniqueId() {
		return uniqueId.getName();
	}

	public void setUniqueId() {
		if (getToEmail() != null) {
			this.uniqueId = KeyFactory.createKey(ContactJDO.class.getSimpleName(), getFromAccountType() + ":" + getFromLocalId() + ":" + getToEmail());
		} else {
			this.uniqueId = KeyFactory.createKey(ContactJDO.class.getSimpleName(), getFromAccountType() + ":" + getFromLocalId() + ":" + getToAccountType() + ":" + getToLocalId());
		}
	}
	
	public void setUniqueId(String id) {
		this.uniqueId = KeyFactory.createKey(ContactJDO.class.getSimpleName(), id);
	}

	public Integer getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(Integer fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getFromLocalId() {
		return fromLocalId;
	}

	public void setFromLocalId(String fromLocalId) {
		this.fromLocalId = fromLocalId;
	}

	public Integer getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(Integer toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getToLocalId() {
		return toLocalId;
	}

	public void setToLocalId(String toLocalId) {
		this.toLocalId = toLocalId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	
	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

}
