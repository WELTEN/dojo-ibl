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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.LocationDelegator;


@Path("/location")
public class Location extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String location(@HeaderParam("Authorization") String token, String locationsString, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		LocationUpdate inLoc = (LocationUpdate) deserialise(locationsString, LocationUpdate.class, contentType);
		LocationDelegator ld = new LocationDelegator(token);
		ld.processLocation(runIdentifier, inLoc.getLat(), inLoc.getLng());
		return locationsString;
		
	}

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("allowTrackLocation")
    public String allowTrackLocation(@HeaderParam("Authorization") String token,
                                     @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                     @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        LocationDelegator ld = new LocationDelegator(this);
        ld.allowTrackLocation(this.getAccount());

        return "{}";
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String submitUserLocation(@HeaderParam("Authorization") String token,
                                     @FormParam("lat") Double lat,
                                     @FormParam("lng") Double lng,
                                     @FormParam("time") Long time,
                                     @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                     @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        LocationDelegator ld = new LocationDelegator(this);
        ld.submitUserLocation(this.getAccount(), lat, lng, time);

        return "{}";
    }



    @GET
    @Path("/account/{account}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getLocation(@HeaderParam("Authorization") String token,
                                     @PathParam("account") String account,
                                     @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                     @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        return new LocationDelegator(this).getUserLocations(account);
    }
}
