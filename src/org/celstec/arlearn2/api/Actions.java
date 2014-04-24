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

import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.User;

import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.codehaus.jettison.json.JSONException;


@Path("/actions")
public class Actions extends Service {
	private static final Logger logger = Logger.getLogger(Actions.class.getName());

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getActions(@HeaderParam("Authorization") String token,
                             @QueryParam("from") Long from,
                             @QueryParam("until") Long until,
                             @QueryParam("resumptionToken") String cursor,
                             @PathParam("runIdentifier") Long runIdentifier,
                             @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		ActionDelegator ad = new ActionDelegator(this);
        if (from == null && until == null) {
            return serialise(ad.getActionList(runIdentifier), accept);
        }
        return serialise(ad.getActionsFromUntil(runIdentifier, from, until, cursor), accept);
	}
	
	

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String action, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inAction = deserialise(action, Action.class, contentType);
		if (inAction instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inAction), accept);
		if (((Action) inAction).getTimestamp() == null) ((Action) inAction).setTimestamp(System.currentTimeMillis());
		Action act = (new ActionDelegator(token)).createAction((Action) inAction);
		return serialise(act, accept);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/notify/{account}/{runId}/{gId}")
	public void notify(
			@HeaderParam("Authorization") String token, 
			String action, 
			@PathParam("account") String account,
			@PathParam("runId") Long runId,
			@PathParam("gId") Long gId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		GeneralItemDelegator gd = new GeneralItemDelegator(token);
		GeneralItem gi = gd.getGeneralItem(runId, gId);
		if (gi != null && gi.getError() == null) {
			GeneralItemModification gim = new GeneralItemModification();
			gim.setModificationType(GeneralItemModification.VISIBLE);
			gim.setRunId(runId);
			gim.setGeneralItem(gi);
			
			ChannelNotificator.getInstance().notify(account, gim);	
		}
		
		
	}
	
	

}
