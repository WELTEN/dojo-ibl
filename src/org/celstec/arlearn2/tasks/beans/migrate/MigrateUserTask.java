package org.celstec.arlearn2.tasks.beans.migrate;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

public class MigrateUserTask extends GenericBean {

	private String accountFrom;
	private String accountTo;
	private Integer accountType;
	
	public MigrateUserTask() {
		
	}
	
	public MigrateUserTask(String accountFrom, Integer accountType, String accountTo, String token) {
		super(token);
		this.accountFrom = 	User.normalizeEmail(accountFrom);
		this.accountTo = accountTo;
		this.accountType = accountType;
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

	@Override
	public void run() {
		for (User u: UserManager.getUserList(null,accountFrom, null , null)){
			UserManager.updateAccount(accountFrom, accountType+":"+accountTo, u.getRunId());	
			 (new MigrateGeneralItemVisiblityTask(accountFrom, accountType, accountTo, u.getRunId(), getToken())).scheduleTask();
			 (new MigrateResponseTask(accountFrom, accountType, accountTo, u.getRunId(), getToken())).scheduleTask();
		}
		
	}

}

