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
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//import org.celstec.arlearn2.beans.BeanDeserialiser;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.cache.CSVCache;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
//import org.celstec.arlearn2.delegators.generalitems.CreateGeneralItems;
//import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;
import org.celstec.arlearn2.tasks.beans.UpdateMdHash;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


@Path("/generalItems")
public class GeneralItems extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public String getArtifacts(
			@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until)
			 {
                 GameDelegator qg = new GameDelegator(account, token);
                 Game g = qg.getGame(gameIdentifier);
                 if (g.getSharing() == null || g.getSharing() == Game.PRIVATE) {
                     if (!validCredentials(token))
                         return serialise(getInvalidCredentialsBean(), accept);
                 }
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
                GeneralItemDelegator gid = new GeneralItemDelegator(token);
                if (from == null && until == null) {
                    return serialise(gid.getGeneralItems(gameIdentifier), accept);
                } else {
                    return serialise(gid.getGeneralItems(gameIdentifier, from, until), accept);
                }
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String gi, @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
					  @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token)){
			return serialise(getInvalidCredentialsBean(), accept);
		}


		Object inItem = deserialise(gi, GeneralItem.class, contentType);
		if (inItem instanceof java.lang.String){
			return serialise(getBeanDoesNotParseException((String) inItem), accept);
		}

		try {
			inItem = deserialise(gi, Class.forName(((GeneralItem) inItem).getType()), contentType);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		String result = serialise(gid.createGeneralItem((GeneralItem) inItem), accept);
		return result;
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/search")
	public String search(
			@HeaderParam("Authorization") String token, 
			String query, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		GeneralItemList gl = new GeneralItemList();
		GeneralItem gi1 = new GeneralItem();
		gi1.setName("query result 1");
		gi1.setDescription("this is a first description");
		gi1.setId(1234567890123l);
		gl.addGeneralItem(gi1);
		GeneralItem gi = new GeneralItem();
		gi.setName("query result 1");
		gi.setDescription("this is a first description");
		gi.setId(1234567890123l);
		gl.addGeneralItem(gi);
		return serialise(gl, accept);
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}/generalItem/{itemId}")
	public String getArtifact(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("itemId") Long itemId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
//		QueryGeneralItems gi = new QueryGeneralItems(token);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);

		for (GeneralItem item : gid.getGeneralItems(gameIdentifier).getGeneralItems()) {
			if (item.getId().equals(itemId)) {
				return serialise(item, accept);
			}
			
		}
		GeneralItem giError = new GeneralItem();
		giError.setError("id " + itemId + " does not exist");
		return serialise(giError, accept);
	}
	
	@DELETE
	@Path("/gameId/{gameIdentifier}/generalItem/{itemId}")
	public String deleteItem(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("itemId") String itemId, 
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		return serialise(gid.deleteGeneralItem(gameIdentifier, itemId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getRunGeneralItems(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		return serialise(gid.getItems(runIdentifier, email, GeneralItemVisibilityManager.VISIBLE_STATUS), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/disappeared")
	public String getRunGeneralItemsDisappeared(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		return serialise(gid.getItems(runIdentifier, email, GeneralItemVisibilityManager.DISAPPEARED_STATUS), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/generalItem/{itemId}")
	public String getRunGeneralItem(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier,@PathParam("itemId") Long itemId,  @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		
		for (GeneralItem item : gid.getNonPickableItems(runIdentifier, email).getGeneralItems()) {
			if (item.getId().equals(itemId)) return serialise(item, accept);
			
		}
		GeneralItem giError = new GeneralItem();
		giError.setError("id "+itemId+" does not exist");
		return serialise(giError, accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/all")
	public String getRunGeneralItemsAll(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
//		GeneralItemDelegator gid = new GeneralItemDelegator(token);
//		UsersDelegator ud = new UsersDelegator(gid);
		GeneralItemList gil = new GeneralItemList();
		
		return serialise(gil, accept);
	}



	@GET
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/cleanUpFilePaths/gameId/{gameIdentifier}")
	public String cleanUpFilePaths(@HeaderParam("Authorization") String token,
										@PathParam("gameIdentifier") Long gameIdentifier,
										@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		return serialise(gid.cleanUpFilePaths(gameIdentifier), accept);
	}

    @GET
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/createDummy/gameId/{gameIdentifier}")
    public String createDummyItem(@HeaderParam("Authorization") String token,
                           @PathParam("gameIdentifier") Long gameIdentifier,
                           @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
    @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        GeneralItemDelegator gid = new GeneralItemDelegator(token);
        return serialise(gid.createDummyItem(gameIdentifier), accept);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/gameId/{gameIdentifier}/generalItem/{itemId}")
    public String postWithDetails(@HeaderParam("Authorization") String token, String gi,
                                  @PathParam("gameIdentifier") Long gameIdentifier,
                                  @PathParam("itemId") String itemId,
                                  @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                      @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inItem = deserialise(gi, GeneralItem.class, contentType);
        if (inItem instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inItem), accept);
        try {
            inItem = deserialise(gi, Class.forName(((GeneralItem) inItem).getType()), contentType);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        GeneralItemDelegator gid = new GeneralItemDelegator(token);
        return serialise(gid.createGeneralItem((GeneralItem) inItem), accept);
    }

	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/search")
	public String search(@HeaderParam("Authorization") String token, 
			String searchQuery, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
			return serialise(gid.search(searchQuery), accept);
	}

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Path("/md5Hashes")
    public String md5Hashes(@HeaderParam("Authorization") String token,
                         String md5Hashes,
                         @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                         @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
                new UpdateMdHash(getToken(), getAccount(), md5Hashes).scheduleTask();

        return "{}";
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Path("/pictureUrl/gameId/{gameIdentifier}/generalItem/{itemId}/{key}")
    public String getPictureUploadUrl(@HeaderParam("Authorization") String token,
                                      @PathParam("gameIdentifier") Long gameId,
                                      @PathParam("itemId") Long itemId,
                                      @PathParam("key") String key,
                                      @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                      @DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String url = blobstoreService.createUploadUrl("/uploadGameContent/generalItems/"+itemId+"/"+key + "?gameId=" + gameId);
        return "{ 'uploadUrl': '"+url+"'}";
    }

}
