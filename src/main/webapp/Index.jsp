<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body>
<c:forEach items="${strings}" var='string'>
	<c:out value='${string}'/>
</c:forEach>
</body>
