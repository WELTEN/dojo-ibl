package org.celstec.arlearn2.download;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import org.celstec.arlearn2.jdo.manager.FilePathManager;
import org.celstec.arlearn2.upload.BlobStoreServlet;
import org.celstec.arlearn2.upload.BlobStoreServletWithExternalUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by str on 27/05/14.
 */
public class GameContentServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BlobStoreServlet.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        System.out.println(path);
        String gameIdString = getFirstPath(path);
        Long gameId = Long.parseLong(gameIdString);
        path = "/"+getReminder(path);
        BlobKey bk = FilePathManager.getBlobKey(null, null,gameId, path);
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
}
