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

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;

public class UpdateGeneralItemsVisibility extends GenericBean{
	
	private Long runId;
	private String userEmail; //TODO check whether userEmail should not be fullId
	private Integer updateType;
	

	private static final Logger log = Logger.getLogger(UpdateGeneralItemsVisibility.class.getName());

	public UpdateGeneralItemsVisibility() {
		

	}
	
	public UpdateGeneralItemsVisibility(String token,  Account account,Long runId, String userEmail, Integer updateType) {
		super(token, account);
		this.runId = runId;
		this.userEmail = userEmail;
		this.updateType = updateType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	@Override
	public void run() {
		switch (updateType) {
		case 1:
			create();
			break;
		case 2:
			delete();
			break;
		default:
			break;
		}
	}
	
	private void delete() {
		GeneralItemVisibilityManager.delete(getRunId(), getUserEmail());
	}

	private void create() {
		GeneralItemDelegator gid = null;
					gid = new GeneralItemDelegator("auth=" + getToken());
		UsersDelegator ud = new UsersDelegator(gid);
		User u = ud.getUserByEmail(getRunId(), getUserEmail());
		GeneralItemList gil = gid.getGeneralItemsRun(getRunId());
        if (gil != null && gil.getGeneralItems() != null) {
			for (GeneralItem gi: gil.getGeneralItems()) {



				if (gi.getDependsOn() == null && GeneralItemDelegator.itemMatchesUserRoles(gi, u.getRoles()) && (gi.getDeleted() == null || !gi.getDeleted())) {
					GeneralItemVisibilityManager.setItemVisible(gi.getId(), getRunId(), getUserEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS, System.currentTimeMillis());
				}
			}
		}
	}
}
