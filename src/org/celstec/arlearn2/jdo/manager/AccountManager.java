package org.celstec.arlearn2.jdo.manager;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountJDO;

import com.google.appengine.api.datastore.KeyFactory;

public class AccountManager {


    public static Account addAccount(Account account) {
        boolean allowTrackLocation = false;
        if (account.getAllowTrackLocation()!=null) allowTrackLocation = account.getAllowTrackLocation();
        return toBean(addAccount(account.getLocalId(),account.getAccountType(), account.getEmail(), account.getGivenName(), account.getFamilyName(), account.getName(), account.getPicture(), allowTrackLocation));
    }
	public static AccountJDO addAccount(String localID, int accountType,
			String email, String given_name, String family_name, String name,
			String picture, boolean allowTrackLocation) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			try {
				AccountJDO account = pm.getObjectById(AccountJDO.class,
						KeyFactory.createKey(AccountJDO.class.getSimpleName(),
								accountType + ":" + localID));
				account.setLocalId(localID);
				account.setAccountType(accountType);
				account.setUniqueId();
				account.setEmail(email);
				account.setGiven_name(given_name);
				account.setFamily_name(family_name);
				account.setName(name);
				account.setPicture(picture);
				account.setLastModificationDate(System.currentTimeMillis());
                account.setAllowTrackLocation(allowTrackLocation);
				return account;
			} catch (Exception e) {

			}

			AccountJDO account = new AccountJDO();
			account.setLocalId(localID);
			account.setAccountType(accountType);
			account.setUniqueId();
			account.setEmail(email);
			account.setGiven_name(given_name);
			account.setFamily_name(family_name);
			account.setName(name);
			account.setPicture(picture);
			account.setLastModificationDate(System.currentTimeMillis());
			account.setAccountLevel(AccountJDO.USER);
            account.setAllowTrackLocation(allowTrackLocation);
			pm.makePersistent(account);
			return account;
		} finally {
			pm.close();
		}
	}

	public static Account getAccount(Account myAccount) {
		return (getAccount(myAccount.getAccountType() + ":"+ myAccount.getLocalId()));
	}

	static Account getAccount(PersistenceManager pm, int type, String localId) {
		return (getAccount(type+ ":" + localId));
	}
	public static Account getAccount(String accountId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(pm.getObjectById(AccountJDO.class, KeyFactory
					.createKey(AccountJDO.class.getSimpleName(), accountId)));
		} catch (Exception e) {
			Account account = new Account();
			account.setError("Account does not exist");
			return account;
		} finally {
			pm.close();
		}
	}

	public static Account toBean(AccountJDO jdo) {
		if (jdo == null)
			return null;
		Account account = new Account();
		account.setLocalId(jdo.getLocalId());
		account.setAccountType(jdo.getAccountType());
		account.setEmail(jdo.getEmail());
		account.setName(jdo.getName());
		account.setFamilyName(jdo.getFamily_name());
		account.setGivenName(jdo.getGiven_name());
		account.setPicture(jdo.getPicture());
		account.setAccountLevel(jdo.getAccountLevel());
        account.setAllowTrackLocation(jdo.getAllowTrackLocation());
        if (account.getAllowTrackLocation()== null)  account.setAllowTrackLocation(false);
		return account;
	}

	public static void makeSuper(String accountId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.getObjectById(AccountJDO.class, KeyFactory
					.createKey(AccountJDO.class.getSimpleName(), accountId)).setAccountLevel(Account.ADMINISTRATOR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
	}

	

}
