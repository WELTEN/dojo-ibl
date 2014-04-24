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
package org.celstec.arlearn2.download;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.jdo.manager.FilePathManager;
import org.celstec.arlearn2.upload.BlobStoreServlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UserContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServlet.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println(path);
		String account = getFirstPath(path);
		path = "/"+getReminder(path);
		System.out.println(account);
		System.out.println(path);
		BlobKey bk = FilePathManager.getBlobKey(account, null, path);
		if (bk != null) {
			blobstoreService.serve(bk, resp);
		} else {
			resp.setStatus(404);
		}
	}
	
	private String getFirstPath(String path) {
		if (path == null)
			return null;
		if (path.startsWith("/"))
			return getFirstPath(path.substring(1));
		if (path.contains("/"))
			return path.substring(0, path.indexOf("/"));
		return path;
	}
	
	private String getReminder(String path) {
		if (path == null)
			return null;
		if (path.startsWith("/"))
			return getReminder(path.substring(1));
		if (path.contains("/"))
			return path.substring(path.indexOf("/") + 1);
		return null;
	}

}
