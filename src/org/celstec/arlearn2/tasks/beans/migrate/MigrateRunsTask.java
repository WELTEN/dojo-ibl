package org.celstec.arlearn2.tasks.beans.migrate;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.GameAccessJDO;
import org.celstec.arlearn2.jdo.manager.GameAccessManager;
import org.celstec.arlearn2.jdo.manager.GameManager;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

public class MigrateRunsTask extends GenericBean {

	private String accountFrom;
	private String accountTo;
	private Integer accountType;
	
	
	public MigrateRunsTask() {
		
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



	public MigrateRunsTask(String accountFrom, Integer accountType, String accountTo, String token) {
		super(token);
		this.accountFrom = 	User.normalizeEmail(accountFrom);
		this.accountTo = accountTo;
		this.accountType = accountType;
	}
	
	@Override
	public void run() {
		 (new MigrateGeneralItemVisiblityTask(accountFrom, accountType, accountTo, 11l, getToken())).scheduleTask();

		for (Run r: RunManager.getRuns(null, null, accountFrom, null, null)){
			RunAccessManager.addRunAccess(accountTo, accountType, r.getRunId(), GameAccessJDO.OWNER);
			r.setOwner("");
			RunManager.addRun(r);
//			 (new MigrateUserTask(accountFrom, accountType, accountTo, r.getRunId(), getToken())).scheduleTask();
			 

			//update ResponseJDO
		}
	}

}
