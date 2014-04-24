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
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.delegators.progressRecord.CreateProgressRecord;
import org.celstec.arlearn2.jdo.manager.ActionManager;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItems;
import org.celstec.arlearn2.tasks.beans.UpdateVariables;
import org.celstec.arlearn2.util.ActionCache;

import java.util.List;
import java.util.logging.Logger;

public class ActionDelegator extends GoogleDelegator{
	
	private static final Logger logger = Logger.getLogger(ActionDelegator.class.getName());

    public ActionDelegator(Service service) {
        super(service);
    }

	public ActionDelegator(String authtoken)  {
		super(authtoken);
	}
	
	public ActionDelegator(GoogleDelegator gd) {
		super(gd);
	}

    public ActionDelegator(Account account, String authToken) {
        super(account, authToken);
    }
	
	
	public ActionList getActionList(Long runId) {
		ActionList al = ActionCache.getInstance().getRunActions(runId);
		if (al == null) {
			al = ActionManager.runActions(runId);
			ActionCache.getInstance().putRunActions(runId, al);
		}
		return al;
	}
	private static final Logger log = Logger.getLogger(ActionDelegator.class.getName());

	public Action createAction(Action action) {

		if (action.getRunId() == null) {
			action.setError("No run identifier specified");
			return action;
		}
		UsersDelegator qu = new UsersDelegator(this);
		action.setUserEmail(qu.getCurrentUserAccount());
		
		RunDelegator rd = new RunDelegator(this);
		Run r = rd.getRun(action.getRunId());
		
		User u = qu.getUserByEmail(action.getRunId(), action.getUserEmail());
		if (u == null) {
			action.setError("User not found");
			log.severe("user not found");
			return action;
		}
		// check if this action needs to be recorded as progress
		CreateProgressRecord cpr = new CreateProgressRecord(this);
//		cpr.updateProgress(action.getRunId(), action.getAction(), action.getUserEmail(), u.getTeamId());
		cpr.updateProgress(action, u.getTeamId());
		
		// check if this action needs to be recorded as score
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(action.getRunId());
		ActionRelevancyPredictor arp = ActionRelevancyPredictor.getActionRelevancyPredicator(run.getGameId(), this);

		//TODO migrate these to list of relevant dependecies (getActionDependencies[])
		boolean relevancy = arp.isRelevant(action);
		ActionManager.addAction(action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType(), action.getTimestamp());
		ActionCache.getInstance().removeRunAction(action.getRunId());

        RunAccessDelegator rad = new RunAccessDelegator(this);
        NotificationDelegator nd = new NotificationDelegator(this);
        for (RunAccess ra :rad.getRunAccess(r.getRunId()).getRunAccess()){
            nd.broadcast(action, ra.getAccount());
        }
//		ChannelNotificator.getInstance().notify(r.getOwner(), action);
		if (relevancy) {
			(new UpdateGeneralItems(authToken, action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType())).scheduleTask();
		}
        System.out.println("Now also schedule the variable update!");
        (new UpdateVariables(authToken, action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType(), action.getTimestamp())).scheduleTask();
		return action;
	}
	
	private boolean applyRelevancyFilter(Action action, List<ActionDependency> dependencies) {
		for (ActionDependency dep: dependencies) {
			boolean soFar = true;
			if (dep.getAction() != null && !dep.getAction().equals(action.getAction())) soFar = false; 
			if (dep.getGeneralItemId() != null && !dep.getGeneralItemId().equals(action.getGeneralItemId()))soFar = false; 
			if (dep.getGeneralItemType() != null && !dep.getGeneralItemType().equals(action.getGeneralItemType())) soFar = false; 
			if (soFar) return true;
		}
		return false;
	}
	
//	private List<ActionDependency> getActionDependencies(Long runId) {
//		RunDelegator qr = new RunDelegator(this);
//		Run run = qr.getRun(runId);
//		List<ActionDependency> gil = GeneralitemsCache.getInstance().getGameActions(run.getGameId(), null, null);
//		if (gil == null) {
//			System.out.println("not from cache");
//			GeneralItemDelegator gd = new GeneralItemDelegator(this);
//			gil = getActionDependencies(gd.getGeneralItems(run.getGameId()).getGeneralItems());
//			GeneralitemsCache.getInstance().putGameActionsList(gil, run.getGameId(), null, null);
//		}
//		return gil;
//	}
	
	

	public void deleteActions(Long runId) {
		ActionManager.deleteActions(runId);
		ActionCache.getInstance().removeRunAction(runId);
	}
	
	public void deleteActions(Long runId, String email) {
		ActionManager.deleteActions(runId, email);
		ActionCache.getInstance().removeRunAction(runId);
	}


    public Object getActionsFromUntil(Long runIdentifier, Long from, Long until, String cursor) {
        return ActionManager.getActions(runIdentifier, from, until, cursor);
    }
}
