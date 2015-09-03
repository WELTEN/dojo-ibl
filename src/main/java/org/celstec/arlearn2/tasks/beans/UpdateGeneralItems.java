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

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.delegators.ActionRelevancyPredictor;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;


public class UpdateGeneralItems extends GenericBean{

	private Long runId;
	private String action;
	private String userEmail;
	private Long generalItemId;
	private String generalItemType;
	
	private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

	public UpdateGeneralItems() {
		
	}
	
	public UpdateGeneralItems(String token, Long runId, String action, String userEmail, Long generalItemId, String generalItemType) {
		super(token);
		this.runId = runId;
		this.action = action;
		this.userEmail = userEmail;
		this.generalItemId = generalItemId;
		this.generalItemType = generalItemType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getGeneralItemType() {
		return generalItemType;
	}

	public void setGeneralItemType(String generalItemType) {
		this.generalItemType = generalItemType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	
	@Override
	public void run() {

		UsersDelegator qu = null;
		User u = null;

			qu = new UsersDelegator("auth=" + getToken());
			u = qu.getUserByEmail(runId, getUserEmail());

		Action a = new Action();
		a.setRunId(runId);
		a.setAction(getAction());
		a.setGeneralItemId(getGeneralItemId());
		a.setGeneralItemType(getGeneralItemType());
		RunDelegator qr = new RunDelegator(qu);
		Run run = qr.getRun(a.getRunId());
		
		GeneralItemDelegator gid = new GeneralItemDelegator(qu);
		ActionRelevancyPredictor arp = ActionRelevancyPredictor.getActionRelevancyPredicator(run.getGameId(), qu);

		boolean userRelevant = arp.isRelevantForUser(a);
		boolean teamRelevant = arp.isRelevantForTeam(a); 
		boolean allRelevant = arp.isRelevantForAll(a); 
		if (userRelevant) {

			gid.checkActionEffect(a, runId, u);	
		} 
			

			if ((teamRelevant ||allRelevant)&&  qu != null) {
                UserList ul = qu.getUsers(runId);
                if (ul != null)
				for (User otherUser :ul.getUsers()) {
					if (!(userRelevant && u.getFullId().equals(otherUser.getFullId())))
					if (teamRelevant && u.getTeamId().equals(otherUser.getTeamId())) {
						gid.checkActionEffect(a, runId, otherUser);
					} else if (allRelevant) {

						gid.checkActionEffect(a, runId, otherUser);
					}
				}
			}
		
		
		
		
		
	}

}
