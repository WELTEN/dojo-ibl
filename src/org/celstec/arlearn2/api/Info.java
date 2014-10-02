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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


@Path("/info")
public class Info extends Service {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getInfo( @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
		org.celstec.arlearn2.beans.Info info = new org.celstec.arlearn2.beans.Info();
		info.setVersion("arlearn-testing-1.2.11");
		info.setTimestamp(System.currentTimeMillis());
		return serialise(info, accept);
	}

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/domain")
    public String getDomain( @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        String result = "";
        try {
            URL url = new URL("http://inquiry.wespot.net/services/api/rest/xml/?method=test.domain");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                result +=line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;
    }
}
