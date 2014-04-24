package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.notification.DeviceDescription;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GCMDevicesRegistryJDO;

public class GCMDevicesRegistryManager {

	
	public static void addDevice(GCMDeviceDescription deviceDes) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GCMDevicesRegistryJDO jdo = new GCMDevicesRegistryJDO();
		jdo.setAccount(deviceDes.getAccount());
		jdo.setRegistrationId(deviceDes.getRegistrationId());
		jdo.setDeviceId(deviceDes.getDeviceUniqueIdentifier());
		try {
			pm.makePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	public static List<DeviceDescription> getDeviceTokens(String account) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(GCMDevicesRegistryJDO.class);
			query.setFilter("account == accountParam");
			query.declareParameters("String accountParam");
			List<GCMDevicesRegistryJDO> list = (List<GCMDevicesRegistryJDO>) query.execute(account);
			ArrayList<DeviceDescription> result = new ArrayList<DeviceDescription>(list.size());
			for (GCMDevicesRegistryJDO jdo : list) {
				result.add(toBean(jdo));
			}
			return result;
		}finally {
			pm.close();
		}
		
	}
	
	private static GCMDeviceDescription toBean(GCMDevicesRegistryJDO jdo) {
		if (jdo == null) return null;
		GCMDeviceDescription gcmReturn = new GCMDeviceDescription();
		gcmReturn.setAccount(jdo.getAccount());
		gcmReturn.setRegistrationId(jdo.getRegistrationId());
		return gcmReturn;
	}
}
