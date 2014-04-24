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

import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.ResponseDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.location.QueryLocations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class Users extends Service {
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/update")
	public String updateUsers(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		UsersDelegator qu = verifyCredentials(token);
		if (qu != null) qu.updateAllUsers();
		return null;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String userString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inUser = deserialise(userString, User.class, contentType);
		if (inUser instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inUser), accept);
		User user = (User) inUser;
		 
		UsersDelegator qu = new UsersDelegator(this.account, token);
//		user.setFullEmail(user.getEmail());
		if (user.getEmail() == null) {
			user.setEmail(qu.getCurrentUserAccount());
		} 
			
		return serialise(qu.createUser(user), accept);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}/email/{email}")
	public String deleteUser(@HeaderParam("Authorization") String token, 
			@PathParam("runId") Long runId, 
			@PathParam("email") String email,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		UsersDelegator cu = new UsersDelegator(this.account, token);
		if (email == null){
			email = cu.getCurrentUserAccount();
		}
			
		ActionDelegator ad = new ActionDelegator(cu);
		ad.deleteActions(runId, email);
		ResponseDelegator rd = new ResponseDelegator(cu);
		rd.deleteResponses(runId, email);
		//TODO delete reponses by user
		//TODO delete score
		//TODO delete progress
		return serialise(cu.deleteUser(runId, email), accept);
	}
	
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}")
	public String deleteUser(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		return deleteUser(token, runId, null, accept);
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getUsers(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept){
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		UsersDelegator qu = new UsersDelegator(token);
		UserList ul = qu.getUsers(runIdentifier, null);
		return serialise(ul, accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/teamId/{teamId}")
	public String getUsersForTeam(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		UsersDelegator qu = new UsersDelegator(token);
		return serialise(qu.getUsers(runIdentifier, teamId), accept);
	}

	// TODO refactor what is below this line
	// ========= (line)

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/updateUser/{runIdentifier}/email/{email}")
	public String update(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("email") String email, String userString,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inUser = deserialise(userString, User.class, contentType);
		if (inUser instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inUser), accept);
		 User user = (User) inUser;
		
		// TODO store coordinates
//		SubmitLocations sl = new SubmitLocations(token);
//		Location l = new Location();
//		l.setLng(user.getLng());
//		l.setLat(user.getLat());
//		l.setTimestamp(System.currentTimeMillis());
//		sl.submitLocation(0l, l, runIdentifier);

		return serialise(user, accept);
		// return uu.updateUser(runIdentifier, email, user);

	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/locations/runId/{runIdentifier}")
	public String location(@HeaderParam("Authorization") String token, String locationsString, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inLocations = deserialise(locationsString, LocationList.class, contentType);
		if (inLocations instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inLocations), accept);
		LocationList locations = (LocationList) inLocations;
		
		if (locations.getLocations().size() >= 500) {
			LocationList error = new LocationList();
			error.setError("The maximum number of locations you can combine in a single request is 500.");

			return serialise(error, accept);

		}
		long delta = System.currentTimeMillis() - locations.getTimestamp();
		return serialise(locations, accept);

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/location/runId/{runIdentifier}/email/{userId}")
	public String getLocation(@HeaderParam("Authorization") String token, @PathParam("userId") String userId, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		QueryLocations ql = new QueryLocations(token);

		return serialise(ql.getLastLocation(userId, runIdentifier), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/loggedInUser/{runIdentifier}/email/{email}")
	@Deprecated
	public String getLoggedInUser(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("email") String email,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		UsersDelegator qu = new UsersDelegator(token);
		return serialise(qu.getUserByEmail(runIdentifier, email), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/account/{accountId}")
	public String getLoggedInUserViaAccount(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("accountId") String accountId,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
    {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		UsersDelegator qu = new UsersDelegator(token);
		return serialise(qu.getUserByEmail(runIdentifier, accountId), accept);
	}

}
