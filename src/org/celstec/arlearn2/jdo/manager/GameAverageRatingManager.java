package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.game.Rating;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameAverageRating;

import javax.jdo.PersistenceManager;

/**
 * Created by str on 18/06/14.
 */
public class GameAverageRatingManager {

    public static GameAverageRating createRating(long gameId, double rating, long amountOfRatings) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        GameAverageRating averageRating = new GameAverageRating();
        averageRating.setGameId(gameId);
        averageRating.setAverageRating(rating);
        averageRating.setAmount(amountOfRatings);
        try {
            pm.makePersistent(averageRating);
            return averageRating;
        } finally {
            pm.close();
        }
    }

    public static Rating getAverageRatingBean(Long gameId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Key k = KeyFactory.createKey(GameAverageRating.class.getSimpleName(), gameId);
        GameAverageRating rating = pm.getObjectById(GameAverageRating.class, k);

        if (rating == null) return null;
        Rating returnRating = new Rating();
        returnRating.setGameId(gameId);
        returnRating.setRating(rating.getAverageRating().intValue());
        returnRating.setAmount(rating.getAmount());
        return returnRating;
    }
}
