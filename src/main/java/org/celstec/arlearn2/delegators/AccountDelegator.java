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
        if (applicationKey.contains(":")) applicationKey = applicationKey.substring(0, applicationKey.indexOf(":"));
        boolean tokenExists = ApplicationKeyManager.getConfigurationObject(applicationKey);

        if (tokenExists) {
            return AccountManager.addAccount(account);
        }
        account.setError("token invalid");
        return account;
    }
//
//    public Index getIndex() {
//        IndexSpec indexSpec = IndexSpec.newBuilder().setName("account_index").build();
//        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
//    }
//
//    public AccountList search(String searchQuery) {
//        try {
//            Results<ScoredDocument> results = getIndex().search(searchQuery);
//            AccountList resultsList = new AccountList();
//            for (ScoredDocument document : results) {
//                Account ac = new Account();
//                ac.setLocalId(document.getFields("localId").iterator().next().getText());
//                ac.setName(document.getFields("accountName").iterator().next().getText());
//                ac.setAccountType((int)document.getFields("accountType").iterator().next().getNumber().doubleValue());
//                resultsList.addAccount(ac);
//            }
//            return resultsList;
//        } catch (SearchException e) {
//            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
//                // retry
//            }
//        }
//        return null;
//    }


    public Account createAccountAndIndex(Account account) {
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

        return AccountManager.addAccount(account);
    }
}
