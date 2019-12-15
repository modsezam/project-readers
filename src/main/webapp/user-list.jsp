<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <!--Let browser know website is optimized for mobile-->
    <meta password="viewport" content="width=device-width, initial-scale=1.0"/>


    <title>Users list</title>
</head>
<body>
<div class="container">
    <div class="row">
        <jsp:include page="navigator.jsp"></jsp:include>
    </div>




    <div class="row valign-wrapper">
        <div class="col m12 z-depth-1">
            <div class="nav-wrapper">
                <form action="/user-search">
                    <div class="input-field margin-search">
                        <input value="${requestScope.query}" id="search" type="search" name="query" required>
                        <label class="label-icon" for="search"><i class="material-icons">search</i></label>
                        <i class="material-icons">close</i>
                    </div>
                </form>
            </div>
        </div>
    </div>


    <table>
        <thead>
        <tr>
            <th>Id</th>
            <th>Username</th>
            <th>Email</th>
            <th>register date</th>
            <th>user type</th>
            <th>delete</th>
            <th>role</th>
            <th>password</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="user" items="${requestScope.users}">
            <tr>
                <td>${user.getId()}</td>
                <td>${user.getUsername()}</td>
                <td>${user.getEmail()}</td>
                <td>${user.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
                <td>${user.getUserType()}</td>
                <td><a href="/user-delete?userId=${user.getId()}"  <c:if test="${sessionScope.userId==user.getId()}">disabled</c:if> class="waves-effect waves-light btn red lighten-1">Delete</a></td>
                <c:if test="${user.getUserType() eq 'USER'}">
                    <td><a href="/setadmin?userId=${user.getId()}" <c:if test="${sessionScope.userId==user.getId()}">disabled</c:if> class="waves-effect waves-light btn red lighten-1">Set admin</a></td>
                </c:if>
                <c:if test="${user.getUserType() eq 'ADMIN'}">
                    <td><a href="/setuser?userId=${user.getId()}" <c:if test="${sessionScope.userId==user.getId()}">disabled</c:if> class="waves-effect waves-light btn red lighten-1">Set user</a></td>
                </c:if>
                <td><a href="/passwordChange?userId=${user.getId()}" class="waves-effect waves-light btn red lighten-1">Change</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <div class="row">
        <ul class="pagination center">

            <%--For displaying Previous link except for the 1st page --%>
            <c:choose>
                <c:when test="${currentPage != 1}">
                    <c:set var="queryPreviousTrue" scope="application" value="/user-list?page=${requestScope.currentPage-1}"/>
                    <%--<c:set var="queryPreviousFalse" scope="application" value="/books-search?page=${requestScope.currentPage-1}&query=${requestScope.query}"/>--%>
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
                <c:set var="queryForEachTrue" scope="application" value="/user-list?page=${i}"/>
                <c:set var="queryForEachFalse" scope="application" value="/user-search?page=${i}&query=${requestScope.query}"/>
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <%--                            <td>${i}</td>--%>
                        <li class="active"><a href="${requestScope.query==null ? queryForEachTrue : queryForEachFalse}">${i}</a></li>
                    </c:when>
                    <c:otherwise>
                                                    <%--<td><a href="/books?page=${i}">${i}</a></td>--%>
                        <li class="waves-effect"><a href="${requestScope.query==null ? queryForEachTrue : queryForEachFalse}">${i}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <%--For displaying Next link --%>
            <c:choose>
                <c:when test="${currentPage lt noOfPages}">
                    <c:set var="queryNextTrue" scope="application" value="/user-list?page=${requestScope.currentPage+1}"/>
                    <c:set var="queryNextFalse" scope="application" value="/user-search?page=${requestScope.currentPage+1}&query=${requestScope.query}"/>
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
</body>
</html>
