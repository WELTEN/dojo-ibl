package org.celstec.arlearn2.upload.scripts;

import com.google.gwt.media.client.Audio;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.InputStreamBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.GameFile;
import org.celstec.arlearn2.beans.game.GameFileList;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.FileReference;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.codehaus.jettison.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class MigrateFilesToCloud {

    public static void main7(String[] args) throws Exception {
        downloadFile("http://drive.google.com/uc?confirm=no_antivirus&id=0B-jfuySH5RAtcmNmMUpTVGM2NFE", "/tmp/test_oba_video");

    }

    public static void main(String[] args) throws Exception {
        String authToken = args[0];
        Long gameId = Long.parseLong(args[1]);
        String baseDir = args[2];
        File folderDir = new File(baseDir, ""+gameId);
        folderDir.mkdir();
        String game = downloadUrl("download/game?gameId=" + gameId + "&auth=" + authToken + "&type=game", baseDir+"/game."+gameId+".json");
        JsonBeanDeserializer jsonBeanDeserializer = new JsonBeanDeserializer(game);
        GamePackage gamePackage = (GamePackage) jsonBeanDeserializer.deserialize(GamePackage.class);
        System.out.println(gamePackage);
        GameFileList gameFileList = new GameFileList();
        long index = 1l;
        for (GeneralItem item : gamePackage.getGeneralItems()) {
            boolean itemUpdated = false;
            if (item.getFileReferences() != null) {

                for (FileReference fileReference:item.getFileReferences()) {
                    if (!isCloudBase(fileReference.getFileReference())) {
                        System.out.println("ref "+fileReference.getFileReference()+ " " + fileReference.getKey());
                        String targetFile = folderDir.getAbsolutePath()+"/"+fileReference.getKey();
                        downloadFile(fileReference.getFileReference(), targetFile);
                        String uploadUrl = getUploadUrl(gameId, item.getId(), fileReference.getKey(), authToken);
                        uploadFile(uploadUrl, new File(targetFile));
                        String newRefFeed = "http://streetlearn.appspot.com/game/"+gameId+"/generalItems/"+item.getId()+"/"+fileReference.getKey();
                        fileReference.setFileReference(newRefFeed);
                        itemUpdated= true;
                    }

//                    downloadFile(fileReference.getFileReference(), rawDir+"/"+fileReference.getKey());
//                    GameFile gameFile = new GameFile();
//                    gameFile.setPath("test");
//                    gameFile.setLocalRawRef(fileReference.getKey());
//                    gameFile.setId(index++);
//                    gameFileList.addGameFile(gameFile);

                }

            }
            if (item.getIconUrl()!=null) {
                if (!isCloudBase(item.getIconUrl())) {
                    System.out.println("icon file to migrate "+item.getIconUrl());
                    String targetFile = folderDir.getAbsolutePath()+"/icon_"+item.getId();
                    downloadFile(item.getIconUrl(), targetFile);
                    String uploadUrl = getUploadUrl(gameId, item.getId(), "icon", authToken);
                    uploadFile(uploadUrl, new File(targetFile));
                    String newIconUrl = "http://streetlearn.appspot.com/game/"+gameId+"/generalItems/"+item.getId()+"/icon";
                    item.setIconUrl(newIconUrl);
                    itemUpdated= true;
                }
            }
            if (item instanceof AudioObject) {
                AudioObject audioObject = (AudioObject) item;
                String audioFeed = audioObject.getAudioFeed();
//                audioFeed = audioFeed.replace("dl.dropboxusercontent.com", "localhost:7777");
                if (!isCloudBase(audioFeed)) {
                    System.out.println("audio file to migrate "+audioFeed);
                    String targetFile = folderDir.getAbsolutePath()+"/audio_"+audioObject.getId();
                    downloadFile(audioFeed, targetFile);
//                String uploadUrl = getUploadUrl(gameId, item.getId(), "audio", "ya29.xQCBcPtoYNzpXqI9aCVM1rB5wXh8jwUsOK7-x3F3FB3rlaG5Mm_5W2ZSbIpM1DcgmpKLJ-I5zAbSOQ");
                    String uploadUrl = getUploadUrl(gameId, item.getId(), "audio", authToken);
                    uploadFile(uploadUrl, new File(targetFile));
                    String newAudioFeed = "http://streetlearn.appspot.com/game/"+gameId+"/generalItems/"+item.getId()+"/audio";
                    audioObject.setAudioFeed(newAudioFeed);
                    itemUpdated= true;
                }
            }

            if (item instanceof VideoObject) {
                VideoObject videoObject = (VideoObject) item;
                String videoFeed = videoObject.getVideoFeed();
                if (!isCloudBase(videoFeed)) {
                    System.out.println("video file to migrate "+videoFeed);
                    String targetFile = folderDir.getAbsolutePath()+"/video_"+videoObject.getId();
                    downloadFile(videoFeed, targetFile);
                    String uploadUrl = getUploadUrl(gameId, item.getId(), "video", authToken);
                    uploadFile(uploadUrl, new File(targetFile));
                    String newVideoFeed = "http://streetlearn.appspot.com/game/"+gameId+"/generalItems/"+item.getId()+"/video";
                    videoObject.setVideoFeed(newVideoFeed);
                    itemUpdated= true;

                }

                }
            if (itemUpdated) publishGeneralItem(item, authToken);

        }

        delete(folderDir);

    }

    private static void publishGeneralItem(GeneralItem item, String token) throws Exception {
        String body = item.toString();
        URL url = new URL("http://streetlearn.appspot.com/rest/generalItems");
//        URL url = new URL("http://localhost:7777/rest/generalItems");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(body.getBytes().length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth="+token);
        conn.setDoOutput(true);
        conn.getOutputStream().write(body.getBytes());

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        for (int c; (c = in.read()) >= 0; System.out.print((char)c));
    }

    private static void uploadFile(String uploadUrl, File file) {
        try {
            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(uploadUrl, charset);
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

//            multipart.addFormField("description", "Cool Pictures");
//            multipart.addFormField("keywords", "Java,upload,Spring");

            multipart.addFilePart("fileUpload", file,"application/octet-stream");

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
//            org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
//            HttpContext httpContext = new BasicHttpContext();
//            HttpPost httpPost = new HttpPost(uploadUrl);
//
//            MultipartEntity multiPartContent = new MultipartEntity();
//
////            httpPost.addHeader("Content-Length",Long.toString(multiPartContent.getContentLength()));
//            String mimeType = "application/octet-stream";
//            multiPartContent.addPart("uploaded_file", new InputStreamBody(new FileInputStream(file), mimeType, file.getName()));
//
//            httpPost.setEntity(multiPartContent);
//            HttpResponse response = httpClient.execute(httpPost, httpContext);
//            httpPost.getEntity();

        } catch (Exception e) {
           e.printStackTrace();

        }
    }

    private static String getUploadUrl(Long gameId, Long itemId, String key, String token) {
        try {
            String url = "http://streetlearn.appspot.com/rest/generalItems/pictureUrl/gameId/"+gameId+"/generalItem/"+itemId+"/"+key;
//            String url = "http://localhost:7777/rest/generalItems/pictureUrl/gameId/"+gameId+"/generalItem/"+itemId+"/"+key;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");


            //add request header
            con.setRequestProperty("Authorization", "GoogleLogin auth="+token);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);


            }

            in.close();

            //print result
//            System.out.println(response.toString());
            JSONObject object = new JSONObject(response.toString());
            return object.getString("uploadUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isCloudBase(String url) {
        if (url.startsWith("http://streetlearn.appspot.com/")) return true;
        return false;
    }


    public static String downloadUrl(String path, String filePath) {
        try {
            String url = "http://streetlearn.appspot.com/"+path;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            File f = new File(filePath);
            FileWriter fr = new FileWriter(f);
            BufferedWriter br  = new BufferedWriter(fr);

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);


            }

            in.close();
            JSONObject object = new JSONObject(response.toString());
            String returnString = object.toString(5);

            br.write(returnString);
            br.close();
            return returnString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadFile(String url, String fileName) {
        url = url.replace("http://dl.dropboxusercontent.com","https://dl.dropboxusercontent.com");
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);
            int responseCode = con.getResponseCode();
            if (responseCode == 302) {
                String newUrl = con.getHeaderField("Location");
                System.out.println(newUrl);
                downloadFile(newUrl, fileName);
            } else {
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);


                InputStream is = con.getInputStream();
                StringBuffer response = new StringBuffer();
                File f = new File(fileName);
                FileOutputStream fo = new FileOutputStream(f);

//            while (is.available()>0) {
//
//                fo.write(is.read());
//
//            }
                byte[] b = new byte[2048];

                int length;
                while ((length = is.read(b)) != -1) {
                    fo.write(b, 0, length);
                }

                is.close();
                fo.close();

        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

}
