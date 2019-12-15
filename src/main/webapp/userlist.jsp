<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <title>My books</title>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body style="background-color:#ffebee">
<div class="row">
    <jsp:include page="navigator.jsp"></jsp:include>
</div>

<div class="container">
    <h4 class="materialize-red-text">My books</h4>

    <c:if test="${deleted}">
        <div class="row">
            <div class="col">
                <div class="card-panel red lighten-2">
                    <span class="white-text">Book has been removed</span>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${todelete}">
        <div class="row">
            <div class="col">
                <div class="card-panel red lighten-2">
                    <div class="card-content white-text">
                        <span>Do you want to remove book <b>${title}</b> from your list? </span>
                    </div>
                    <div class="card-action">
                        <table class="right-aligned">
                            <tbody>
                            <td><a href="/userlist/list" class="white-text">Cancel</a></td>
                            <th><a href="/userlist/delete?id=<c:out value="${id}"/>"
                                   class="btn-small black">Remove</a></th>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <table class="highlight">
        <thead>
        <tr>
            <th>Author</th>
            <th>Title</th>
            <th>Rate</th>
            <th>Addition date</th>
            <th>Remove</th>
        </tr>
        </thead>

        <tbody>
        <jsp:useBean id="usersBookList" scope="request" type="java.util.List"/>
        <c:forEach var="userBook" items="${requestScope.usersBookList}">
            <tr>
                <td>${userBook.book.authorName} ${userBook.book.authorSurname} </td>
                <td>${userBook.book.title}</td>
                <td>
                    <form action="/account/books-edit?bookId=${userBook.book.id}&action=rate" method="post">
                    <select class="browser-default" name="rate">
                        <option value="" disabled selected>${userBook.rate}</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                        <button class="btn waves-effect waves-light white red-text lighten-2"
                                type="submit"
                                name="action">
                            <i class="material-icons">add</i>
                        </button>
                    </form>
                </td>
                <td>${userBook.additionDate}</td>
                <td>
                    <form action="/account/books-delete?id=${userBook.book.id}" method="post">
                        <button class="btn waves-effect waves-light white red-text lighten-2"
                                type="submit"
                                name="action">
                            <i class="material-icons">clear</i>
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>

</div>
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>
