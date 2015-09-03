package org.celstec.arlearn2.cache;

import com.google.appengine.api.utils.SystemProperty;

public class ConfigurationCache extends GenericCache {
	private static ConfigurationCache instance;

	private static String CACHE_CK_PREFIX = SystemProperty.applicationVersion.get()+"Configuration:CK";
	private ConfigurationCache() {
	}

	public static ConfigurationCache getInstance() {
		if (instance == null)
			instance = new ConfigurationCache();
		return instance;

	}

	public void storeKeyValue(String key, String value) {
		getCache().put(CACHE_CK_PREFIX+key, value);
	}
	
	public String getValue(String key) {
		return (String) getCache().get(CACHE_CK_PREFIX+key);
	}
	
}