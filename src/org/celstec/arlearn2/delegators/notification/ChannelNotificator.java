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


import java.util.logging.Logger;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@Deprecated
public class ChannelNotificator implements NotificationChannel{

	private static ChannelNotificator instance;
	private static final Logger log = Logger.getLogger(ChannelNotificator.class.getName());

	ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	
	private ChannelNotificator() {
	}
	
	public static ChannelNotificator getInstance() {
		if (instance == null) instance = new ChannelNotificator();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		if (account != null) notify(account, bean.toString());
	}
	
	public void notify(String account, String message) {
		if (account != null) channelService.sendMessage(new ChannelMessage(account, message));
	}
}
