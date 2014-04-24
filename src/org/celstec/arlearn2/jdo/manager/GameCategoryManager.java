package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.store.GameCategory;
import org.celstec.arlearn2.beans.store.GameCategoryList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameCategoryJDO;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
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
public class GameCategoryManager {

    public static GameCategory linkGameCategory(Long gameId, Long categoryId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        GameCategoryJDO gameCategoryJDO = new GameCategoryJDO();
        gameCategoryJDO.setCategoryId(categoryId);
        gameCategoryJDO.setGameId(gameId);
        gameCategoryJDO.setDeleted(false);
        try {
            pm.makePersistent(gameCategoryJDO);
            return toBean(gameCategoryJDO);
        } finally {
            pm.close();
        }
    }
    public static GameCategoryList getGames(Long categoryId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            javax.jdo.Query query = pm.newQuery(GameCategoryJDO.class);
            query.setFilter("categoryId == "+categoryId);
            List<GameCategoryJDO> list = (List<GameCategoryJDO>) query.execute();
            GameCategoryList resultList = new GameCategoryList();
            for (GameCategoryJDO categoryJDO : list) {
                resultList.addGameCategory(toBean(categoryJDO));
            }
            return resultList;
        } finally {
            pm.close();
        }
    }

    public static GameCategory toBean(GameCategoryJDO jdo){
        if (jdo == null)
            return null;
        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(jdo.getId().getId());
        gameCategory.setCategoryId(jdo.getCategoryId());
        gameCategory.setGameId(jdo.getGameId());
        gameCategory.setDeleted(jdo.getDeleted());
        return gameCategory;

    }


}
