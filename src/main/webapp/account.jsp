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

    <title>Yours account</title>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body style="background-color:#ffebee">



<div class="container">
    <div class="row">
        <jsp:include page="navigator.jsp"></jsp:include>
    </div>

    <div class="row">

        <div class="col s5">
            <div class="row">
                <div class="col s12 m7">
                    <div class="card">
                        <div class="card-image">
                            <img src="https://i0.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1">
                            <span class="card-title"></span>
                        </div>
                        <div class="card-content">
                            <h4>Your data:</h4>
                            <table>
                                <tbody>
                                <tr>
                                    <td><b>Your Name</b></td>
                                    <td><%=request.getAttribute("userName")%></td>
                                </tr>
                                <tr>
                                    <td><b>User type</b></td>
                                    <td><%=request.getAttribute("type")%></td>
                                </tr>
                                <tr>
                                    <td><b>Register date</b></td>
                                    <td><%=request.getAttribute("registerDate")%></td>
                                </tr>
                                <tr>
                                    <td><b>Your email</b></td>
                                    <td><%=request.getAttribute("email")%></td>
                                </tr>
                                <tr>
                                    <td><b>Total books read </b></td>
                                    <td><%=request.getAttribute("sumOfReadBooks")%></td>
                                </tr>
                                <tr>
                                    <td colspan="2"><a class="btn btn-primary btn-lg btn-block" href="/logout">Logout</a></td>
                                </tr>

                                <c:if test="${sessionScope.userType eq 'ADMIN'}">
                                <tr>
                                    <td colspan="2"><a class="btn btn-primary btn-lg btn-block" href="/user-list">List of users</a></td>
                                </tr>
                                </c:if>

                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="col s7">
            <!-- Teal page content  -->
            <table>
                <thead>
                <tr>
                    <th>Recently read</th>
                </tr>
                </thead>

                <tr>
                    <td><a href="#!">${lastReadBook.title}</a></td>
                </tr>
                <tr>
                    <td><a href="#!">"Clean Code" Robert C. Martin/a></a></td>
                </tr>
                <thead>
                <tr>
                    <th>Yours books:</th>
                </tr>
                </thead>
                <tr>
                    <td>? list form database in a window ?</td>
                </tr>
                <thead>
                <tr>
                    <th>Books in highlights</th>
                </tr>
                </thead>
                <tr>
                    <td>Test attribute: <%=request.getAttribute("testAttribute")%></td>
                </tr>
                <tr>
                    <td>Test attribute: <c:out value="${requestScope.testAttribute}" /></td>
                </tr>
                <tr>
                    <td>Test attribute: <c:out value="${testAttribute}" /></td>
                </tr>
                <tr>
                    <td>? list of 5 most read books in database ?</td>
                </tr>

            </table>


        </div>
    </div>

</div>


<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>