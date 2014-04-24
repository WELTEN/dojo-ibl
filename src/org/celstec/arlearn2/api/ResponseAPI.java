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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.delegators.ResponseDelegator;


@Path("/response")
public class ResponseAPI extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getAnswers(@HeaderParam("Authorization") String token, 
			@QueryParam("from") Long from,
			@QueryParam("until") Long until,
			@QueryParam("resumptionToken") String cursor,
			@PathParam("runIdentifier") Long runIdentifier, 
			@HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		ResponseDelegator rd = new ResponseDelegator(this);
		if (from == null && until == null) {
			return serialise(rd.getResponses(runIdentifier, null, null), accept);
		}
		return serialise(rd.getResponsesFromUntil(runIdentifier, from, until, cursor), accept);
	}

	// TODO work out and return responses
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/account/{account}")
	public String getAnswers(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("account") String account, @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		ResponseDelegator rd = new ResponseDelegator(this);
		return serialise(rd.getResponses(runIdentifier, null, account), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/itemId/{itemId}")
	public String getAnswers(@HeaderParam("Authorization") String token,
                             @QueryParam("from") Long from,
                             @QueryParam("until") Long until,
                             @QueryParam("resumptionToken") String cursor,
                             @PathParam("runIdentifier") Long runIdentifier, @PathParam("itemId") Long itemId, @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		ResponseDelegator rd = new ResponseDelegator(this);
        if (from == null && until == null) {
            return serialise(rd.getResponses(runIdentifier, itemId, null), accept);
        }
        return serialise(rd.getResponsesFromUntil(runIdentifier, itemId, from, until, cursor), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/itemId/{itemId}/account/{account}")
	public String getAnswers(@HeaderParam("Authorization") String token,
                             @QueryParam("from") Long from,
                             @QueryParam("until") Long until,
                             @QueryParam("resumptionToken") String cursor,
                             @PathParam("runIdentifier") Long runIdentifier, @PathParam("itemId") Long itemId, @PathParam("account") String account, @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		ResponseDelegator rd = new ResponseDelegator(this);
        if (from == null && until == null) {
            return serialise(rd.getResponses(runIdentifier, itemId, account), accept);
        }
        return serialise(rd.getResponsesFromUntil(runIdentifier, itemId, account, from, until, cursor), accept);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String rString,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inResponse = deserialise(rString, Response.class, contentType);
		if (inResponse instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inResponse), accept);
		Response r = (Response) inResponse;
		
		ResponseDelegator cr = new ResponseDelegator(this);
		if (r.getRevoked() != null && r.getRevoked()) {
			return serialise(cr.revokeResponse(r), accept);
		} else {
			return serialise(cr.createResponse(r.getRunId(), r), accept);
		}
	}
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/list")
	public String putList(@HeaderParam("Authorization") String token, String rString,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inResponse = deserialise(rString, ResponseList.class, contentType);
		if (inResponse instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inResponse), accept);
		ResponseList rl = (ResponseList) inResponse;
		
		for (Response r: rl.getResponses()) {
			put(token, r.toString(), contentType, accept);
		}
		return "{}";
	}
	

}
