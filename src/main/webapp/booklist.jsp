<%@ page import="com.github.magdanadratowska.model.UserBook" %>
<%@ page import="java.util.List" %>
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
    <style>
        .margin-bottom-0 {
            margin-bottom: 0px;
        }
        .margin-search {
            margin-top: 0.4rem;
            margin-bottom: 0.3rem;
        }
    </style>

    <title>List of books</title>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body>

<div class="row">
    <jsp:include page="navigator.jsp"></jsp:include>
</div>
<%
    String getQuery = (String) request.getAttribute("query");
%>

<div class="section no-pad-bot">
    <div class="container">
        <div class="row valign-wrapper">
            <div class="col m3">
                <a class="btn waves-effect waves-light red lighten-2 white-text z-depth-2" href="/books-add">Add book
                    <i class="material-icons right">add</i>
                </a>
            </div>
            <div class="col m9 z-depth-1">
<%--                <nav>--%>
                    <div class="nav-wrapper">
                        <form action="/books-search">
                            <div class="input-field margin-search">
                                <input value="${requestScope.query}" id="search" type="search" name="query" required>
                                <label class="label-icon" for="search"><i class="material-icons">search</i></label>
                                <i class="material-icons">close</i>
                            </div>
                        </form>
                    </div>
<%--                </nav>--%>
            </div>
        </div>
        <div class="row margin-bottom-0">
            <div class="col m12">
                <h4 class="red-text lighten-2">List of all books</h4>
            </div>
        </div>
        <div class="row">
            <table class="highlight">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Author name</th>
                    <th>Author surname</th>
                    <th>Details</th>
                    <th>My list</th>
                </tr>
                </thead>
                <tbody>

                <jsp:useBean id="userBookList" scope="request" type="java.util.List"/>

                <c:forEach var="userBook" items="${userBookList}">
                    <tr>
                        <td>${userBook.book.title}</td>
                        <td>${userBook.book.authorName}</td>
                        <td>${userBook.book.authorSurname}</td>

                        <td>
                            <c:choose>
                                <c:when test="${userBook.isOwned}">
                                    <form action="/account/books-edit" method="get">
                                        <input type="hidden" id="id" name="id" value="${userBook.book.id}">
                                        <button class="btn waves-effect waves-light white red-text lighten-2"
                                                type="submit">
                                            <i class="material-icons">description</i>
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="/books-edit" method="get">
                                        <input type="hidden" id="id_e" name="id" value="${userBook.book.id}">
                                        <button class="btn waves-effect waves-light white red-text lighten-2"
                                                type="submit">
                                            <i class="material-icons">edit</i>
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${userBook.isOwned}">

                                    <c:choose>
                                        <c:when test="${userBook.isActive}">
                                            <form action="/account/books-delete?id=${userBook.book.id}" method="post">
                                                <button class="btn waves-effect waves-light white red-text lighten-2"
                                                        type="submit"
                                                        name="action">
                                                    <i class="material-icons">star</i>
                                                </button>
                                            </form>

                                        </c:when>
                                        <c:otherwise>
                                            <form action="/account/books-add?id=${userBook.book.id}&isOwned=true"
                                                  method="post">
                                                <button class="btn waves-effect waves-light white red-text lighten-2"
                                                        type="submit"
                                                        name="action">
                                                    <i class="material-icons">delete_forever</i>
                                                </button>
                                            </form>

                                        </c:otherwise>
                                    </c:choose>

                                </c:when>
                                <c:otherwise>
                                    <form action="/account/books-add?id=${userBook.book.id}" method="post">
                                        <button class="btn waves-effect waves-light white red-text lighten-2"
                                                type="submit"
                                                name="action">
                                            <i class="material-icons">star_border</i>
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="row">
            <ul class="pagination center">


                <%--For displaying Previous link except for the 1st page --%>
                <c:choose>
                    <c:when test="${currentPage != 1}">
                        <c:set var="queryPreviousTrue" scope="application" value="/books?page=${requestScope.currentPage-1}"/>
                        <c:set var="queryPreviousFalse" scope="application" value="/books-search?page=${requestScope.currentPage-1}&query=${requestScope.query}"/>
                        <li class="waves-effect"><a href="${requestScope.query==null ? queryPreviousTrue : queryPreviousFalse}"><i class="material-icons">chevron_left</i></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="disabled"><a href=""><i class="material-icons">chevron_left</i></a>
                        </li>
                    </c:otherwise>
                </c:choose>
                <%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <c:set var="queryForEachTrue" scope="application" value="/books?page=${i}"/>
                    <c:set var="queryForEachFalse" scope="application" value="/books-search?page=${i}&query=${requestScope.query}"/>
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <%--                            <td>${i}</td>--%>
                            <li class="active"><a href="${requestScope.query==null ? queryForEachTrue : queryForEachFalse}">${i}</a></li>
                        </c:when>
                        <c:otherwise>
                            <%--                            <td><a href="/books?page=${i}">${i}</a></td>--%>
                            <li class="waves-effect"><a href="${requestScope.query==null ? queryForEachTrue : queryForEachFalse}">${i}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <%--For displaying Next link --%>
                <c:choose>
                    <c:when test="${currentPage lt noOfPages}">
                        <c:set var="queryNextTrue" scope="application" value="/books?page=${requestScope.currentPage+1}"/>
                        <c:set var="queryNextFalse" scope="application" value="/books-search?page=${requestScope.currentPage+1}&query=${requestScope.query}"/>
                        <li class="waves-effect"><a href="${requestScope.query==null ? queryNextTrue : queryNextFalse}"><i class="material-icons">chevron_right</i></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="disabled"><a href=""><i class="material-icons">chevron_right</i></a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>

    </div>
</div>


<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>