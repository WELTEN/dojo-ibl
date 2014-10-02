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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.CollaborationDelegator;


@Path("/collaboration")
public class Collaboration extends Service {

	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/getContacts")
	public String getContacts(@HeaderParam("Authorization") String token, 
			@QueryParam("from") Long from,
			@QueryParam("until") Long until,
			@QueryParam("resumptionToken") String cursor,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		CollaborationDelegator cd = new CollaborationDelegator(token);
		return serialise(cd.getContacts(from, until, cursor), accept);
	}
	
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/addContact/email/{email}")
	public String addContactViaEmail(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("email") String email
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		CollaborationDelegator cd = new CollaborationDelegator(token);
		cd.addContactViaEmail(email);
		return null;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/getContact/addContactToken/{addContactToken}")
	public String getContactDetails(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("addContactToken") String addContactToken
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		CollaborationDelegator cd = new CollaborationDelegator(token);
		return serialise(cd.getContactDetails(addContactToken), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/getContact/{accountId}")
	public String getContactDetailsViaAccountId(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("accountId") String accountId
			)  {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
		AccountDelegator gd = new AccountDelegator(this);
		return serialise(gd.getContactDetails(accountId), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/confirmAddContact/{addContactToken}")
	public String confirmAddContact(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("addContactToken") String addContactToken
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		CollaborationDelegator cd = new CollaborationDelegator(token);
		return serialise(cd.confirmAddContact(addContactToken), accept);
	}
	
	

}
