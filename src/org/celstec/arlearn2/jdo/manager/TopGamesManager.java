package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.Text;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameClass;
import org.celstec.arlearn2.jdo.classes.GeneralItemJDO;
import org.celstec.arlearn2.jdo.classes.TopGames;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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

    public static void addGame(long gameId, long amountOfUsers) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        TopGames gi = new TopGames();
        gi.setGameId(gameId);
        gi.setAmountOfUsers(amountOfUsers);
        try {
            pm.makePersistent(gi);
        } finally {
            pm.close();
        }
    }
}
