package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FeaturedGameJDO;

import javax.jdo.PersistenceManager;
import java.util.List;

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
public class FeaturedGameManager {

    public static Game createFeaturedGame(long gameId, int rank, String lang) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        FeaturedGameJDO featuredGameJDO = new FeaturedGameJDO();
        featuredGameJDO.setGameId(gameId);
        featuredGameJDO.setRank(rank);
        featuredGameJDO.setLang(lang);
        featuredGameJDO.setLastModificationDate(System.currentTimeMillis());
        try {
            pm.makePersistent(featuredGameJDO);
            Game game = new Game();
            game.setGameId(featuredGameJDO.getGameId());
            game.setRank(featuredGameJDO.getRank());
            game.setLanguage(featuredGameJDO.getLang());
            return game;

        } finally {
            pm.close();
        }
    }

    public static GamesList getFeaturedGames(String lang) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            javax.jdo.Query query = pm.newQuery(FeaturedGameJDO.class);

            List<FeaturedGameJDO> list = (List<FeaturedGameJDO>) query.execute();
            GamesList resultList = new GamesList();
            for (FeaturedGameJDO featuredGameJDO : list) {
                Game game = new Game();
                game.setGameId(featuredGameJDO.getGameId());
                game.setRank(featuredGameJDO.getRank());
                game.setLanguage(featuredGameJDO.getLang());
                resultList.addGame(game);
            }
            return resultList;
        } finally {
            pm.close();
        }
    }


}
