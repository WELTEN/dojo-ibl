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

public class NotificationEngine {

	private static NotificationEngine instance;
	
	
	private NotificationEngine() {
	}
	
	public static NotificationEngine getInstance() {
		if (instance == null) instance = new NotificationEngine();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		for (NotificationChannel c: getNotificationChannels(account)) {
			c.notify(account, bean);
		}
	}
	
	private NotificationChannel[] getNotificationChannels(String account) {
		NotificationChannel[] result;
		int amount  = 1;
		if (account.equals("arlearn1")) {
			amount++;
		}
		result = new NotificationChannel[amount];
		result[0] = ChannelNotificator.getInstance();
		if (account.equals("arlearn1")) {
			result[1] = APNNotificationChannel.getInstance();

		}
		return result;
	}

}
