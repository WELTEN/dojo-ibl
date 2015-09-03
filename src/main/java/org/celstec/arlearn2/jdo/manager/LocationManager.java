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
package org.celstec.arlearn2.jdo.manager;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.LocationJDO;

public class LocationManager {

	public static Location addLocation(Long runId, String email, LocationList locList) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<Location> it= locList.getLocations().iterator();
			Location returnLoc= null;
			double mostRecentTimestamp = 0;
			while (it.hasNext()) {
				Location l = (Location) it.next();
				if (mostRecentTimestamp < l.getTimestamp()) {
					returnLoc = l;
				}
				addLocation(pm, runId, email, l.getLat(), l.getLng(), l.getTimestamp());
			}
			return returnLoc;
		} finally {
			pm.close();
		}
	}
	
	public static void addLocation(Long runId, String email, Double lat, Double lng, Long timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			addLocation(pm, runId, email, lat, lng, timeStamp);
		} finally {
			pm.close();
		}
	}
	
	public static Location getLastLocation(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<LocationJDO> list = getLocations(pm, runId, email);
		
		try {
			if (list.isEmpty()) return null;
			return toBean(list.get(0));
		} finally {
			pm.close();
		}
		
	}

	private static void addLocation(PersistenceManager pm, Long runId, String email, Double lat, Double lng, Long timeStamp) {
		LocationJDO location = new LocationJDO();
		location.setRunId(runId);
		location.setEmail(email);
		location.setLat(lat);
		location.setLng(lng);
		location.setTimeStamp(timeStamp);
		pm.makePersistent(location);
	}
	
	public static List<LocationJDO> getLocations(PersistenceManager pm, Long runId, String email) {
		Query query = pm.newQuery(LocationJDO.class);
		Object args [] = new Object[]{runId, email};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		 query.setOrdering("timeStamp desc");
		return ((List<LocationJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
		
	}
	
	private static Location toBean(LocationJDO jdo) {
		if (jdo == null) return null;
		Location pd = new Location();
		pd.setRunId(jdo.getRunId());
		pd.setLat(jdo.getLat());
		pd.setLng(jdo.getLng());
		pd.setTimestamp(jdo.getTimeStamp());
		return pd;
	}
	
	private static final String params[] = new String[]{"runId", "email"};
	private static final String paramsNames[] = new String[]{"runIdParam", "emailParam"};
	private static final String types[] = new String[]{"Long", "String"};

}
