package org.celstec.arlearn2.gwtcommonlib.client.network;


public class CollaborationClient extends GenericClient {
	
	private static CollaborationClient instance;
	
	private CollaborationClient() {
	}
	
	public static CollaborationClient getInstance() {
		if (instance == null) instance = new CollaborationClient();
		return instance;
	}
	
	public void getContactDetails(String addContactToken, final JsonCallback jcb) {
		invokeJsonGET("/getContact/addContactToken/"+addContactToken, jcb);
	}
	
	public void getContact(String accountId, final JsonCallback jcb) {
		invokeJsonGET("/getContact/"+accountId , jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "collaboration";
	}

	public void confirmAddContact(String addContactToken, JsonCallback jsonCallback) {
		invokeJsonGET("/confirmAddContact/"+addContactToken, jsonCallback);	
	}
	
	public void addContactViaEmail(String email, JsonCallback jsonCallback) {
		invokeJsonGET("/addContact/email/"+email, jsonCallback);
	}

	public void getContacts(long from, String resumptionToken, final JsonCallback jcb) {
		if (resumptionToken == null) {
			invokeJsonGET("/getContacts?from="+from, jcb);
		} else {
			invokeJsonGET("/getContacts?from="+from +"&resumptionToken="+resumptionToken, jcb);
		}
	}
	
}
