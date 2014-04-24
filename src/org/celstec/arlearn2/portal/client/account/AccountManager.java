package org.celstec.arlearn2.portal.client.account;

import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;

import com.google.gwt.json.client.JSONValue;

public class AccountManager {
	
	private static AccountManager instance;
	
	private Account account;
	private NotifyAccountLoaded notification = null;
	
	private AccountManager() {
		loadAccount();
	}
	
	public static AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}
	
	private void loadAccount() {
		AccountClient.getInstance().accountDetails(new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				account = new Account(jsonValue.isObject());	
				if (jsonValue.isObject().containsKey("error")) {
					if (notification != null) notification.accountLoaded(false);
				} else {
					if (notification != null) notification.accountLoaded(true);	
				}
				
			}			
		});
	}
	
	public Account getAccount() {
		return account;
	}
	
	public boolean isAdvancedUser() {
		if (account == null) return false;
		return account.isAdvancedUser();
	}
	
	public boolean isAdministrator() {
		if (account == null) return false;
		return account.isAdministrator();
	}
	
	public void setAccountNotification (NotifyAccountLoaded notification) {
		this.notification = notification;
	}
	
	public interface NotifyAccountLoaded {
		public void accountLoaded(boolean success);
	}

}
