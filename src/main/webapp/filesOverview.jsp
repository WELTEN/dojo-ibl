<%@ page import="org.celstec.arlearn2.beans.generalItem.GeneralItem" %>
<%@ page import="org.celstec.arlearn2.delegators.GeneralItemDelegator" %>
<%@ page import="org.celstec.arlearn2.delegators.GameDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameFile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html><head><title>ARLearn - Join the game!</title>
    <style type="text/css">
        h1 { page-break-before:always; }
    </style>
</head><body>
<table>
    <th>
    <td>item id</td>
    <td>Message title</td>
    <td>deleted</td>
    <td>size</td>
    </th>
<%
        Long gameId = Long.parseLong(request.getParameter("gameId"));
    GeneralItemDelegator itemDelegator = new GeneralItemDelegator("no token");
    GameDelegator gameDelegator = new GameDelegator(itemDelegator);
    List<GameFile> gameFiles = gameDelegator.getGameContentDescription(gameId).getGameFiles();
    HashSet<GameFile> doneGameFiles = new HashSet<GameFile>();
    for (GeneralItem generalItem : itemDelegator.getGeneralItems(gameId).getGeneralItems()){

%>
    <tr>
        <td><%=generalItem.getId()%></td>
        <td><%=generalItem.getName()%></td>
        <td><%=generalItem.getDeleted()%></td>
        <td>
            <%
                for (GameFile gameFile: gameFiles) {
                    if (gameFile.getPath().contains(generalItem.getId()+"")) {
                        doneGameFiles.add(gameFile);
                        %>
            <%=gameFile.getPath()%>, size <%=gameFile.getSize()%>, <a href="filesBlobOverview.jsp?fileId=<%=gameFile.getId()%>"> <%=gameFile.getId()%> </a>, <a href="filesBlobOverview.jsp?delete=true&fileId=<%=gameFile.getId()%>"> delete </a><br>
                        <%
                    }
                }
            %>

        </td>
        </tr>
<%
    }


%>
    </table>

<%
    for (GameFile gameFile: gameFiles) {
        if (!doneGameFiles.contains(gameFile)) {
%>
<%=gameFile.getPath()%>, size <%=gameFile.getSize()%> , <a href="filesBlobOverview.jsp?fileId=<%=gameFile.getId()%>"> <%=gameFile.getId()%> </a>, <a href="filesBlobOverview.jsp?delete=true&fileId=<%=gameFile.getId()%>"> delete </a><br>
<%
        }
    }
%>
test
test
test
</body></html>