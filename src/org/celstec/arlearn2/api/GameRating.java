package org.celstec.arlearn2.api;


import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshallers;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import org.celstec.arlearn2.beans.game.Rating;
import org.celstec.arlearn2.jdo.manager.GameAverageRatingManager;
import org.celstec.arlearn2.mappers.rating.RatingMapper;
import org.celstec.arlearn2.mappers.rating.RatingMarshaller;
import org.celstec.arlearn2.mappers.rating.RatingOutput;
import org.celstec.arlearn2.mappers.rating.RatingReducer;

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
@Path("/gameRating")
public class GameRating extends Service {

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/generateRatings")
    public String startRatings(
            @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        String id = MapReduceJob.start(
                MapReduceSpecification.of(
                        "Lom Map Reduce",
                        new DatastoreInput("RatingJDO", 2),
                        new RatingMapper(),
                        Marshallers.getStringMarshaller(),
                        new RatingMarshaller(),
                        new RatingReducer(),
                        new RatingOutput(2)),
                getSettings()
        );

        return "{ 'jobId':"+id+"}";
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @CacheControlHeader("no-cache")
    @Path("/gameId/{gameId}")
    public String getRating(@PathParam("gameId") Long gameId, @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
        return serialise(GameAverageRatingManager.getAverageRatingBean(gameId), accept);
    }


}
