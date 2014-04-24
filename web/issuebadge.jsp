<html>
<head>
<script src="http://beta.openbadges.org/issuer.js"></script>
</head>
<body onLoad="OpenBadges.issue('http://streetlearn.appspot.com/rest/myRuns/badge/<%= request.getParameter("runId") %>/<%= request.getParameter("itemId") %>/<%= request.getParameter("email") %>')">
</body>
</html>