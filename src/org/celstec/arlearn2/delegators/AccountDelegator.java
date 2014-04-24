package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.cache.AccountCache;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ApplicationKeyManager;

import java.util.UUID;

public class AccountDelegator extends GoogleDelegator {

	public AccountDelegator(String authToken) {
		super(authToken);
	}

	public AccountDelegator(Service service) {
		super(service);
	}
	public AccountDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public Account getAccountInfo(Account myAccount) {
		return AccountManager.getAccount(myAccount.getFullId());
		
	}

	public Account getContactDetails(String accountId) {
		Account account = AccountCache.getInstance().getAccount(accountId);
		if (account == null) {
			account = AccountManager.getAccount(accountId);
			if (account.getError() != null) return account;
			AccountCache.getInstance().storeAccountValue(account.getFullId(), account);
		}
		return account;
	}

	public Account createAnonymousContact(Account inContact) {
		String localID = UUID.randomUUID().toString();
		return AccountManager.toBean(AccountManager.addAccount(localID, 0, inContact.getEmail(), inContact.getGivenName(), inContact.getFamilyName(), inContact.getName(), inContact.getPicture(), false));
	}

	public void makeSuper(String accountId) {
		AccountManager.makeSuper(accountId);
	}

    public Account createAccount(Account account, String applicationKey) {
        if (account.getAccountType() == null) {
            account.setError("accountType attribute is missing");
            return account;
        }
        if (account.getLocalId() == null) {
            account.setError("localID attribute is missing");
            return account;
        }
        if (account.getEmail() == null) {
            account.setError("email attribute is missing");
            return account;
        }
        if (account.getName() == null) {
            account.setError("name attribute is missing");
            return account;
        }
        if (applicationKey.contains(":")) applicationKey = applicationKey.substring(0,applicationKey.indexOf(":"));
        boolean tokenExists = ApplicationKeyManager.getConfigurationObject(applicationKey);
        if (tokenExists) {
            return AccountManager.addAccount(account);
        }
        account.setError("token invalid");
        return account;
    }

}
