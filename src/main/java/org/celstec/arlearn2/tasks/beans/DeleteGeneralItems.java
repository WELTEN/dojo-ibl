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
import org.celstec.arlearn2.delegators.GeneralItemDelegator;

public class DeleteGeneralItems  extends GenericBean {

	private Long gameId;

	public DeleteGeneralItems() {
		super();
	}

    public DeleteGeneralItems(String authToken, Account account, Long gameIdentifier) {
        super(authToken, account);
        this.gameId = gameIdentifier;
    }

    public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public void run() {

			GeneralItemDelegator gid = new GeneralItemDelegator(getAccountBean(), getToken());

//			CreateGeneralItems cgi = new CreateGeneralItems("auth=" + getToken());
			if (getGameId() !=null) gid.deleteGeneralItems(getGameId());

		
	}

}
