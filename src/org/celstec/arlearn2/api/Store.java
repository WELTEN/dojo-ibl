package org.celstec.arlearn2.api;

import com.google.appengine.tools.mapreduce.*;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.InMemoryOutput;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.CategoryDelegator;
import org.celstec.arlearn2.mappers.CountOutput;
import org.celstec.arlearn2.mappers.CountReducer;
import org.celstec.arlearn2.mappers.CountRunsMapper;
import org.celstec.arlearn2.mappers.lom.GamesMapper;
import org.celstec.arlearn2.mappers.lom.LomOutput;
import org.celstec.arlearn2.mappers.lom.LomReducer;

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
@Path("/store")
public class Store extends Service {


    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/category/id/{id}")
    public String getCategoryById(@PathParam("id") Long id,
                              @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        return serialise(new CategoryDelegator().getCategory(id), accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/categories/categoryId/{id}")
    public String getCategoryByCategoryId(@PathParam("id") Long id,
                                  @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        return serialise(new CategoryDelegator().getCategoryByCategoryId(id), accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/categories/lang/{lang}")
    public String getCategoryByLang(@PathParam("lang") String lang,
                                  @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        return serialise(new CategoryDelegator().getCategories(lang), accept);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/category")
    public String put(@HeaderParam("Authorization") String token, String category,
                      @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                      @DefaultValue("application/json") @HeaderParam("Accept") String accept)  {
        if (!isAdministrator(token)) {
            return serialise(getInvalidCredentialsBean(), accept);
        }

        Object inCategory = deserialise(category, Category.class, contentType);
        if (inCategory instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inCategory), accept);
        Category act = (new CategoryDelegator(token)).createCategory((Category) inCategory);
        return serialise(act, accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/category/{categoryId}")
    public String getGames(@PathParam("categoryId") Long categoryId, @QueryParam("resumptionToken") String cursor,
                           @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        return serialise(new CategoryDelegator(this).getGames(categoryId), accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/category/{gameId}/{categoryId}")
    public String linkGame(@HeaderParam("Authorization") String token, String category,
                           @PathParam("gameId") Long gameId,
                           @PathParam("categoryId") Long categoryId,
                           @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        if (!isAdministrator(token)) {
            return serialise(getInvalidCredentialsBean(), accept);
        }
        return serialise((new CategoryDelegator(this)).linkGameToCategory(gameId, categoryId), accept);
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/topGames/lang/{lang}")
    public String getTopGames(
            @PathParam("lang") String lang,
            @QueryParam("resumptionToken") String cursor,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept){
//        private String startStatsJob(int mapShardCount, int reduceShardCount) {
           return "";
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/countUsers")
    public String startUsersMapReduce(
            @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        String id = MapReduceJob.start(
                MapReduceSpecification.of(
                        "MapReduceTest stats",
                        new DatastoreInput("UserJDO", 2),
                        new CountRunsMapper(),
                        Marshallers.getStringMarshaller(),
                        Marshallers.getLongMarshaller(),
                        new CountReducer(),
                        new CountOutput(2)),
                getSettings());

        return "{ 'jobId':"+id+"}";
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @CacheControlHeader("no-cache")
    @Path("/games/generateOai")
    public String startLomGeneration(
            @DefaultValue("application/json") @HeaderParam("Accept") String accept){
        String id = MapReduceJob.start(
                MapReduceSpecification.of(
                        "Lom Map Reduce",
                        new DatastoreInput("GameJDO", 2),
                        new GamesMapper(),
                        Marshallers.getStringMarshaller(),
                        Marshallers.getStringMarshaller(),
                        new LomReducer(),
                        new LomOutput(2)),
                getSettings());

        return "{ 'jobId':"+id+"}";
    }


    private MapReduceSettings getSettings() {
        return new MapReduceSettings().setWorkerQueueName("default").setBucketName("ar-learn-mapreduce").setModule("default");
    }
}
