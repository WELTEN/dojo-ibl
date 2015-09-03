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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.Version;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VersionJDO;
import org.codehaus.jettison.json.JSONException;

import com.google.appengine.api.datastore.KeyFactory;

public class VersionManager {

	
	public static Version addVersion(Version v) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		VersionJDO version = toJDO(v);
		try {
			return toBean(pm.makePersistent(version));
		} finally{
			pm.close();
		}
	}
	
	public static Version getVersion(Integer versionCode){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(VersionJDO.class);
		query.setFilter("versionCode  == versionCodeParam");
		query.declareParameters("Integer versionCodeParam");
		try {
			List<VersionJDO> results = (List<VersionJDO>) query.execute(versionCode);
			if (results.isEmpty()) return null;
			return toBean(results.get(0));
		
		} finally {
			query.closeAll();
			pm.close();
		}
		
	}

	private static Version toBean(VersionJDO versionJDO) {
		Version v;
		try {
			v = (Version) JsonBeanDeserializer.deserialize(versionJDO.getPayLoad());

		} catch (JSONException e) {
			v = new Version();
			v.setError(e.getMessage());
			e.printStackTrace();
		}
		v.setVersionCode(versionJDO.getVersionCode());
		return v;
	}

	private static VersionJDO toJDO(Version v) {
		VersionJDO jdo = new VersionJDO();
		jdo.setVersionCode(v.getVersionCode());
		jdo.setPayLoad(v.toString());
		return jdo;
	}
}
