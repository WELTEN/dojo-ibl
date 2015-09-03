package org.celstec.arlearn2.api;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
@Path("/ecoapi")
public class EcoAPI extends Service {

    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/users/{id}/courses")
    public String getEcoCourses(
            @HeaderParam("Authorization") String token,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept,
            @PathParam("id") String id
    )  {

        JSONArray resultArray = new JSONArray();
        RunDelegator rd = new RunDelegator(this);


        try {
            for (Run r: rd.getRuns("6:"+id).getRuns()) {
                Game g = r.getGame();
                JSONObject gameObject = new JSONObject();
                gameObject.put("id", g.getGameId());
                resultArray.put(gameObject);
            }
            return resultArray.toString(5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "{}";

    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @CacheControlHeader("no-cache")
    @Path("/teachers/{accountFullId}")
    public String getECOTeacherId(@HeaderParam("Authorization") String token,
                                  @DefaultValue("application/json") @HeaderParam("Accept") String accept,
                                  @PathParam("accountFullId") String accountFullId

    ) {
        if (accountFullId.startsWith("urn:uuid:")) {
            accountFullId = accountFullId.trim().substring(9);
        }
//        if (!validCredentials(token))
//            return serialise(getInvalidCredentialsBean(), accept);

        AccountDelegator ad = new AccountDelegator(this);
        String myAccount = UserLoggedInManager.getUser(token);
        if (myAccount == null) {
            Account ac = new Account();
            ac.setError("account is not logged in");
        }
        Account account = ad.getContactDetails("6:"+accountFullId);
        JSONObject result = new JSONObject();
        try {
            result.put("name", account.getName());
            if (account.getPicture()!= null) result.put("imageUrl", account.getPicture());

            return result.toString(5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/heartbeat")
    public String heartbeat( @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        JSONObject result = new JSONObject();
        try {
            result.put("alive_at", df.format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
