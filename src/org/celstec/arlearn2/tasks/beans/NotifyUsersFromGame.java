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

import java.util.List;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.notification.TeamModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.delegators.notification.NotificationEngine;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.codehaus.jettison.json.JSONException;

public class NotifyUsersFromGame extends GenericBean {

	private long runId;
	private String gi;
	private int modificationType;

	public NotifyUsersFromGame(){
		
	}
	
	public NotifyUsersFromGame(String token, long runId, String gi, int modificationType) {
		super(token);
		this.runId = runId;
		this.gi = gi;
		this.modificationType = modificationType;

	}
	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getGi() {
		return gi;
	}

	public void setGi(String gi) {
		this.gi = gi;
	}

	public int getModificationType() {
		return modificationType;
	}

	public void setModificationType(int modificationType) {
		this.modificationType = modificationType;
	}
	
	@Override
	public void run() {
		try {
			UsersDelegator ud = new UsersDelegator("auth=" + getToken());
			UserList ul = ud.getUsers(runId);
			GeneralItemModification gim = new GeneralItemModification();
			if (gi != null) {
			gim.setModificationType(modificationType);
			gim.setRunId(runId);
			
			gim.setGeneralItem((GeneralItem) JsonBeanDeserializer.deserialize(gi));
			gim.setGameId(gim.getGeneralItem().getGameId());
			gim.setItemId(gim.getGeneralItem().getId());
			}
			for (User u : ul.getUsers()) {
				if (modificationType == TeamModification.ALTERED) {
					TeamModification tm = new TeamModification();
					tm.setModificationType(TeamModification.ALTERED);
					tm.setRunId(runId);
					new NotificationDelegator(getToken()).broadcast(tm, u.getFullId());

				}
				if (u.getDeleted() == null || !u.getDeleted()) {
				VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, u.getFullId());
				if (modificationType == GeneralItemModification.CREATED) {
					if (gim.getGeneralItem().getDependsOn() == null && GeneralItemDelegator.itemMatchesUserRoles(gim.getGeneralItem(), u.getRoles()) && (gim.getGeneralItem().getDeleted() == null || !gim.getGeneralItem().getDeleted())) {
						GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), getRunId(), u.getFullId(), GeneralItemVisibilityManager.VISIBLE_STATUS, System.currentTimeMillis());
					}
					new NotificationDelegator(getToken()).broadcast(gim, u.getFullId());
				}
				if (modificationType == GeneralItemModification.VISIBLE) {
					if (gim.getGeneralItem().getDeleted() == null || !gim.getGeneralItem().getDeleted()) {
						GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), getRunId(), u.getFullId(), GeneralItemVisibilityManager.VISIBLE_STATUS, System.currentTimeMillis());
					}
					new NotificationDelegator(getToken()).broadcast(gim, u.getFullId());
				}
				if (modificationType == GeneralItemModification.DELETED) {
					GeneralItemVisibilityManager.delete(getRunId(), gim.getGeneralItem().getId(), u.getFullId(), null);
					new NotificationDelegator(getToken()).broadcast(gim, u.getFullId());
				}
				if (modificationType == GameModification.ALTERED ||modificationType == GameModification.DELETED ||modificationType == GameModification.CREATED) {
					UserManager.gameChanged(u);
				}
				}
			}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
