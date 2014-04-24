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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescription;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.IOSDevicesRegistryJDO;

import com.google.appengine.api.blobstore.BlobKey;

public class IOSDevicesRegistryManager {

	public static void addDevice(APNDeviceDescription deviceDes) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		IOSDevicesRegistryJDO jdo = new IOSDevicesRegistryJDO();
		jdo.setAccount(deviceDes.getAccount());
		jdo.setDeviceToken(deviceDes.getDeviceToken());
        jdo.setBundleIdentifier(deviceDes.getBundleIdentifier());
        jdo.setDeviceUniqueIdentifier(deviceDes.getDeviceUniqueIdentifier());

		try {
			pm.makePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	public static List<DeviceDescription> getDeviceTokens(String account) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(IOSDevicesRegistryJDO.class);
			query.setFilter("account == accountParam");
			query.declareParameters("String accountParam");
			List<IOSDevicesRegistryJDO> list = (List<IOSDevicesRegistryJDO>) query.execute(account);
			ArrayList<DeviceDescription> result = new ArrayList<DeviceDescription>(list.size());
			int i = 0;
			for (IOSDevicesRegistryJDO jdo : list) {
				result.add(toBean(jdo));
			}
			return result;
		}finally {
			pm.close();
		}
		
	}
	
	private static APNDeviceDescription toBean(IOSDevicesRegistryJDO jdo) {
		if (jdo == null) return null;
		APNDeviceDescription apnReturn = new APNDeviceDescription();
		apnReturn.setAccount(jdo.getAccount());
		apnReturn.setDeviceToken(jdo.getDeviceToken());
		apnReturn.setDeviceUniqueIdentifier(jdo.getDeviceUniqueIdentifier());
        apnReturn.setBundleIdentifier(jdo.getBundleIdentifier());
		return apnReturn;
	}
	
}
