package org.celstec.arlearn2.upload;

import com.google.appengine.api.blobstore.*;
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

/**
 * Created by str on 27/05/14.
 */
public class UploadGameContentServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

//    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        String path = req.getPathInfo();
//        Long gameId = Long.parseLong(req.getParameter("gameId"));
//        String newPath = "/uploadGameContent" + path+"?gameId="+gameId;
//        String uploadUrl = blobstoreService.createUploadUrl(newPath);
//        String page = "<body>";
//        page += "Example invocation: uploadGameContent/filePath?gameId=&lt;gameId&gt; + <br>";
//        page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
//        page += "<input type=\"file\" name=\"myFile\">";
//        page += "<input type=\"submit\" value=\"Submit\">";
//        page += "</form></body>";
//        res.getWriter().write(page);
//
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "max-age=2592000");

        String path = req.getPathInfo();
//        long gameIdString = Long.parseLong(path.split("/", -1)[2]);
        Long gameIdString = Long.parseLong(req.getParameter("gameId"));
//        String fileName = path.split("/", -1)[3];
        BlobKey bk = FilePathManager.getBlobKey(null, null, gameIdString, path);
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

    public static void deleteIfFileExists(BlobstoreService blobstoreService, Long gameId,String path) {
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
