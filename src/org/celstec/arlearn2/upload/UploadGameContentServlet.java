package org.celstec.arlearn2.upload;

import com.google.appengine.api.blobstore.*;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by str on 27/05/14.
 */
public class UploadGameContentServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();

        String uploadUrl = blobstoreService.createUploadUrl("/uploadGameContent" + path);
        String page = "<body>";
        page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
        page += "<input type=\"file\" name=\"myFile\">";
        page += "<input type=\"submit\" value=\"Submit\">";
        page += "</form></body>";
        res.getWriter().write(page);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Long gameId = Long.parseLong(req.getParameter("gameId"));
        java.util.Map<java.lang.String, java.util.List<BlobKey>> blobs = blobstoreService.getUploads(req);
        for (String key : blobs.keySet()) {
            deleteIfFileExists(gameId, req.getPathInfo());
            FilePathManager.addFile(null, gameId, null, req.getPathInfo(), blobs.get(key).get(0));
        }
    }

    private void deleteIfFileExists(Long gameId,String path) {
        BlobKey bk = FilePathManager.getBlobKey(null, null, gameId, path);
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
