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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.notification.Pong;
import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@Path("/channelAPI")
public class ChannelAPI extends Service{

	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/token")
	public String getToken(@HeaderParam("Authorization") String token)  {
		try {
		if (!validCredentials(token))
			return null;
		UsersDelegator qu = new UsersDelegator(token);
		String myAccount = qu.getCurrentUserAccount();
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return "{ \"token\": \""+channelService.createChannel(myAccount)+"\"}";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/token3m")
	public String getToken3m(@HeaderParam("Authorization") String token)  {
		try {
		if (!validCredentials(token))
			return null;
		UsersDelegator qu = new UsersDelegator(token);
		String myAccount = qu.getCurrentUserAccount();
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return "{ \"token\": \""+channelService.createChannel(myAccount,2)+"\"}";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/token2m")
	public String getShortToken(@HeaderParam("Authorization") String token)  {
		if (!validCredentials(token))
			return null;
		UsersDelegator qu = new UsersDelegator(token);
		String myAccount = qu.getCurrentUserAccount();
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return "clientId: "+myAccount+" token2m " + channelService.createChannel(myAccount, 2);

	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/run/{account}")
	public String getRun(@PathParam("account") String account)  {		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		Run r = new Run();
		r.setGameId(123l);
		r.setTitle("een game");
		channelService.sendMessage(new ChannelMessage(account, toJson(r)));
		return "sent";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/location/{from}/{to}/{lat}/{lng}")
	public String loc(@PathParam("from") String from,@PathParam("to") String to,
			@PathParam("lat") Double lat,@PathParam("lng") Double lng)  {		
		LocationUpdate lu = new LocationUpdate();
		lu.setLat(lat);
		lu.setLng(lng);
		lu.setRecepientType(LocationUpdate.MODERATOR);
		lu.setAccount(from);
		ChannelNotificator.getInstance().notify(to, lu.toString());
		return "ok";
	}
	
	//TODO make secure otherwise this can be used as a means to spy on participants
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/ping/{from}/{to}")
	public String ping(@PathParam("from") String from, @PathParam("to") String to) {
		Ping ping = new Ping();
		ping.setTimestamp(System.currentTimeMillis());
		ping.setFrom(from);
		ping.setTo(to);
		ChannelNotificator.getInstance().notify(to, ping.toString());
		return "ok";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/ping/{from}/{to}/{requestType}/{payload}")
	public String pingWithRequest(@PathParam("from") String from, @PathParam("to") String to,  
			@PathParam("requestType") int requestType, @PathParam("payload") String payload) {
		Ping ping = new Ping();
		ping.setTimestamp(System.currentTimeMillis());
		ping.setFrom(from);
		ping.setTo(to);
		ping.setRequestType(requestType);
		ping.setPayload(payload);
		ChannelNotificator.getInstance().notify(to, ping.toString());
		return "ok";
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/pong/{from}/{to}/{origRequest}/{origTimeStamp}")
	public String pong(String response, @PathParam("from") String from,@PathParam("to") String to, @PathParam("origTimeStamp") long origTimeStamp, @PathParam("origRequest") int origRequest ) {
		System.out.println("pong received "+response);
		Pong pong = new Pong();
		pong.setTimestamp(System.currentTimeMillis());
		pong.setFrom(from);
		pong.setTo(to);
		pong.setOrigTimeStamp(origTimeStamp);
		pong.setResponse(response);
		pong.setRequestType(origRequest);
		ChannelNotificator.getInstance().notify(to, pong.toString());
		return "ok";
	}

}
