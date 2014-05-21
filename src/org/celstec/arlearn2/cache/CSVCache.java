package org.celstec.arlearn2.cache;

import com.google.appengine.api.utils.SystemProperty;


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
public class CSVCache extends GenericCache {

    private static String CSV_PREFIX = SystemProperty.applicationVersion.get()+":CSV:";
    private static CSVCache instance;
    private CSVCache() {
    }

    public static CSVCache getInstance() {
        if (instance == null)
            instance = new CSVCache();
        return instance;

    }

    public void putCSV(String csvId, CSVEntry CSV) {
        try {
            getCache().put(CSV_PREFIX + csvId, CSV);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CSVEntry getCSV(String csvId) {
        return (CSVEntry) getCache().get(CSV_PREFIX+csvId);
    }

}
