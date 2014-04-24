package org.celstec.arlearn2.jdo.manager;

import java.util.UUID;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ApplicationAccessKeyJDO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ApplicationKeyManager {

	
	public static void addKey(String applicationName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ApplicationAccessKeyJDO conf = new ApplicationAccessKeyJDO();
		conf.setKey(UUID.randomUUID().toString());
		conf.setApplicationName(applicationName);
		try {
			pm.makePersistent(conf);
		} finally {
			pm.close();
		}
	}
	
	public static boolean getConfigurationObject(String token) {
		Key key = KeyFactory.createKey(ApplicationAccessKeyJDO.class.getSimpleName(), token);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.getObjectById(ApplicationAccessKeyJDO.class, key);
			return true;
		} catch (Exception e){
            return false;
		}finally {
			pm.close();
		} 
	}
}
