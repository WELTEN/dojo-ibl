<%@ page import="org.celstec.arlearn2.delegators.UsersDelegator" %>
<%@ page import="org.celstec.arlearn2.beans.run.UserList" %>
<%@ page import="org.celstec.arlearn2.beans.run.User" %>
<% 
Long runId = Long.parseLong(request.getParameter("runId")); 
UsersDelegator qu = new UsersDelegator("no token");
UserList ul = qu.getUsers(runId, null);
for (User u: ul.getUsers()) {
if (u.getAccountType()==0 && (u.getDeleted() == null || !u.getDeleted())) {
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html><head><title>ARLearn - Join the game!</title>
<style type="text/css">
h1 { page-break-before:always; }
</style>
</head><body>

<h1>ARLearn - Join the game!</h1>
<table>
<tr>
<td style="border:solid black 1px;">
<h2>1. Android device at hand?</h2>
<table><tr>
<td><img width=113 src="images/qr_dialog-apply.png"/></td>
<td><img width=113 src="images/qr_dialog-error.png"/></td>
</tr><tr>
<td><p>Yes: continue</p></td>
<td><p>No: Get one first!</p></td>
</tr></table>
</td>
<td style="border:solid black 1px;">
<h2>2. ARLearn installed?</h2>
<table><tr>
<td><img width=113 src="images/qr_dialog-apply.png"/></td>
<td><img width=113 src="images/qr_forward.png"/></td>
<td><img width=113 src="http://qrfree.kaywa.com/?l=1&s=8&d=http%3A%2F%2Fgoo.gl%2FSooqF" alt="ARLearn download"/></td>
</tr><tr>
<td><p>Yes? continue</p></td>
<td><p>No? Get it here:</p></td>
<td><p><a href="http://goo.gl/SooqF">http://goo.gl/SooqF</a></p></td>
</tr></table>
</td>
</tr>
<tr>
<td colspan=2 style="border:solid black 1px;">
<h2>3. Login - (<%= u.getName()%>, role:  <%= u.getRoles()%>)</h2>
<table><tr>
<td><img width=226 src="images/qr_dialog-apply.png"/></td>
<td><img width=226 src="http://qrfree.kaywa.com/?l=1&s=8&d=<%= u.getLocalId()%>" alt="ARLearn login"/></td>
</tr><tr>
<td colspan=2><p>Start ARLearn, chose QRCode login, scan the tag<p></td>
</tr></table>
</td>
</tr>
<tr>
<td style="border:solid black 1px;">
<h3>Background</h3>
<p>Learn more about ARLearn</p>
<p><img width=63 src="http://qrfree.kaywa.com/?l=1&s=8&d=http%3A%2F%2Fou.nl%2Farlearn" alt="ARLearn homepage"/></p>
<p><a href="http://ou.nl/arlearn">http://ou.nl/arlearn</a></p>
</td>
<td style="border:solid black 1px;"><h3>CELSTEC - creators of ARLearn</h3>
<p>Need help? Ask us at CELSTEC.org</p>
<p><img width=63 src="http://qrfree.kaywa.com/?l=1&s=8&d=http%3A%2F%2Fcelstec.org" alt="CELSTEC - creators of ARLearn"/></p>
<p><a href="http://celstec.org">http://celstec.org</a></p>
</td>
</tr></table>

<%
}
}
%>

</body></html>