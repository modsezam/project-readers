<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page isELIgnored="false" %>
<html>
<head>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <!--Let browser know website is optimized for mobile-->
    <meta password="viewport" content="width=device-width, initial-scale=1.0"/>


    <title>Password change</title>
</head>
<body>
<div class="container">

    <div class="row">
        <%@include file="navigator.jsp" %>
    </div>


    <div class="row">
        <div class="col s12 m12">
            <div class="row">
                <h4>Password change for user: ${requestScope.user.getUsername()}, (${requestScope.user.getEmail()} </h4>
                <form method="post" action="/passwordChange">
                    <div>
                        <input type="hidden" name="userId" value="${requestScope.user.getId()}">
                        <input placeholder="New password" id="newPassword" type="password" class="validate" name="newPassword">
                        <label for="newPassword">Type new password</label>
                        <input placeholder="Repeat new password" id="newPasswordRepeat" type="password" class="validate"
                               name="newPasswordRepeat">
                        <label for="newPasswordRepeat">Repeat new password</label>
                    </div>
                    <div>
                        <input type="submit" class="waves-effect waves-light btn red lighten-1" value="Submit">
                    </div>
                </form>
            </div>

            <c:if test="${sessionScope.changePasswordError != null}">
                <div class="row">
                    <div class="col s12">
                        <div class="card-panel red lighten-1">
                            <c:choose>
                                <c:when test="${sessionScope.changePasswordError.equals('emptyFields')}">
                                    <span class="white-text">Empty fields.</span>
                                </c:when>
                                <c:when test="${sessionScope.changePasswordError.equals('repeatPasswordError')}">
                                    <span class="white-text">Repeat password correctly.</span>
                                </c:when>

                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>


    </div>
</div>


</body>
</html>
