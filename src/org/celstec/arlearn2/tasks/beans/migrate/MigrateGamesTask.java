package org.celstec.arlearn2.tasks.beans.migrate;


import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.classes.GameAccessJDO;
import org.celstec.arlearn2.jdo.manager.GameAccessManager;
import org.celstec.arlearn2.jdo.manager.GameManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

public class MigrateGamesTask extends GenericBean {

	private String accountFrom;
	private String accountTo;
	private Integer accountType;
	
	
	public MigrateGamesTask() {
		
	}
	
	
	
	public String getAccountFrom() {
		return accountFrom;
	}



	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}



	public String getAccountTo() {
		return accountTo;
	}



	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}



	public Integer getAccountType() {
		return accountType;
	}



	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}



	public MigrateGamesTask(String accountFrom, Integer accountType, String accountTo, String token) {
		super(token);
		this.accountFrom = 	User.normalizeEmail(accountFrom);
		this.accountTo = accountTo;
		this.accountType = accountType;
	}
	
	@Override
	public void run() {
		for (Game g: GameManager.getGames(null, null, accountFrom, null, null)) {
			GameAccessManager.addGameAccess(accountTo, accountType, g.getGameId(), GameAccessJDO.OWNER);	
			g.setOwner("");
			GameManager.addGame(g, "");
		}
	}

}
