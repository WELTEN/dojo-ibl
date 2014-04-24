package org.celstec.arlearn2.cache;

import com.google.appengine.api.utils.SystemProperty;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.delegators.ActionRelevancyPredictor;

import java.util.HashSet;

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
public class CategoryCache extends GenericCache {

    private static CategoryCache instance;

    private static String CATEGORY_PREFIX = SystemProperty.applicationVersion.get()+"Category";

    private CategoryCache() {
    }

    public static CategoryCache getInstance() {
        if (instance == null)
            instance = new CategoryCache();
        return instance;

    }

    public void storeCategory(Category bean) {
        getCache().put(CATEGORY_PREFIX+bean.getId(), bean);
    }

    public void storeCategories(CategoryList bean) {
        if (!bean.getCategoryList().isEmpty()){
            getCache().put(CATEGORY_PREFIX+":lang:"+bean.getCategoryList().get(0).getLang(), bean);
        }
    }

    public void storeCategoriesByCatId(CategoryList bean) {
        if (!bean.getCategoryList().isEmpty()){
            getCache().put(CATEGORY_PREFIX+":catId:"+bean.getCategoryList().get(0).getCategoryId(), bean);
        }
    }

    public Category getCategory(Long id) {
        return (Category) getCache().get(CATEGORY_PREFIX+id);
    }

    public CategoryList getCategories(String lang) {
        return (CategoryList) getCache().get(CATEGORY_PREFIX+":lang:"+lang);
    }

    public CategoryList getCategories(Long categoryId) {
        return (CategoryList) getCache().get(CATEGORY_PREFIX+":catId:"+categoryId);
    }
}
