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
public class UserJDO extends RunClass {

	@Persistent
    private String teamId;
	
	@Persistent
    private String name;
	
	@Persistent
    private String email;

	@Persistent
	private Text payload;
	
	@Persistent
	private Long lastModificationDate;
	
	@Persistent
	private Long gameId;
	
	@Persistent
	private Long lastModificationDateGame;
	
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setId() {
		if (getEmail() != null && getRunId() !=null) {
			this.id = KeyFactory.createKey(UserJDO.class.getSimpleName(), generateId(getEmail(), getRunId()));
		}
	}
	
	public static String generateId(String email, Long runId) {
		return runId +":"+email;
	}
	
	public Text getPayload() {
		return payload;
	}

	public void setPayload(Text payload) {
		this.payload = payload;
	}
	
	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getLastModificationDateGame() {
		return lastModificationDateGame;
	}

	public void setLastModificationDateGame(Long lastModificationDateGame) {
		this.lastModificationDateGame = lastModificationDateGame;
	}

}
