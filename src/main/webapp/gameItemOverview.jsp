<%@ page import="org.celstec.arlearn2.delegators.GeneralItemDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.generalItem.GeneralItem" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="org.celstec.arlearn2.upload.UploadGameContentServlet" %>
<%@ page import="org.celstec.arlearn2.jdo.manager.FilePathManager" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--
  Created by IntelliJ IDEA.
  User: str
  Date: 28/08/15
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<%
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  Long gameId = Long.parseLong(request.getParameter("gameId"));

  Long generalItemId = Long.parseLong(request.getParameter("generalItemId"));
  Cookie[] cookies = request.getCookies();
  String accessToken = null;
  for (int i = 0; i < cookies.length; i++) {
    if (cookies[i].getName().equals("arlearn.AccessToken")) accessToken = cookies[i].getValue();
  }
  if (accessToken != null) {
    GeneralItemDelegator generalItemDelegator = new GeneralItemDelegator(accessToken);
    GeneralItem gi = generalItemDelegator.getGeneralItem(generalItemId);
    System.out.println(gi.getName());

    if (request.getParameter("result")== null){
    String newPath = "/gameItemOverview.jsp?gameId="+gameId+"&generalItemId="+generalItemId+"&result=true";

    String uploadUrl = blobstoreService.createUploadUrl(newPath);
    %>

    <form action="<%=uploadUrl%>"
          method="post"
          enctype="multipart/form-data">
      <input type="file" name="myFile">
      <input type="text" name="name">
      <input type="submit" value="Submit">
    </form>

    <%

        } else {
          java.util.Map<java.lang.String, java.util.List<BlobKey>> blobs = blobstoreService.getUploads(request);
          String path = "/generalItems/"+request.getParameter("generalItemId")+"/"+request.getParameter("name");
          for (String key : blobs.keySet()) {
            UploadGameContentServlet.deleteIfFileExists(blobstoreService, gameId, path);
            FilePathManager.addFile(null, gameId, null, path, blobs.get(key).get(0));
          }
          String code = "<img src=\"/game/"+gameId+path+"\" />";
          System.out.println(code);
          request.setAttribute("code", code);
          %>
<br>Gebruik de volgende code: <br><br>
${fn:escapeXml(code)}

          <%

        }

  }
%>



</body>
</html>
