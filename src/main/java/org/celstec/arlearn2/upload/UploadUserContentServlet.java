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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class UploadUserContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//		String account = req.getParameter("account");
//		String path = req.getPathInfo();
//		System.out.println(path);
//		System.out.println(account);
//		if (account != null) {
//			String uploadUrl = blobstoreService.createUploadUrl("/uploadUserContent" + path + "?account=" + account);
//			String page = "<body>";
//			page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
//			page += "<input type=\"file\" name=\"myFile\">";
//			page += "<input type=\"submit\" value=\"Submit\">";
//			page += "</form></body>";
//			res.getWriter().write(page);
//		}
//	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "max-age=2592000");

		String path = req.getPathInfo();
//        long gameIdString = Long.parseLong(path.split("/", -1)[2]);
		String account = req.getParameter("account");
//        String fileName = path.split("/", -1)[3];
		BlobKey bk = FilePathManager.getBlobKey(account, null, null, path);
		if (bk != null) {
			if (req.getParameter("thumbnail") == null) {
				blobstoreService.serve(bk, resp);
			}  else {
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(bk);
				options.imageSize(Integer.parseInt(req.getParameter("thumbnail")));
				boolean crop = false;
				if (req.getParameter("crop")!=null) {
					crop = Boolean.parseBoolean(req.getParameter("crop"));
				}
				options.crop(req.getParameter("crop")!=null);
				String thumbnailUrl =imagesService.getServingUrl(options);

				resp.sendRedirect(thumbnailUrl);
			}

		} else {
			resp.setStatus(404);
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
