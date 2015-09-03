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
package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.delegators.NotificationDelegator;

@Path("/apn")
public class ApplePushNotifications extends Service {

	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String deviceDescString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inApnDesc = deserialise(deviceDescString, APNDeviceDescription.class, contentType);
		if (inApnDesc instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inApnDesc), accept);
		APNDeviceDescription description = (APNDeviceDescription) inApnDesc;
		(new NotificationDelegator(token)).registerDescription(description);
		return serialise(description, accept);
	}
	
	@POST
	@Path("/sendNotification/user/{account}")
	public String sendNotification(@HeaderParam("Authorization") String token, String text, 
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		(new NotificationDelegator(token)).sendNotification(account, text);

		return "sent";
	}
	
	@POST
	@Path("/json/user/{account}")
	public String notificationAsJson(@HeaderParam("Authorization") String token, String text, 
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept){
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		(new NotificationDelegator(token)).sendiOSNotificationAsJson(account, text);

		return "sent";
	}
	
}
