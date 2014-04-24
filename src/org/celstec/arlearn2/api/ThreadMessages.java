package org.celstec.arlearn2.api;

import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.delegators.ThreadDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
@Path("/messagesOld")
public class ThreadMessages extends Service {

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread")
    public String createThread(
            @HeaderParam("Authorization") String token,
            String thread,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inThread = deserialise(thread, org.celstec.arlearn2.beans.run.Thread.class, contentType);
        if (inThread instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inThread), accept);

        ThreadDelegator td = new ThreadDelegator(this);
        return serialise(td.createThread((Thread)inThread), accept);

    }

}
