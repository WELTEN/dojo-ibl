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

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Path("/gcm")
public class GoogleCloadMessaging extends Service{
	
	@POST
	@Path("/sendNotification/user/{account}")
	public String sendNotification(@HeaderParam("Authorization") String token, String text, 
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
//		(new ApplePushNotificationDelegator(token)).sendNotification(account, text);
		Sender sender = new Sender("AIzaSyBBHzixGmnJnu8YhZS44zCObl85JTspo_Q");
		Message message = new Message.Builder().addData("runId", "1234").build();
		
		try {
			Result result = sender.send(message, "APA91bFDiScakJJnxA9LfFNknB965TdghV5ep7w5ZGwOG51JJF_X3MBy3_set6mPPaYRK_ceWcK00JF9x6vW-vuDGB4dYqgevukafBYq8VgovwG8dyLK4QtbT123N_8_hmACIJ185JztOBvVLb-8AlDLJPG_I3gldQ", 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "sent";
	}
//	gcm/sendNotification/user/arlearn1
}
