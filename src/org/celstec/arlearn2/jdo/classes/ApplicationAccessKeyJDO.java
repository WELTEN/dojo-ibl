package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ApplicationAccessKeyJDO {

	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key applicationKey;
	
	@Persistent
	private String key;
	
	@Persistent
	private String applicationName;

	public Key getApplicationKey() {
		return applicationKey;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		this.applicationKey = KeyFactory.createKey(ApplicationAccessKeyJDO.class.getSimpleName(), key);

	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	
	
}
