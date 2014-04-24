/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.cache;

import java.util.TreeSet;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

import org.celstec.arlearn2.delegators.LocationDelegator;
import org.celstec.arlearn2.util.FusionCache;

public class LocationCache {

	private static LocationCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(LocationCache.class
			.getName());

	private LocationCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static LocationCache getInstance() {
		if (instance == null)
			instance = new LocationCache();
		return instance;
	}
	
	private static String LOCATIONS_PREFIX = "locationsPrefix";

	public long getLocationTableIdForRun(Long runId) {
		Long responseLong = (Long) cache.get(LOCATIONS_PREFIX+runId);
		if (responseLong == null) responseLong = -1l;
		return responseLong;
	}
	
	public void putLocationTableIdForRun(Long runId, Long tableId) {
		cache.put(LOCATIONS_PREFIX+runId, tableId);
	}

    public void putLocationUser(String account, TreeSet<LocationDelegator.Location> locations) {
        cache.put(LOCATIONS_PREFIX+account, locations);
    }

    public  TreeSet<LocationDelegator.Location> getLocations(String account) {
        TreeSet<LocationDelegator.Location> result = (TreeSet<LocationDelegator.Location>) cache.get(LOCATIONS_PREFIX+account);
        if (result == null) return new TreeSet<LocationDelegator.Location>();
        return result;
    }
}
