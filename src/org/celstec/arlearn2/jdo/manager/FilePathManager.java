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
package org.celstec.arlearn2.jdo.manager;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FilePathJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;

import com.google.appengine.api.blobstore.BlobKey;

public class FilePathManager {

	
	private static final String params[] = new String[]{"email", "runId", "fileName"};
	private static final String paramsNames[] = new String[]{ "emailParam", "runIdParam", "fileNameParam"};
	private static final String types[] = new String[]{"String",  "Long", "String"};

	public static void addFile(Long runId, String email, String fileName, BlobKey blobkey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FilePathJDO filepath = new FilePathJDO();
		filepath.setRunId(runId);
		filepath.setEmail(email);
		filepath.setFileName(fileName);
		filepath.setBlobKey(blobkey);
		try {
			pm.makePersistent(filepath);
		} finally {
			pm.close();
		}
	}
	
	public static BlobKey getBlobKey(String email, Long runId,  String fileName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<FilePathJDO> files = getFilePathJDOs(pm, email, runId, fileName);
			if (!files.isEmpty()) return files.get(0).getBlobKey();
		} finally {
			pm.close();
		}
		return null;
	}
	
	public static List<FilePathJDO> getFilePathJDOs(PersistenceManager pm, String email, Long runId,  String fileName) {
		Query query = pm.newQuery(FilePathJDO.class);
		Object args[] = { email, runId, fileName};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<FilePathJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	public static void delete(BlobKey bk) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(FilePathJDO.class);
			query.setFilter("blobKey == bk");
			query.declareParameters("com.google.appengine.api.blobstore.BlobKey bk");
			List<FilePathJDO> list = (List<FilePathJDO>) query.execute(bk);
			pm.deletePersistentAll(list);
		}finally {
			pm.close();
		}
	}
	
}
