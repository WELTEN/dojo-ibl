package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.Rating;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RatingJDO;

import javax.jdo.PersistenceManager;

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
public class RatingManager {

    public static Rating createRating(long gameId, int providerId, String userId, int rating) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        RatingJDO ratingJDO = new RatingJDO();
        ratingJDO.setGameId(gameId);
        ratingJDO.setUserId(userId);
        ratingJDO.setUserProviderId(providerId);
        ratingJDO.setRating(rating);
        ratingJDO.setId();

        try {
            pm.makePersistent(ratingJDO);
            return toBean(ratingJDO);
        } finally {
            pm.close();
        }
    }

    private static Rating toBean(RatingJDO jdo) {
        if (jdo == null)
            return null;
        Rating bean = new Rating();
        bean.setGameId(jdo.getGameId());
        bean.setUserId(jdo.getUserId());
        bean.setUserProviderId(jdo.getUserProviderId());
        bean.setRating(jdo.getRating());
        return bean;
    }
}
