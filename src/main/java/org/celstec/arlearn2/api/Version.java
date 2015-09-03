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
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.jdo.manager.VersionManager;


@Path("/version")
public class Version extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String newVersion(String versionString, @DefaultValue("application/json") @HeaderParam("Accept") String accept)   {
		Object vObject = deserialise(versionString, org.celstec.arlearn2.beans.Version.class, accept);
		if (vObject instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) vObject), accept);
		org.celstec.arlearn2.beans.Version v = (org.celstec.arlearn2.beans.Version) vObject;
		return serialise(VersionManager.addVersion(v), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{versionCode}")
	public String get(@HeaderParam("Authorization") String token, @PathParam("versionCode") Integer versionCode, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			  {
		return serialise(VersionManager.getVersion(versionCode), accept);
	}

    @GET
    @Produces({ MediaType.TEXT_PLAIN})
    @Path("/time")
    public String getServerTime() {
        return ""+System.currentTimeMillis();
    }
}
