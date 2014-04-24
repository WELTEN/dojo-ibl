package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ConfigurationJDO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key key;
	
	@Persistent
	private String value;

	public String getKey() {
		return key.getName();
	}

	public void setKey(String key) {
		this.key = KeyFactory.createKey(ConfigurationJDO.class.getSimpleName(), key);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
