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
package org.celstec.arlearn2.upload;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UploadUserContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String account = req.getParameter("account");
		String path = req.getPathInfo();
		System.out.println(path);
		System.out.println(account);
		if (account != null) {
			String uploadUrl = blobstoreService.createUploadUrl("/uploadUserContent" + path + "?account=" + account);
			String page = "<body>";
			page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
			page += "<input type=\"file\" name=\"myFile\">";
			page += "<input type=\"submit\" value=\"Submit\">";
			page += "</form></body>";
			res.getWriter().write(page);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("in upload");
		System.out.println(req.getParameter("account"));
		System.out.println(req.getPathInfo());
		java.util.Map<java.lang.String,java.util.List<BlobKey>> blobs = blobstoreService.getUploads(req);
		for (String key: blobs.keySet()) {
			deleteIfFileExists(req.getParameter("account"), req.getPathInfo());
			FilePathManager.addFile(null, req.getParameter("account"), req.getPathInfo(), blobs.get(key).get(0));
		}
	}

    private void deleteIfFileExists(String email, String path){
        BlobKey bk = FilePathManager.getBlobKey(email, null, path);
        if (bk != null) {
            try {
                blobstoreService.delete(bk);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilePathManager.delete(bk);
        }

    }
}
