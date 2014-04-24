package org.celstec.arlearn2.cache;

import org.celstec.arlearn2.beans.account.Account;

import com.google.appengine.api.utils.SystemProperty;

public class AccountCache extends GenericCache {
	private static AccountCache instance;

	private static String CACHE_ACOUNTS_PREFIX = SystemProperty.applicationVersion.get()+"Accounts:CK";
	
	private AccountCache() {
	}

	public static AccountCache getInstance() {
		if (instance == null)
			instance = new AccountCache();
		return instance;

	}

	public void storeAccountValue(String key, Account account) {
		getCache().put(CACHE_ACOUNTS_PREFIX+key, account);
	}
	
	public Account getAccount(String key) {
		return (Account) getCache().get(CACHE_ACOUNTS_PREFIX+key);
	}

    public void removeAccount(String key) {
        getCache().remove(CACHE_ACOUNTS_PREFIX+key);
    }
	
}
