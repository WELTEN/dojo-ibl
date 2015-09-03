<%@ page import="org.celstec.arlearn2.beans.generalItem.GeneralItem" %>
<%@ page import="org.celstec.arlearn2.delegators.GeneralItemDelegator" %>
<%@ page import="org.celstec.arlearn2.delegators.GameDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameFile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="org.celstec.arlearn2.jdo.manager.FilePathManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameFileList" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html><head><title>ARLearn - Join the game!</title>

</head><body>


<%
    Long fileId = Long.parseLong(request.getParameter("fileId"));
    boolean delete = Boolean.parseBoolean(request.getParameter("delete"));

    BlobKey bk = FilePathManager.getBlobKey(fileId);
    if (delete) {
        System.out.println("test");
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        blobstoreService.delete(bk);
        FilePathManager.delete(bk);
    }
    GeneralItemDelegator itemDelegator = new GeneralItemDelegator("no token");
    GameFileList gameFileList =FilePathManager.getFilePathJDOs(bk);
    System.out.println("delete "+delete);


%>
<table>
    <tr>

        <td>blobkey</td>
        <td><%=bk.getKeyString()%></td>
        <td>generalItemId</td>
        <td>name</td>
        <td>deleted</td>
    </tr>
    <%
        for (GameFile gf : gameFileList.getGameFiles()) {
            String id = gf.getPath();
            long itemId = 0l;
            if (id.startsWith("/generalItems/")) {
                id = id.substring(14);
                id = id.substring(0, id.indexOf("/"));
                try {
                    itemId = Long.parseLong(id);
                } catch (java.lang.NumberFormatException e) {
                }
            }
            String name = "";
            boolean deleted = false;
            if (itemId != 0l) {

                GeneralItem item = itemDelegator.getGeneralItem(itemId);

                if (item != null) {
                    name = item.getName();
                }

                if (item != null) {
                    deleted = item.getDeleted();
                }
            }

        %>
    <tr>

        <td>game file path</td>
        <td><%=gf.getPath()%></td>
        <td><%=itemId%></td>
        <td><%=name%></td>
        <td><%=deleted%></td>
    </tr>


    <%
        }
    %>

</table>
</body></html>