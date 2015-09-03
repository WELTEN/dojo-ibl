package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameClass;
import org.celstec.arlearn2.jdo.classes.GeneralItemJDO;
import org.celstec.arlearn2.jdo.classes.TopGames;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
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

public class TopGamesManager {

    public static void addGame(long gameId, long amountOfUsers, Game game) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        TopGames topGames = new TopGames();
        topGames.setGameId(gameId);
        topGames.setAmountOfUsers(amountOfUsers);
        topGames.setSharing(game.getSharing());
        topGames.setDeleted(game.getDeleted());
        topGames.setLanguage(game.getLanguage());
        topGames.setTitle(game.getTitle());
        try {
            pm.makePersistent(topGames);
        } finally {
            pm.close();
        }
    }

    public static GamesList getTopGames(String lang) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            javax.jdo.Query query = pm.newQuery(TopGames.class);
            query.setFilter("language == languageParam && deleted == false && sharing == 3");

            query.declareParameters("String languageParam");

            query.setOrdering("amountOfUsers desc");
            List<TopGames> list = (List<TopGames>) query.execute(lang);
            GamesList resultList = new GamesList();
            for (TopGames topGames : list) {
                Game game = new Game();
                game.setGameId(topGames.getGameId());
                game.setLanguage(topGames.getLanguage());
                game.setTitle(topGames.getTitle());
                resultList.addGame(game);
            }
            return resultList;
        } finally {
            pm.close();
        }
    }
}
