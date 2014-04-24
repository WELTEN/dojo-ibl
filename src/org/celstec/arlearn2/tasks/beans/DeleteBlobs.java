/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.tasks.beans;

import java.util.Iterator;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FilePathJDO;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class DeleteBlobs extends GenericBean {
	
	private Long runId;
	private String fullAccount;

	public DeleteBlobs() {
		super();
	}
	
	public DeleteBlobs(String token, Account account,Long runId) {
		super(token, account);
		this.runId = runId;
	}
	
	public DeleteBlobs(String token, Account account, Long runId, String fullAccount) {
		super(token, account);
		this.runId = runId;
		this.fullAccount = fullAccount;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

    public String getFullAccount() {
        return fullAccount;
    }

    public void setFullAccount(String fullAccount) {
        this.fullAccount = fullAccount;
    }

    @Override
	public void run() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		for (Iterator<FilePathJDO> iterator = FilePathManager.getFilePathJDOs(pm, getFullAccount(), getRunId(), null).iterator(); iterator.hasNext();) {               //TODO check if this deletion works and iff fullAccount is not an email but 1:123
			FilePathJDO fpjdo = (FilePathJDO) iterator.next();
			try {
				blobstoreService.delete(fpjdo.getBlobKey());
				pm.deletePersistent(fpjdo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
