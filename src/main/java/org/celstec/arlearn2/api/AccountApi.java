package org.celstec.arlearn2.api;

//import com.google.gdata.util.AuthenticationException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.AccountManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/account")
public class AccountApi extends Service {
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/createAnonymousContact")
	public String createAnonymContact(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			String contact
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inContact = deserialise(contact, Account.class, contentType);
		if (inContact instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inContact), accept);
		AccountDelegator ad = new AccountDelegator(this);
		return serialise(ad.createAnonymousContact((Account) inContact), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/createAnonymousContact/{email}/{name}")
	public String createAnonymContact(@HeaderParam("Authorization") String token,
									  @PathParam("email") String email,
									  @PathParam("name") String name,
									  @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
									  @DefaultValue("application/json") @HeaderParam("Accept") String accept,
									  String contact
	)  {
		Account inContact = new Account();
		inContact.setPicture("");
		inContact.setEmail(email);
		inContact.setName(name);
		AccountDelegator ad = new AccountDelegator(this);
		return serialise(ad.createAnonymousContact((Account) inContact), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/anonymousLogin/{anonToken}")
	public String anonymousLogin( 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("anonToken") String anonToken
			) {
		Account account = AccountManager.getAccount("0:"+anonToken);
		AuthResponse ar = new AuthResponse();

		if (account != null && account.getError() == null) {
			ar.setAuth(UUID.randomUUID().toString());
			UserLoggedInManager.submitOauthUser("0:"+anonToken, ar.getAuth());		
			return serialise(ar, accept);
		}
		
		ar.setError("Authentication failed");
		return serialise(ar, accept);

	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/accountDetails")
	public String getContactDetails(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("addContactToken") String addContactToken
			) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		AccountDelegator ad = new AccountDelegator(this);
		return serialise(ad.getContactDetails(this.account.getFullId()), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/accountDetails/{accountFullId}")
	public String getContactDetailsForId(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("accountFullId") String accountFullId

			) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		AccountDelegator ad = new AccountDelegator(this);
		String myAccount = UserLoggedInManager.getUser(token);
		if (myAccount == null) {
			Account ac = new Account();
			ac.setError("account is not logged in");
		}
		return serialise(ad.getContactDetails(accountFullId), accept);
	}
	
//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@CacheControlHeader("no-cache")
//	@Path("/makesuper/{accountFullId}")
//	public String makesuper(@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@PathParam("accountFullId") String accountFullId
//
//			) {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//
//		AccountDelegator ad = new AccountDelegator(this);
//		String myAccount = UserLoggedInManager.getUser(token);
//		if (myAccount == null) {
//			Account ac = new Account();
//			ac.setError("account is not logged in");
//		}
//		ad.makeSuper(accountFullId);
//		return "{}";
//	}

    private static final Logger log = Logger.getLogger(AccountApi.class.getName());

    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/createAccount")
    public String createAccount(@HeaderParam("Authorization") String token,
                                      @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                      @DefaultValue("application/json") @HeaderParam("Accept") String accept,
                                      String contact
    ) {

        Object inContact = deserialise(contact, Account.class, contentType);
        log.log(Level.SEVERE, "registering account"+contact);

        if (inContact instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inContact), accept);
        AccountDelegator ad = new AccountDelegator(this);
        return serialise(ad.createAccount((Account) inContact, token), accept);
    }

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/search")
	public String search(@HeaderParam("Authorization") String token,
						 String searchQuery,
						 @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
						 @DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		AccountDelegator ad = new AccountDelegator(token);
		return serialise(ad.search(searchQuery), accept);
	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/url/account/{account}/{key}")
	public String getUploadUrl(@HeaderParam("Authorization") String token,
							   @PathParam("account") String account,
							   @PathParam("key") String key,
							   @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
							   @DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String url = blobstoreService.createUploadUrl("/uploadUserContent/"+key+"?account=" + account);

//		String url = blobstoreService.createUploadUrl("/uploadGameContent/generalItems/"+itemId+"/"+key + "?runId=" + runId);
		return "{ \"uploadUrl\": \""+url+"\"}";
	}
	
}
