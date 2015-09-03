package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class GameAccessJDO {

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
	private Long gameId;
	
	@Persistent
	private Integer accessRights;
	
	@Persistent
	private Long lastModificationDateGame;
	
	public String getUniqueId() {
		return uniqueId.getName();
	}
	
	public void setUniqueId() {
		this.uniqueId = KeyFactory.createKey(GameAccessJDO.class.getSimpleName(), getAccountType()+":"+getLocalId()+":"+getGameId());
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

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Integer getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(Integer accessRights) {
		this.accessRights = accessRights;
	}

	public Long getLastModificationDateGame() {
		return lastModificationDateGame;
	}

	public void setLastModificationDateGame(Long lastModificationDateGame) {
		this.lastModificationDateGame = lastModificationDateGame;
	}
	
}
