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

import org.celstec.arlearn2.beans.run.GeneralItemVisibility;
import org.celstec.arlearn2.delegators.GeneralItemVisibilityDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;

@Path("/generalItemsVisibility")
public class GeneralItemsVisibility extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getVisibilityStatements(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept, @QueryParam("from") Long from, @QueryParam("until") Long until)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemVisibilityDelegator vidDel = new GeneralItemVisibilityDelegator(token);
		UsersDelegator ud = new UsersDelegator(vidDel);
		String email = ud.getCurrentUserAccount();
		return serialise(vidDel.getVisibleItems(runIdentifier, email, from, until), accept);
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/account/{account}")
	public String setVisibilityStatus(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType, @PathParam("runIdentifier") Long runIdentifier, @PathParam("account") String account, String visiblityStatement,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemVisibilityDelegator vidDel = new GeneralItemVisibilityDelegator(token);
		Object statement = deserialise(visiblityStatement, GeneralItemVisibility.class, contentType);
		if (statement instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) statement), accept);
		return serialise(vidDel.makeItemVisible(runIdentifier, account, (GeneralItemVisibility) statement), accept);

	}

}
