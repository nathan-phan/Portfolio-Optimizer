<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>test</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="/js/4.0/jquery-3.0.0.min.js"></script>
<link href="/css/fonts/material-design-icons/material.css" rel="stylesheet" />
<script src="/js/materialize.min.js"></script>
<link rel="stylesheet" href="/css/materialize.min.css">
</head>
<body>
<c:forEach items="${strings}" var='string'>
<c:out value='${string}'/>
</c:forEach>
</body>

</html>