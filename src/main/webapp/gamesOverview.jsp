<%@ page import="org.celstec.arlearn2.delegators.GameAccessDelegator" %>
<%@ page import="org.celstec.arlearn2.delegators.UsersDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.account.Account" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameAccessList" %>
<%@ page import="org.celstec.arlearn2.beans.game.GameAccess" %>
<%@ page import="org.celstec.arlearn2.delegators.GameDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.game.Game" %>
<%--
  Created by IntelliJ IDEA.
  User: str
  Date: 28/08/15
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Overview Games</title>
</head>
<body>
<table>

    <thead>
    <tr>
        <th>Game</th>
    </tr>
    </thead>
    <tbody>
<%
    Cookie[] cookies = request.getCookies();
    String accessToken = null;
    for (int i = 0; i < cookies.length; i++) {
        if (cookies[i].getName().equals("arlearn.AccessToken")) accessToken = cookies[i].getValue();
    }
    if (accessToken != null) {
        UsersDelegator qu  = new UsersDelegator(accessToken);
        Account account = qu.getCurrentAccount();
        GameAccessDelegator gameAccessDelegator = new GameAccessDelegator(accessToken);
        GameAccessList gal = gameAccessDelegator.getGamesAccess(0l, null);
        GameDelegator gameDelegator = new GameDelegator(accessToken);
        for (int i = 0; i < gal.getGameAccess().size(); i++) {
            GameAccess gameAccess = gal.getGameAccess().get(i);
            System.out.println(gameAccess.getGameId());
            Game game = gameDelegator.getGame(gameAccess.getGameId());
            %>
<tr>
    <td><a href="/gameOverview.jsp?gameId=<%=gameAccess.getGameId()%>"><%=game.getTitle()%></a></td>

</tr>
            <%
        }
    }

%>



    </tbody>
</table>

</body>
</html>
