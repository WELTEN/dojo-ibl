package org.celstec.arlearn2.mappers.rating;

import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.Output;
import com.google.appengine.tools.mapreduce.OutputWriter;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.celstec.arlearn2.jdo.manager.GameAverageRatingManager;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by str on 17/06/14.
 */
public class RatingOutput extends Output<KeyValue<String, Rating>, List<List<KeyValue<String, Rating>>>> {
    public static RatingOutput create(int numShards) {
        return new RatingOutput(numShards);
    }
    public RatingOutput(int shardCount) {
        Preconditions.checkArgument(shardCount >= 0, "Negative shardCount: %s", shardCount);
        this.shardCount = shardCount;
    }

    private static class Writer extends OutputWriter<KeyValue<String, Rating>> {
        private boolean closed = false;
//        private final List<KeyValue<String, Long>> accu = Lists.newArrayList();

        @Override
        public String toString() {
//            return "InMemoryOutput.Writer(" + accu.size() + " items" + (closed ? ", closed" : " so far")
//                    + ")";
            return "nothing";
        }

        @Override
        public void write(KeyValue<String, Rating> value) {
            Preconditions.checkState(!closed, "%s: Already closed", this);
            Rating lomString = value.getValue();
            long gameId =Long.parseLong(value.getKey());
//            LomManager.addGame(gameId, lomString);
            System.out.println("about to write "+gameId + " lom : "+lomString.getAmoutOfRatings() +" "+lomString.getTotalRating());
            double averageRating = ((double) lomString.getTotalRating()) / ((double) lomString.getAmoutOfRatings());
            GameAverageRatingManager.createRating(gameId, averageRating, lomString.getAmoutOfRatings());
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    private final int shardCount;

    @Override
    public List<OutputWriter<KeyValue<String, Rating>>> createWriters() {
        ImmutableList.Builder<OutputWriter<KeyValue<String, Rating>>> out = ImmutableList.builder();
        for (int i = 0; i < shardCount; i++) {
            out.add(new Writer());
        }
        return out.build();
    }

    @Override
    public List<List<KeyValue<String, Rating>>> finish(Collection<? extends OutputWriter<KeyValue<String, Rating>>> writers) throws IOException {

        return null;
    }

    @Override
    public int getNumShards() {
        return shardCount;
    }

}
