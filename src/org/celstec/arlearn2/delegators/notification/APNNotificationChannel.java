/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.delegators.notification;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescription;
import org.celstec.arlearn2.beans.notification.NotificationBean;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.jdo.manager.IOSDevicesRegistryManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class APNNotificationChannel implements NotificationChannel{
	private static APNNotificationChannel instance;
	
	
	private APNNotificationChannel() {
	}
	
	public static APNNotificationChannel getInstance() {
		if (instance == null) instance = new APNNotificationChannel();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		for (DeviceDescription desc: IOSDevicesRegistryManager.getDeviceTokens(account)){
			try {
				sendNotification(account, ((APNDeviceDescription) desc).getDeviceToken(), ((NotificationBean)bean));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendNotification(String account, String deviceToken, NotificationBean bean) throws JSONException {
		String text = "";
		bean.retainOnlyIdentifier();
	
		JSONObject json = new JSONObject(bean.toString());
		JSONObject aps = new JSONObject();
		aps.put("aps", json);
		json.put("alert", "notification");
		if (bean instanceof RunModification) {
			RunModification rm = (RunModification) bean;
			if (rm.getModificationType() == RunModification.CREATED) {
				json.put("alert", "new run available");		
			}
			if (rm.getModificationType() == RunModification.DELETED) {
				json.put("alert", "Run deleted");		
			}
		}
		json.put("sound", "default");
		text = aps.toString();
		System.out.println("tesxt "+text);
		
        try {
            URL url = new URL("http://sharetec.celstec.org/APN/not.php?deviceToken="+deviceToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(text);
            writer.close();
    
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
            } else {
                // Server returned HTTP error code.
            }
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
	}
}
