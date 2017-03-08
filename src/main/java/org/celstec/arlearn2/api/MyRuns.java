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

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.OpenBadgeAssertion;
import org.celstec.arlearn2.beans.run.GeneralItemsStatus;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.GeneralItemStatusDelegator;
import org.celstec.arlearn2.delegators.RunAccessDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.RunManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;


@Path("/myRuns")
public class MyRuns extends Service {
	private static final Logger logger = Logger.getLogger(MyRuns.class.getName());

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getRuns(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.getRuns(), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/runAccess")
	public String getGameAccess(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		if (from == null) {
			from = 0l;
		}
		RunAccessDelegator qg = new RunAccessDelegator(this);
		return serialise(qg.getRunsAccess(from, until), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/participate")
	public String getParticipateRuns(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		if (from == null && until == null) {
			return serialise(rd.getParticipateRuns(), accept);	
		}
		return serialise(rd.getParticipateRuns(from, until), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/participate/gameId/{gameId}")
	public String getParticipateRunsWithGameId(
			@HeaderParam("Authorization") String token,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@QueryParam("from") Long from,
//			@QueryParam("until") Long until,
			@PathParam("gameId") Long gameId
	)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
//		if (from == null && until == null) {
			return serialise(rd.getParticipateRuns(gameId), accept);
//		}
//		return serialise(rd.getParticipateRuns(from, until), accept);
	}
//
//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/gameId/{gameId}")
//	public String getRunsWithGameId(
//			@HeaderParam("Authorization") String token,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
//			@PathParam("gameId") Long gameId
//	)  {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		RunDelegator rd = new RunDelegator(this);
//			return serialise(rd.getParticipateRuns(gameId), accept);
//	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/tagId/{tagId}")
	public String getRunsByTag(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("tagId") String tagId
			)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		
		
		return serialise(rd.getTaggedRuns(tagId), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/access/runId/{runIdentifier}/account/{account}/accessRight/{accessRight}")
	public String access(@HeaderParam("Authorization") String token, 
			@PathParam("runIdentifier") Long runIdentifier, 
			@PathParam("account") String account, 
			@PathParam("accessRight") Integer accessRight, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunAccessDelegator rd = new RunAccessDelegator(this);
		rd.provideAccessWithCheck(runIdentifier, account, accessRight);
		return "{}";
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/removeAccess/runId/{runIdentifier}/account/{account}")
	public String removeAccess(@HeaderParam("Authorization") String token,
							   @PathParam("runIdentifier") Long runIdentifier,
							   @PathParam("account") String account,
							   @PathParam("accessRight") Integer accessRight,
							   @DefaultValue("application/json") @HeaderParam("Accept") String accept)
	{
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		RunAccessDelegator rad = new RunAccessDelegator(token);
		rad.removeAccessWithCheck(runIdentifier, account);
		return "{}";
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getRun(@HeaderParam("Authorization") String token,
						 @PathParam("runIdentifier") Long runIdentifier,
						 @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		RunDelegator rd = new RunDelegator(this);
		Run r = rd.getRun(runIdentifier);
		if (r.getRunConfig()!= null &&r.getRunConfig().getSelfRegistration()!=null) {
			if (r.getRunConfig().getSelfRegistration()) {
				return serialise(r, accept);
			}
		}
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		return serialise(r, accept);
	}


	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/code/{code}")
	public String getRunByCode(@HeaderParam("Authorization") String token,
						 @PathParam("code") String code,
						 @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		RunDelegator rd = new RunDelegator(this);
		Run r = rd.getRun(code);
		if (r.getRunConfig()!= null &&r.getRunConfig().getSelfRegistration()!=null) {
			if (r.getRunConfig().getSelfRegistration()) {
				return serialise(r, accept);
			}
		}
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		return serialise(r, accept);
	}
	
	
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/config/runId/{runIdentifier}")
	public String getConfig(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		Config c = rd.getConfig(runIdentifier);
		return serialise(c, accept);
	}
	
//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/selfRegister/tagId/{tagId}")
//	public String selfRegister(@HeaderParam("Authorization") String token,
//			@PathParam("tagId") String tagId,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
//
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		RunDelegator rd = new RunDelegator(this);
//		return serialise(rd.selfRegister(tagId), accept);
//	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/selfRegister/runId/{runId}")
	public String selfRegisterWithRunId(@HeaderParam("Authorization") String token, 
			@PathParam("runId") Long runId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.selfRegister(runId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public String getRuns(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept)  {
        // TODO
//        if (!validCredentials(token))
//            return serialise(getInvalidCredentialsBean(), accept);
//        RunDelegator rd = new RunDelegator(this);
//        return serialise(rd.getRunsForGame(Long.parseLong(gameIdentifier), account), accept);
        return "{}";
    }

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String runString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.createRun(run), accept);
	}
	
	@PUT
	@Path("/runId/{runIdentifier}")
	public String updateRun(@HeaderParam("Authorization") String token, String runString,
			@PathParam("runIdentifier") Long runIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.updateRun(run, runIdentifier), accept);
	}

	@DELETE
	@Path("/runId/{runIdentifier}")
	public String deleteRun(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("gameIdentifier") String gameIdentifier,
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(this);
		return serialise(rd.deleteRun(runIdentifier), accept);
	}
	
	@GET
	@Path("/badge/{runIdentifier}/{itemId}/{userId}")
	 @Produces("application/json")
	public String getBadge(@PathParam("runIdentifier") Long runId, @PathParam("itemId") String itemId, @PathParam("userId") String email) {
		List<Run> runList = RunManager.getRuns(runId, null, null, null, null, null);
		if (runList.isEmpty())
			return null;
		Run r = runList.get(0);
		AccountDelegator ad = new AccountDelegator(this);
		Account account = ad.getContactDetails(email);
//		List<User> users = UserManager.getUserList(null, email, null, runId);
//		if (users.isEmpty())
//			return null;
		GeneralItemList gil = new GeneralItemList();
			gil.setGeneralItems(GeneralItemManager.getGeneralitems(r.getGameId(), itemId, null, null));
			GeneralitemsCache.getInstance().putGeneralItemList(gil, r.getGameId(), itemId, null);
		if (gil == null || gil.getGeneralItems() == null || gil.getGeneralItems().isEmpty()) {
			return null;
		}
		GeneralItem gi = gil.getGeneralItems().get(0);
		OpenBadge ob = (OpenBadge) gi;
		OpenBadgeAssertion ou = new OpenBadgeAssertion();
		ou.setRecipient(account.getEmail());
		ou.setEvidence(ob.getEvidence());
//		ou.setExpires("2013-06-01");
//		ou.setIssued_on("2012-10-11");
		ou.setBadge_name(ob.getName());
		ou.setBadge_image(ob.getImage());
		ou.setBadge_description(ob.getDescription());
		ou.setBadge_criteria("/badges/html5-basic");
		ou.setBadge_issuer_origin(ob.getBadgeUrl());
		ou.setBadge_issuer_name("celstec");
		return serialise(ou, "application/json").replace("\\/", "/");
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}/generalItem/{generalItemId}/status/{status}")
	public String setStatusGiItem(@HeaderParam("Authorization") String token, String generalItemStatusString,
								  @PathParam("generalItemId") Long generalItemId,
								  @PathParam("runId") Long runId,
								  @PathParam("status") Integer status,
								  @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
								  @DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		Object ingeneralItemStatusString = deserialise(generalItemStatusString, GeneralItemsStatus.class, contentType);
		if (ingeneralItemStatusString instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) ingeneralItemStatusString), accept);

		GeneralItemStatusDelegator gisd = new GeneralItemStatusDelegator(token);
		return serialise(gisd.changeItemStatus((GeneralItemsStatus) ingeneralItemStatusString), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}/generalItem/{generalItemId}/status")
	public String getStatusGiItem(@HeaderParam("Authorization") String token,
								  @PathParam("generalItemId") Long generalItemId,
								  @PathParam("runId") Long runId,
								  @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		GeneralItemStatusDelegator gisd = new GeneralItemStatusDelegator(token);

		GeneralItemsStatus generalItemsStatus = gisd.getItemStatus(runId, generalItemId);
		return serialise(generalItemsStatus, accept);
	}
}
