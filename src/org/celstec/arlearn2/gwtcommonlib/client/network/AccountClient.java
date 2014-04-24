package org.celstec.arlearn2.gwtcommonlib.client.network;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;

public class AccountClient  extends GenericClient {
	
	private static AccountClient instance;
	
	private AccountClient() {
	}
	public String getUrl() {
		return super.getUrl() + "account";
	}
	
	public static AccountClient getInstance() {
		if (instance == null) instance = new AccountClient();
		return instance;
	}
	
	public void accountDetails(final JsonCallback jcb) {
		invokeJsonGET("/accountDetails", jcb);
	}
	
	public void createAnonymousContact(Account newGame, JsonCallback jsonCallback) {
		invokeJsonPOST("/createAnonymousContact", newGame.getJsonRep(), jsonCallback);
	}
	
	
}
