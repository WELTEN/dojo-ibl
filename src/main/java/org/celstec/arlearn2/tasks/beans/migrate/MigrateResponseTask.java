package org.celstec.arlearn2.tasks.beans.migrate;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.manager.ResponseManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

public class MigrateResponseTask extends GenericBean {

	private String accountFrom;
	private String accountTo;
	private Integer accountType;
	private Long runId;
	
	
	public MigrateResponseTask() {
		
	}
	
	public MigrateResponseTask(String accountFrom, Integer accountType, String accountTo, Long runId, String token) {
		super(token);
		this.accountFrom = 	User.normalizeEmail(accountFrom);
		this.accountTo = accountTo;
		this.accountType = accountType;
		this.runId = runId;
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

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public void run() {
		ResponseManager.updateAccount(accountFrom, accountType+":"+accountTo, runId);
	}

}

