package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.GameDelegator;


@Path("/notifications")
public class NotificationAPI extends Service {

	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gcm")
	public String registerAndroidDevice( 
			@HeaderParam("Authorization") String token,
			String deviceDescription,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GCMDeviceDescription gcmDeviceBean = (GCMDeviceDescription) deserialise(deviceDescription, GCMDeviceDescription.class, contentType);
		new NotificationDelegator(account, token).registerDescription(gcmDeviceBean);
		return "{}";
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/apn")
	public String registeriOSDevice( 
			String deviceDescription,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		APNDeviceDescription apnDeviceBean = (APNDeviceDescription) deserialise(deviceDescription, APNDeviceDescription.class, contentType);
		new NotificationDelegator(this).registerDescription(apnDeviceBean);
		System.out.println("register device "+apnDeviceBean);

		return "{}";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/listDevices/account/{account}")
	public String getGamesParticipate(
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept
			)   {
		
		return serialise(new NotificationDelegator(this).listDevices(account), accept);
	}
	//{"aps":{"type":"org.celstec.arlearn2.beans.notification.GeneralItemModification","modificationType":7,"runId":74,"gameId":69,"itemId":71,"alert":"notification","sound":"default"}}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/notify/account/{account}")
	public String sendNotification( 
			String notification,
			@PathParam("account") String userId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		new NotificationDelegator(this).broadcast(notification, userId);

		return "{}";
	}
	
	
}
