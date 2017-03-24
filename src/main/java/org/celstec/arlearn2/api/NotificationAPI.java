package org.celstec.arlearn2.api;

import apns.*;
import apns.keystore.ClassPathResourceKeyStoreProvider;
import apns.keystore.KeyStoreProvider;
import apns.keystore.KeyStoreType;
import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.delegators.MailDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


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
//try {
//    PushNotification pushNotification = new PushNotification()
//            .setAlert("You got your emails.")
//            .setBadge(9)
//            .setSound("bingbong.aiff")
//            .setDeviceTokens("2c51a18edd40c19941efe3d9e7e9bd3cae59d0ee6031a161921702faeede2563");
//    PushNotificationService pns = new DefaultPushNotificationService();
//
//    DefaultApnsConnectionFactory.Builder builder = DefaultApnsConnectionFactory.Builder.get();
//
//    System.out.println("from arlearn for wim");
////        KeyStoreProvider ksp = new ClassPathResourceKeyStoreProvider("apns/pim.p12", KeyStoreType.PKCS12, "notnimda".toCharArray());
////        builder.setProductionKeyStoreProvider(ksp);
//
//    KeyStoreProvider ksp = new ClassPathResourceKeyStoreProvider("apns/arlearn.p12", KeyStoreType.PKCS12, "notnimda".toCharArray());
//    builder.setSandboxKeyStoreProvider(ksp);
//
//    ApnsConnectionFactory acf = builder.build();
//
//    ApnsConnection connection = acf.openPushConnection();
//    pns.send(pushNotification, connection);
//} catch (Exception e) {
//    e.printStackTrace();
//}
		new NotificationDelegator(this).broadcast(notification, userId);

		return "{}";
	}
	
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/test/deviceToken/{token}/apns/{apns}/{type}")
    public String testAPN(@PathParam("token") String account, @PathParam("apns") String apnString, @PathParam("type") String type) {
        try {
            PushNotification pushNotification = new PushNotification()
                    .setAlert("You got your emails.")
                    .setBadge(9)
                    .setSound("bingbong.aiff")
                    .setDeviceTokens(account);
            PushNotificationService pns = new DefaultPushNotificationService();

            DefaultApnsConnectionFactory.Builder builder = DefaultApnsConnectionFactory.Builder.get();

            System.out.println("from arlearn for wim");
            KeyStoreProvider ksp = null;
            if ("prod".equals(type)) {
                 ksp = new ClassPathResourceKeyStoreProvider("apns/"+apnString, KeyStoreType.PKCS12, "notnimda".toCharArray());
                 builder.setProductionKeyStoreProvider(ksp);
            } else {
                ksp = new ClassPathResourceKeyStoreProvider("apns/" + apnString, KeyStoreType.PKCS12, "notnimda".toCharArray());
            builder.setSandboxKeyStoreProvider(ksp);
        }
            ApnsConnectionFactory acf = builder.build();

            ApnsConnection connection = acf.openPushConnection();
            pns.send(pushNotification, connection);


            FeedbackService fs = new DefaultFeedbackService();
            List<FailedDeviceToken> failedTokens = fs.read(connection);

            for (FailedDeviceToken failedToken : failedTokens) {
                failedToken.getFailTimestamp();
                failedToken.getDeviceToken();
                System.out.println("feedback "+failedToken.getFailTimestamp() +" "+failedToken.getDeviceToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

	@POST
	@Path("/reminders")
	public String sendReminder(@HeaderParam("Authorization") String token,
							 @PathParam("account") String account,
							 @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		MailDelegator md = new MailDelegator(token);
//		md.sendReminders(returnMessage, ra.getAccount());
		return null;
	}
}
