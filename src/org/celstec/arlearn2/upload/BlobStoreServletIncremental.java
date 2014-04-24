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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

public class BlobStoreServletIncremental extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServlet.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private FileService fileService = FileServiceFactory.getFileService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println(path);
		String runIdString = getFirstPath(path);
		path = getReminder(path);
		String account = getFirstPath(path);
		path = getReminder(path);
		String fileName = path;
		path = getReminder(path);
		System.out.println(runIdString);
		System.out.println(account);
		System.out.println(fileName);
		BlobKey bk = FilePathManager.getBlobKey(account, Long.parseLong(runIdString), fileName);
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

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Long runId = null;
			String account = null;
			String serverPath = null;
			boolean last = false;
			ServletFileUpload upload = new ServletFileUpload();
			res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			System.out.println("before while");
			while (iterator.hasNext()) {
				System.out.println("in while");
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					if ("runId".equals(item.getFieldName())) {
						runId = Long.parseLong(Streams.asString(stream));
						System.out.println("runid is " + runId);
					}

					if ("account".equals(item.getFieldName())) {
						account = Streams.asString(stream);
						System.out.println("account is " + account);
					}
					if ("last".equals(item.getFieldName())) {
						last = Boolean.parseBoolean(Streams.asString(stream));
						System.out.println("last is " + last);
					}
					if ("serverPath".equals(item.getFieldName())) {
						serverPath = Streams.asString(stream);
						System.out.println("serverPath is " + serverPath);
					}
					
					
				} else {					log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());
					AppEngineFile file = storeBlob(item.getContentType(), item.getName(), stream, last, serverPath);
					
					BlobKey blobkey = fileService.getBlobKey(file);
					if (blobkey != null) {
						// File exists
						BlobKey oldkey = FilePathManager.getBlobKey(account, runId, item.getName());
						if (oldkey != null) {
							FilePathManager.delete(oldkey);
							blobstoreService.delete(oldkey);
						}
						FilePathManager.addFile(runId, account, item.getName(), blobkey);
						System.out.println(blobkey.toString());
					} 
					res.getWriter().write(file.getFullPath());
//					else {
//						blobkey.toString();
//					}

				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	private AppEngineFile storeBlob(String contentType, String fileName, InputStream stream, boolean last, String serverPath) throws IOException {
		
		
		AppEngineFile file;
		if (serverPath == null) {
			 file = fileService.createNewBlobFile(contentType, fileName);
		} else {
			file = new AppEngineFile(serverPath);
		}

//		boolean lock = true;
		log.warning("last is"+last+ "file fullpath "+file.getFullPath());
		FileWriteChannel writeChannel = fileService.openWriteChannel(file, last);
		ByteBuffer buf = ByteBuffer.allocateDirect(10);

		byte[] bytes = new byte[1024];
		int count = 0;
		int index = 0;

		// Continue writing bytes until there are no more
		while (count >= 0) {
			if (index == count) {
				count = stream.read(bytes);
				index = 0;
			}
			// Fill ByteBuffer
			while (index < count && buf.hasRemaining()) {
				buf.put(bytes[index++]);
			}

			// Set the limit to the current position and the
			// position to 0
			// making the new bytes visible for write()
			buf.flip();

			// Write the bytes to the channel
			int numWritten = writeChannel.write(buf);

			// Check if all bytes were written
			if (buf.hasRemaining()) {
				buf.compact();
			} else {
				buf.clear();
			}
		}
		writeChannel.close();
		if (last) writeChannel.closeFinally();
		return file;
//		return fileService.getBlobKey(file);
	}

}
