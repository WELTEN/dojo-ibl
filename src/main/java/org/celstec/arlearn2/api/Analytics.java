package org.celstec.arlearn2.api;

import org.celstec.arlearn2.delegators.DojoAnalyticsDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;

/**
 * ****************************************************************************
 * Copyright (C) 2017 Open Universiteit Nederland
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
 * Contributors: Angel Suarez
 * Date: 09/10/17
 * ****************************************************************************
 */

@Path("/la")
public class Analytics extends Service {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public String put(@HeaderParam("Authorization") String token, String laStatement,
                      @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                      @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

//        Object inLaStatement = deserialise(laStatement, Game.class, contentType);
//        if (inLaStatement instanceof java.lang.String)
//            return serialise(getBeanDoesNotParseException((String) inLaStatement), accept);

        try {
            new DojoAnalyticsDelegator(this).registerStatement((String) laStatement);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "{\n" +
                "\t\"value\": \"ok\"\n" +
                "}";
    }

}
