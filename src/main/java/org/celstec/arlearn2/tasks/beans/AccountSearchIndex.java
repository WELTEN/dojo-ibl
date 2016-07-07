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

import com.google.appengine.api.search.*;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountSearchIndex extends GenericBean {

	private String accountName;
	private String localId;
	private Integer accountType;

	private static final Logger log = Logger.getLogger(NotificationDelegator.class.getName());

	public AccountSearchIndex() {
		super();
	}

	public static void scheduleAccountTask(Account account) {
		new AccountSearchIndex(account.getName(), account.getLocalId(), account.getAccountType()).scheduleTask();
	}

	public AccountSearchIndex(String accountName, String localId, Integer accountType) {
		super();
		this.accountName = accountName;
		this.localId = localId;
		this.accountType = accountType;
	}

	@Override
	public void run() {
		try {
			if (localId != null) {
				log.log(Level.WARNING, "adding account to index");
				addToIndex();
			} else {
				removeFromIndex();
			}
			addAccountToIndex();
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				// retry putting the document
				throw e;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "error", e);
			e.printStackTrace();
		}
	}

	private void addAccountToIndex() {
		AccountDelegator ac = new AccountDelegator("auth=" + getToken());
		AccountSearchIndex.scheduleAccountTask(ac.getContactDetails(getLocalId()));
	}

	private void removeFromIndex() {
		ArrayList<String> docIds = new ArrayList<String>();
		docIds.add("account:"+getLocalId());
		getIndex().delete(docIds);
	}

	private void addToIndex() throws PutException {
		Document.Builder builder = Document.newBuilder()
				.setId("account:" + getLocalId())
				.addField(Field.newBuilder().setName("localId").setText("" + getLocalId()))
				.addField(Field.newBuilder().setName("accountType").setNumber(getAccountType()))
				.addField(Field.newBuilder().setName("accountName").setText(getAccountName()));

		Document doc = builder.build();
		log.log(Level.WARNING, "doc is "+doc);
		getIndex().put(doc);
		log.log(Level.WARNING, "index is  "+getIndex());
	}

	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("account_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
}
