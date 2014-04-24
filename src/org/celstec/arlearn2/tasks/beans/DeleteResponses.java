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
import org.celstec.arlearn2.delegators.ResponseDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.jdo.manager.ResponseManager;


public class DeleteResponses extends GenericBean {

	private Long runId;
    private String fullAccount;

	public DeleteResponses() {
		super();
	}

	public DeleteResponses(String token, Account account, Long runId) {
		super(token, account);
		this.runId = runId;
	}

	public DeleteResponses(String token, Account account, Long runId, String fullAccount) {
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
			ResponseDelegator rd = new ResponseDelegator(getToken());
			if (getFullAccount() == null) {
				rd.deleteResponses(runId);
			} else{
				rd.deleteResponses(runId, getFullAccount());

			}
	}
}
