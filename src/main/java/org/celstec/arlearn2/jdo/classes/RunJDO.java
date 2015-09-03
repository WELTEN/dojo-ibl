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
public class RunJDO extends RunClass{

	@Persistent
	private Long gameId;
	
	@Persistent
	private String title;
	
	@Persistent
    private String owner;
	
	@Persistent
    private String tagId;
	
	@Persistent
	private Long startTime;

	@Persistent
	private Long serverCreationTime;
	
	@Persistent
	private Long lastModificationDate;
	
	@Persistent
	private Text payload;

	public Long getRunId() {
		return id.getId();
	}
	
	public void setRunId(Long runId) {
		if (runId != null) 
			setRunId(KeyFactory.createKey(RunJDO.class.getSimpleName(), runId));
	}
	
	public void setRunId(Key runId) {
		this.id = runId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getServerCreationTime() {
		return serverCreationTime;
	}

	public void setServerCreationTime(Long serverCreationTime) {
		this.serverCreationTime = serverCreationTime;
	}

	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Text getPayload() {
		return payload;
	}

	public void setPayload(Text payload) {
		this.payload = payload;
	}

	

	
}
