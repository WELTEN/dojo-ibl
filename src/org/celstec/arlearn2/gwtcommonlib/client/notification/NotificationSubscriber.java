package org.celstec.arlearn2.gwtcommonlib.client.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.gwtcommonlib.client.network.ChannelClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.ui.modal.SessionTimeout;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class NotificationSubscriber {

	private static NotificationSubscriber instance;
	private static boolean error = false;

	private HashMap<String, List<NotificationHandler>> notificationMap = new HashMap<String, List<NotificationHandler>>();

	private ChannelCreatedCallback channelCreatedCallback = new ChannelCreatedCallback() {
		@Override
		public void onChannelCreated(Channel channel) {
			channel.open(new SocketListener() {
				@Override
				public void onOpen() {
					// Window.alert("Channel opened!");
				}

				@Override
				public void onMessage(String message) {
					System.out.println("message: "+message);
					dispatchMessage(message);
				}

				@Override
				public void onError(SocketError sError) {
					instance = null;
					error = true;
					SessionTimeout st = new SessionTimeout();
					st.show();
				}

				@Override
				public void onClose() {
					instance = null;
					if (!error)
						requestToken();
					// Window.alert("Channel closed!");
				}
			});
		}
	};

	private NotificationSubscriber() {
		requestToken();
	}

	public static NotificationSubscriber getInstance() {
		if (instance == null)
			instance = new NotificationSubscriber();
		return instance;
	}

	public void openChannel(String token) {
		ChannelFactory.createChannel(token, channelCreatedCallback);
	}

	public void requestToken() {
		ChannelClient.getInstance().getToken(new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				openChannel(jsonValue.isObject().get("token").isString().stringValue());
			}
		});
	}

	private void dispatchMessage(String message) {
		JSONValue jsonValue = JSONParser.parseLenient(message);
		JSONObject object = jsonValue.isObject();
		if (object != null && object.containsKey("type")) {
			List<NotificationHandler> handlers = notificationMap.get(object.get("type").isString().stringValue());
			if (handlers != null)
				for (NotificationHandler handler : handlers) {
					if (handler != null)
						handler.onNotification(object);
				}
		}
		List<NotificationHandler> handlers = notificationMap.get("all");
		if (handlers != null)
			for (NotificationHandler handler : handlers) {
				if (handler != null)
					handler.onNotification(object);
			}

	}

	public void addNotificationHandler(String type, NotificationHandler handler) {
		if (!notificationMap.containsKey(type)) {
			notificationMap.put(type, new ArrayList<NotificationHandler>());

		}
		notificationMap.get(type).add(handler);
	}

	public void removeAllHandlers() {

		notificationMap = new HashMap<String, List<NotificationHandler>>();
	}

	public void reload() {
		requestToken();

	}
}

