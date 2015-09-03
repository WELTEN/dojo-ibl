package org.celstec.arlearn2.gwtcommonlib.client.network;


public class ChannelClient extends GenericClient {

	private static ChannelClient instance;
	private ChannelClient() {
	}
	
	public static ChannelClient getInstance() {
		if (instance == null) instance = new ChannelClient();
		return instance;
	}
	
	public void getToken(final JsonCallback jcb) {
		invokeJsonGET("/token", jcb);
	}
	
	public void ping(final JsonCallback jcb, String from, String to, String request) {
		invokeJsonGET("/ping/"+from+"/"+to+"/"+request, jcb);
	}
	
	public void pingRequest(final JsonCallback jcb, String from, String to, int requestType, String payload) {
		invokeJsonGET("/ping/"+from+"/"+to+"/"+requestType+"/"+payload, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "channelAPI";
	}
}
