<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title>Login successful</title>
    <link href="/static/main.css" media="all" rel="stylesheet" type="text/css" />
</head>

<body>

<c:choose>
<c:when test="${isLibrarian == true}">
    <h2>Hi ${username} [Librarian]! Welcome to the web library terminal!</h2>

    <h3>Loaned books</h3>
    <table border="2">
        <tr>
            <th>Book ID</th>
            <th>User ID</th>
            <th>Username</th>
            <th>Date of Loan</th>
            <th>Book Title</th>
            <th>Book Author</th>
            <th>Book Publish Date</th>
            <th>Stock</th>
            <th>Return</th>
        </tr>

        <c:forEach items="${loanedBooks}" var="book">
            <tr>
                <td><c:out value="${book.bookID}"/></td>
                <td><c:out value="${book.userID}"/></td>
                <td><c:out value="${book.username}"/></td>
                <td><c:out value="${book.dateLoan}"/></td>
                <td><c:out value="${book.title}"/></td>
                <td><c:out value="${book.author}"/></td>
                <td><c:out value="${book.datePublish}"/></td>
                <td><c:out value="${book.stock}"/></td>
                <td>
                    <form action="/login/?q=return" method="post">
                        <input type="submit" value="Return">
                        <input type="hidden" name="query" value="return">
                        <input type="hidden" name="userId" value="${book.userID}">
                        <input type="hidden" name="bookId" value="${book.bookID}">
                    </form>
                </td>
            </tr>
        </c:forEach>

    </table>
    <br/>
</c:when>
<c:otherwise>

<h2>Hi ${username} [User]! Welcome to the web library terminal!</h2>
<h3>Available books</h3>

<form action="/login/?q=search" method="post">
    <label>Search by title: <input type="text" name="title"></label><br/>
    <input type="submit" value="Search">
</form>

<form action="/login/?q=reset" method="post">
    <input type="submit" value="Reset">
</form>

<table border="2">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Author</th>
        <th>Availability</th>
        <th>Loan</th>
    </tr>

    <c:forEach items="${availableBooks}" var="book">
        <tr>
            <td><c:out value="${book.id}"/></td>
            <td><c:out value="${book.title}"/></td>
            <td><c:out value="${book.author}"/></td>
            <td><c:out value="${book.stockAvailable}"/></td>
            <td>
                <form action="/login/?q=borrow" method="post">
                    <input type="submit" value="Borrow">
                    <input type="hidden" name="query" value="borrow">
                    <input type="hidden" name="bookId" value="${book.id}">
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

<h3>Borrowed books</h3>
<table border="2">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Author</th>
        <th>Availability</th>
        <%--<th>Loan</th>--%>
    </tr>

    <c:forEach items="${borrowedBooks}" var="book">
    <tr>
        <td><c:out value="${book.id}"/></td>
        <td><c:out value="${book.title}"/></td>
        <td><c:out value="${book.author}"/></td>
        <td><c:out value="${book.stockAvailable}"/></td>
        <%--<td>--%>
            <%--<form action="/login/?q=return" method="post">--%>
                <%--<input type="submit" value="Return">--%>
                <%--<input type="hidden" name="query" value="return">--%>
                <%--<input type="hidden" name="bookId" value="${book.id}">--%>
            <%--</form>--%>
        <%--</td>--%>
    </tr>
    </c:forEach>
</table>
    <br/>
</c:otherwise>
</c:choose>

    <a href="/login/?q=logout">Logout</a>

</body>

</html>
