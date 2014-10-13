package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.jdo.manager.FeaturedGameManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

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
public class FeaturedGameDelegator extends GoogleDelegator {
    public FeaturedGameDelegator(String authToken) {
        super(authToken);
    }

    public FeaturedGameDelegator() {
    }

    public FeaturedGameDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public FeaturedGameDelegator(Account account, String token) {
        super(account, token);
    }

    public FeaturedGameDelegator(Service service) {
        super(service);
    }

    public FeaturedGameDelegator(GenericBean bean) {
        super(bean);
    }

    public GamesList getFeaturedGames(String lang) {
        return FeaturedGameManager.getFeaturedGames(lang);
    }

    public Game createFeaturedGame(String lang, Long gameId, Integer rank) {
        return FeaturedGameManager.createFeaturedGame(gameId, rank, lang);
    }
}
