package org.celstec.arlearn2.mappers.rating;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Mapper;

/**
 * Created by str on 17/06/14.
 */
public class RatingMapper  extends Mapper<Entity, String, Rating> {

    public void map(Entity entity) {
        Long gameId = (Long) entity.getProperty("gameId");
        long rating = (Long) entity.getProperty("rating");
        emit(""+gameId, new Rating(1, rating));
    }
}
