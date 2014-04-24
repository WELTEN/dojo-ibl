package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonResumptionListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class ContactsDataSource extends GenericDataSource {

	public static ContactsDataSource instance;
	
	public static ContactsDataSource getInstance() {
		if (instance == null)
			instance = new ContactsDataSource();
		return instance;
	}
	
	private ContactsDataSource() {
		super();
		setDataSourceModel(new ContactModel(this));
	}
	
	public GenericClient getHttpClient() {
		return CollaborationClient.getInstance();
	}

	public void loadDataFromWeb() {
		final long lastSyncDateLocal = ContactsDataSource.this.lastSyncDate;
			JsonResumptionListCallback callback = new JsonResumptionListCallback(getBeanType(), this.getDataSourceModel(), 0l) {

				@Override
				public void nextCall() {
					((CollaborationClient) getHttpClient()).getContacts(lastSyncDateLocal, resumptionToken, this);

				}

			};
		((CollaborationClient) getHttpClient()).getContacts(lastSyncDateLocal, null, callback);
		AccountClient.getInstance().accountDetails(new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				getDataSourceModel().addJsonObject(jsonValue.isObject());
			}
		});
	}

	protected String getBeanType() {
		return "accountList";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}

}
