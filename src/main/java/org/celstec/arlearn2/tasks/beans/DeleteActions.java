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

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.delegators.ActionDelegator;


public class DeleteActions extends GenericBean {
	
	private Long runId;
	private String fullAccount;

	public DeleteActions() {
		super();
	}
	
	public DeleteActions(String token,  Account account,Long runId) {
		super(token, account);
		this.runId = runId;
	}
	
	public DeleteActions(String token, Account account, Long runId, String fullAccount) {
		super(token, account);
		this.runId = runId;
		this.fullAccount = fullAccount;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

    public String getFullAccount() {
        return fullAccount;
    }

    public void setFullAccount(String fullAccount) {
        this.fullAccount = fullAccount;
    }

    @Override
	public void run() {
			ActionDelegator ad = new ActionDelegator(getAccountBean(), getToken());
			if (fullAccount == null) ad.deleteActions(getRunId());
			ad.deleteActions(getRunId(), getFullAccount());
	}
}
