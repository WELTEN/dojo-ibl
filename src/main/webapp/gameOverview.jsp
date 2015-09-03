<%@ page import="org.celstec.arlearn2.delegators.GameAccessDelegator" %>
<%@ page import="org.celstec.arlearn2.delegators.UsersDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.account.Account" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameAccessList" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameAccess" %>
<%@ page import="org.celstec.arlearn2.delegators.GameDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.game.Game" %>
<%@ page import="org.celstec.arlearn2.delegators.GeneralItemDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.generalItem.GeneralItemList" %>
<%@ page import="org.celstec.arlearn2.beans.generalItem.GeneralItem" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameFile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%--
  Created by IntelliJ IDEA.
  User: str
  Date: 28/08/15
  Time: 10:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<table>


  <%!
    public String readableFileSize(final long size) {
      if(size <= 0) return "0";
      final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
      int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
      return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
  %>
  <%
    Cookie[] cookies = request.getCookies();
    String accessToken = null;
    for (int i = 0; i < cookies.length; i++) {
      System.out.println(cookies[i].getName() + " "+cookies[i].getValue());
      if (cookies[i].getName().equals("arlearn.AccessToken")) accessToken = cookies[i].getValue();
    }



    if (accessToken != null) {
      Long gameId = Long.parseLong(request.getParameter("gameId"));
      GameDelegator gameDelegator = new GameDelegator(accessToken);
      List<GameFile> gameFiles = gameDelegator.getGameContentDescription(gameId).getGameFiles();
      System.out.println(gameFiles);
      long contentSize = 0l;
      for (int i = 0; i < gameFiles.size(); i++) {
        contentSize += (gameFiles.get(i).getSize());
      }

  %>
  Totaal storage game = <%=readableFileSize(contentSize)%><br>
  <thead>
  <tr>
    <th>Game</th>
  </tr>
  </thead>
  <tbody>
  <%


      UsersDelegator qu  = new UsersDelegator(accessToken);
      Account account = qu.getCurrentAccount();
      if (account != null) {
        GeneralItemDelegator generalItemDelegator = new GeneralItemDelegator(accessToken);
        GeneralItemList generalItemList = generalItemDelegator.getGeneralItems(gameId);
        for (int i = 0; i < generalItemList.getGeneralItems().size(); i++) {
          GeneralItem generalItem = generalItemList.getGeneralItems().get(i);
           contentSize = 0l;
          for (int j = 0; j < gameFiles.size(); j++) {
//            String path = "test"+gameFiles.get(j).getPath().indexOf("test");
            if (gameFiles.get(j).getPath().indexOf(""+generalItem.getId()) != -1 )
                contentSize += (gameFiles.get(j).getSize());
          }


  %>
  <tr>
    <td><a href="/gameItemOverview.jsp?gameId=<%=gameId%>&generalItemId=<%=generalItem.getId()%>"><%=generalItem.getName()%></a></td>
    <td><%=readableFileSize(contentSize)%></td>

  </tr>
  <%
      }
    }
    }

  %>


  </tbody>
</table>

</body>
</html>