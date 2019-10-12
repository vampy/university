<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>

</head>
<body>

<%--<c:forEach var="i" begin="1" end="5">--%>
<%--<p>NAME ${i}<p>--%>
<%--</c:forEach>--%>

<%--<p>--%>
<h2>Station route</h2>

<c:set var="hasFinal" scope="request" value="${false}"/>
<c:if test="${not empty route}">
    <c:set var="hasFinal" scope="request" value="${route.hasFinalStation()}"/>
</c:if>

<p>
<c:if test="${not empty route}">
    <ol>
        <c:forEach items="${route.getStations()}" var="station">
            <li>${station.name}</li>
        </c:forEach>
    </ol>
    <%--<c:set var="hasFinal" scope="request" value="${route.hasFinalStation()}"/>--%>
    <c:if test="${not route.hasFinalStation()}">
        <form method="post">
            <input type="hidden" name="action" value="mark-final">
            <input type="submit" value="Mark as final station">
        </form>
    </c:if>
    <form method="post">
        <input type="hidden" name="action" value="back">
        <input type="submit" value="Go back">
    </form>
</c:if>
</p>


<p>
<c:if test="${not hasFinal}">
<h2>Select Stations</h2>
<form method="post">
    <label>Station
        <select name="station-id">
            <c:forEach items="${stations}" var="station">
                <option value="${station.id}">${station.name}</option>
            </c:forEach>
        </select>
    </label>
    <input type="hidden" name="action" value="select">
    <input type="submit" value="Select">
</form>
</c:if>
</p>

</body>
</html>
