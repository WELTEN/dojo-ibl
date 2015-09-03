package org.celstec.arlearn2.mappers.rating;

/**
 * Created by str on 17/06/14.
 */
public class Rating {
    private long amoutOfRatings;
    private long totalRating;

    public Rating(long amoutOfRatings, long totalRating) {
        this.amoutOfRatings = amoutOfRatings;
        this.totalRating = totalRating;
    }

    public long getAmoutOfRatings() {
        return amoutOfRatings;
    }

    public void setAmoutOfRatings(long amoutOfRatings) {
        this.amoutOfRatings = amoutOfRatings;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }
}
