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


    <title>Register / Login</title>
</head>
<body>
<div class="container">

    <div class="row">
        <%@include file="navigator.jsp" %>
    </div>


    <div class="row">
        <div class="col s12 m6">
            <div class="row">
                <h4>Login:</h4>
                <form method="post" action="/login">
                    <div>
                        <input placeholder="Your email" id="user-email-login" type="text" class="validate" name="email">
                        <label for="user-email-login">User email</label>
                        <input placeholder="Your password" id="user-password-login" type="password" class="validate"
                               name="password">
                        <label for="user-password-login">User email</label>
                    </div>
                    <div>
                        <input type="submit" class="waves-effect waves-light btn red lighten-1" value="login">
                    </div>
                </form>
            </div>

            <c:if test="${sessionScope.loginError != null}">
                <div class="row">
                    <div class="col s12">
                        <div class="card-panel red lighten-1">
                            <c:choose>
                                <c:when test="${sessionScope.loginError.equals('userNotFound')}">
                                    <span class="white-text">User not found.</span>
                                </c:when>
                                <c:when test="${sessionScope.loginError.equals('wrongPassword')}">
                                    <span class="white-text">Repeat password correctly.</span>
                                </c:when>
                                <c:when test="${sessionScope.loginError.equals('emptyFields')}">
                                    <span class="white-text">Empty fields.</span>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>


        <div class="col s12 m6">
            <div class="row">
                <h4>New user?</h4>
                <form method="post" action="/user-register">
                    <div>
                        <input placeholder="Your name" id="user-name" type="text" class="validate" name="name">
                        <label for="user-name">User name</label>

                        <input placeholder="Your email" id="user-email" type="text" class="validate" name="email">
                        <label for="user-email">User email</label>

                        <input placeholder="Your password" id="user-password" type="password" class="validate"
                               name="password">
                        <label for="user-password">User email</label>

                        <input placeholder="Repeat password" id="user-password-repeat" type="password" class="validate"
                               name="password-repeat">
                        <label for="user-password-repeat">Repeat password</label>
                    </div>
                    <div>
                        <input type="submit" class="waves-effect waves-light btn red lighten-1" value="submit">
                    </div>
                </form>
            </div>
            <c:if test="${sessionScope.registerError != null}">
                <div class="row">
                    <div class="col s12">
                        <div class="card-panel red lighten-1">
                            <c:choose>
                                <c:when test="${sessionScope.registerError.equals('repeatPasswordError')}">
                                    <span class="white-text">Powtórzone hasło jest niepoprawne.</span>
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
