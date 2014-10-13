package org.celstec.arlearn2.mappers.rating;

import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.Reducer;
import com.google.appengine.tools.mapreduce.ReducerInput;

/**
 * Created by str on 17/06/14.
 */
public class RatingReducer extends Reducer<String, Rating, KeyValue<String, Rating>> {

    @Override
    public void reduce(String key, ReducerInput<Rating> values) {
        long total = 0;
        long amount = 0;
        while (values.hasNext()) {
            Rating next = values.next();
            total += next.getTotalRating();
            amount += next.getAmoutOfRatings();
        }
        emit(KeyValue.of(key, new Rating(amount, total)));
    }
}
