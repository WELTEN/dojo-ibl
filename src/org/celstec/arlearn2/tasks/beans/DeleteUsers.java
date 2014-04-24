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
package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.UsersDelegator;


public class DeleteUsers extends GenericBean {

	private Long runId;
	private String userId;
	private String teamId;

	public DeleteUsers() {
		super();
	}

	public DeleteUsers(String token, Long runId, String userId, String teamId) {
		super(token);
		this.runId = runId;
		this.userId = userId;
		this.teamId = teamId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Override
	public void run() {
			UsersDelegator ud = new UsersDelegator(getToken());
			if (getUserId()!=null) {
				ud.deleteUser(getRunId(), getUserId());
			} 
			if (getTeamId() != null) {
				ud.deleteUser(getTeamId());
			}
			if (getRunId() != null) {
				ud.deleteUser(runId);
			}


	}

}
