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
package org.celstec.arlearn2.jdo.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ContactJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.KeyFactory;

public class ContactManager {
	
	private static final int ACCOUNTS_IN_LIST = 10;

	public static ContactJDO addContactInvitation(String localID, int accountType, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ContactJDO contact = new ContactJDO();
		contact.setFromAccountType(accountType);
		contact.setFromLocalId(localID);
		contact.setStatus(ContactJDO.PENDING);
		contact.setToEmail(email);
		contact.setUniqueId(UUID.randomUUID().toString());
		contact.setLastModificationDate(System.currentTimeMillis());

		try {
			pm.makePersistent(contact);
			return contact;
		} finally {
			pm.close();
		}
	}

	public static Account getContactViaId(String addContactToken) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ContactJDO jdo = pm.getObjectById(ContactJDO.class, KeyFactory.createKey(ContactJDO.class.getSimpleName(), addContactToken));
			if (jdo == null) return null;
			Account account = new Account();
			account.setLocalId(jdo.getFromLocalId());
			account.setAccountType(jdo.getFromAccountType());
			return account;
		} catch (Exception ex){
			Account account = new Account();
			account.setError("could not retrieve this account");
			return account;
		}finally {
			pm.close();
		}
	}
	
	public static Account getContactViaAcountId(String accountId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ContactJDO jdo = pm.getObjectById(ContactJDO.class, KeyFactory.createKey(ContactJDO.class.getSimpleName(), accountId));
			if (jdo == null) return null;
			Account account = new Account();
			account.setLocalId(jdo.getFromLocalId());
			account.setAccountType(jdo.getFromAccountType());
			return account;
		} finally {
			pm.close();
		}
	}

	public static void addContact(Account fullAccount, Account targetAccount, String addContactToken) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			createContact(pm, fullAccount, targetAccount);
			createContact(pm, targetAccount, fullAccount);
			ContactJDO jdo = pm.getObjectById(ContactJDO.class, KeyFactory.createKey(ContactJDO.class.getSimpleName(), addContactToken));
			pm.deletePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	private static void createContact(PersistenceManager pm, Account fullAccount, Account targetAccount) {
		ContactJDO contact = new ContactJDO();
		contact.setFromAccountType(fullAccount.getAccountType());
		contact.setFromLocalId(fullAccount.getLocalId());
		contact.setStatus(ContactJDO.ACCEPTED);
		contact.setToAccountType(targetAccount.getAccountType());
		contact.setToLocalId(targetAccount.getLocalId());
		contact.setUniqueId();
		contact.setLastModificationDate(System.currentTimeMillis());
		pm.makePersistent(contact);

	}
	
	public static AccountList getContacts(Account myAccount, Long from, Long until, String cursorString) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AccountList returnList = new AccountList();
		try {
			Query query = pm.newQuery(ContactJDO.class);
			if (cursorString != null) {

				Cursor c = Cursor.fromWebSafeString(cursorString);
				Map<String, Object> extendsionMap = new HashMap<String, Object>();
				extendsionMap.put(JDOCursorHelper.CURSOR_EXTENSION, c);
				query.setExtensions(extendsionMap);
			}
			query.setRange(0, ACCOUNTS_IN_LIST);
			String filter = null;
			String params = null;
			Object args[] = null;
			if (from == null) {
				filter = "fromAccountType == accountTypeParam & fromLocalId == localIdParam & lastModificationDate <= untilParam";
				params = "Long accountTypeParam, String localIdParam, Long untilParam";
				args = new Object[] { myAccount.getAccountType() };
			} else if (until == null) {
				filter = "fromAccountType == accountTypeParam & fromLocalId == localIdParam & lastModificationDate >= fromParam";
				params = "Integer accountTypeParam, String localIdParam, Long fromParam";
				args = new Object[] { myAccount.getAccountType(), myAccount.getLocalId(), from };
			} else {
				filter = "fromAccountType == accountTypeParam & fromLocalId == localIdParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
				params = "Integer accountTypeParam, String localIdParam, Long fromParam, Long untilParam";
				args = new Object[] { myAccount.getAccountType(), myAccount.getLocalId(), from, until };
			}

			query.setFilter(filter);
			query.declareParameters(params);
			List<ContactJDO> results = (List<ContactJDO>) query.executeWithArray(args);
			Iterator<ContactJDO> it = (results).iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				ContactJDO object = it.next();
				AccountManager.getAccount(myAccount);
				if (object.getToLocalId() != null) {
					returnList.addAccount(AccountManager.getAccount(pm,object.getToAccountType(), object.getToLocalId()));
				}
				
			}
			Cursor c = JDOCursorHelper.getCursor(results);
			cursorString = c.toWebSafeString();
			if (i == ACCOUNTS_IN_LIST) {
				returnList.setResumptionToken(cursorString);
			}
			returnList.setServerTime(System.currentTimeMillis());


		}finally {
			pm.close();
		}
		return returnList;
		
	}
}
