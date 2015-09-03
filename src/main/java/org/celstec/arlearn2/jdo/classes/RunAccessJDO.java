package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class RunAccessJDO {

	public final static int OWNER = 1;
	public final static int CAN_EDIT = 2;
	public final static int CAN_VIEW = 3;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key uniqueId;
	
	@Persistent
	private String localId;
	
	@Persistent
	private Integer accountType;
	
	@Persistent
	private Long runId;
	
	@Persistent
	private Integer accessRights;
	
	@Persistent
	private Long lastModificationDateRun;
	
	public String getUniqueId() {
		return uniqueId.getName();
	}
	
	public void setUniqueId() {
		this.uniqueId = KeyFactory.createKey(RunAccessJDO.class.getSimpleName(), getAccountType()+":"+getLocalId()+":"+getRunId());
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Integer getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(Integer accessRights) {
		this.accessRights = accessRights;
	}

	public Long getLastModificationDateRun() {
		return lastModificationDateRun;
	}

	public void setLastModificationDateRun(Long lastModificationDateRun) {
		this.lastModificationDateRun = lastModificationDateRun;
	}
}
