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

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.celstec.arlearn2.beans.game.GameFile;
import org.celstec.arlearn2.beans.game.GameFileList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FilePathJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;

import com.google.appengine.api.blobstore.BlobKey;

public class FilePathManager {

	
	private static final String params[] = new String[]{"email", "runId", "gameId", "fileName"};
	private static final String paramsNames[] = new String[]{ "emailParam", "runIdParam", "gameIdParam", "fileNameParam"};
	private static final String types[] = new String[]{"String",  "Long", "Long", "String"};
    private static final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

	public static void addFile(Long runId, String email, String fileName, BlobKey blobkey) {
        addFile(runId, null, email, fileName,blobkey);
	}

    public static void addFile(Long runId, Long gameId, String email, String fileName, BlobKey blobkey) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        FilePathJDO filePathJDO = new FilePathJDO();
        filePathJDO.setRunId(runId);
        filePathJDO.setGameId(gameId);
        filePathJDO.setEmail(email);
        filePathJDO.setFileName(fileName);
        filePathJDO.setBlobKey(blobkey);
        try {
            pm.makePersistent(filePathJDO);
        } finally {
            pm.close();
        }
    }

    public static boolean clone(Long oldGameId, Long newGameId, String oldFileName, String newFileName){
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<FilePathJDO> files = getFilePathJDOs(pm, null, null, oldGameId, oldFileName);
            if (files.isEmpty()) return false;
            List<FilePathJDO> newFiles = getFilePathJDOs(pm, null, null, newGameId, newFileName);
            for (FilePathJDO newFile:newFiles) {
                pm.deletePersistent(newFile);
            }
            FilePathJDO oldFilePathJDO = files.get(0);
            FilePathJDO filePathJDO = new FilePathJDO();
            filePathJDO.setGameId(newGameId);
            filePathJDO.setFileName(newFileName);
            filePathJDO.setBlobKey(oldFilePathJDO.getBlobKey());
            pm.makePersistent(filePathJDO);
            return true;
        } finally {
            pm.close();
        }
    }

    public static void deleteFilePath(long gameId, String filePath) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<FilePathJDO> newFiles = getFilePathJDOs(pm, null, null, gameId, filePath);
            for (FilePathJDO newFile:newFiles) {
                pm.deletePersistent(newFile);
            }
        } finally {
            pm.close();
        }
    }

	public static BlobKey getBlobKey(String email, Long runId,  String fileName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<FilePathJDO> files = getFilePathJDOs(pm, email, runId, null, fileName);
			if (!files.isEmpty()) return files.get(0).getBlobKey();
		} finally {
			pm.close();
		}
		return null;
	}

    public static BlobKey getBlobKey(String email, Long runId,  Long gameId, String fileName){
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<FilePathJDO> files = getFilePathJDOs(pm, email, runId, gameId, fileName);
            if (!files.isEmpty()) return files.get(0).getBlobKey();
        } finally {
            pm.close();
        }
        return null;
    }
    public static GameFileList getFilePathJDOs(String email, Long runId, Long gameId, String fileName) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            GameFileList result = new GameFileList();
            for (FilePathJDO filePathJDO : getFilePathJDOs(pm, email, runId, gameId, fileName)) {
                    result.addGameFile(toBean(filePathJDO));
            }
            return result;
        } finally {
            pm.close();
        }
    }
	public static List<FilePathJDO> getFilePathJDOs(PersistenceManager pm, String email, Long runId, Long gameId, String fileName) {
		Query query = pm.newQuery(FilePathJDO.class);
		Object args[] = { email, runId, gameId, fileName};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<FilePathJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}


    private static GameFile toBean(FilePathJDO filePathJDO) {
        GameFile gf = new GameFile();
        gf.setId(filePathJDO.getId());
        gf.setPath(filePathJDO.getFileName());
        gf.setMd5Hash(blobInfoFactory.loadBlobInfo(filePathJDO.getBlobKey()).getMd5Hash());
        gf.setSize(blobInfoFactory.loadBlobInfo(filePathJDO.getBlobKey()).getSize());
        return gf;

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


    public static BlobKey getBlobKey(Long fileId) {
        Key k = KeyFactory.createKey(FilePathJDO.class.getSimpleName(), fileId);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            FilePathJDO pathJDO = pm.getObjectById(FilePathJDO.class, k);
            return pathJDO.getBlobKey();
        }finally {
            pm.close();
        }

    }

    public static GameFileList getFilePathJDOs(BlobKey bk) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(FilePathJDO.class);
            query.setFilter("blobKey == bk");
            query.declareParameters("com.google.appengine.api.blobstore.BlobKey bk");
            GameFileList result = new GameFileList();
            for (FilePathJDO filePathJDO : (List<FilePathJDO>) query.execute(bk)) {
                result.addGameFile(toBean(filePathJDO));
            }
            return result;
        }finally {
            pm.close();
        }
    }
}
