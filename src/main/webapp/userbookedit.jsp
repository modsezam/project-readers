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

    <title>Yours book</title>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body>
<%--<jsp:useBean id="userBook" scope="request" type="com.github.magdanadratowska.model.UserBook"/>--%>

<div class="row">
    <jsp:include page="navigator.jsp"></jsp:include>
</div>

<div class="section no-pad-bot">
    <div class="container">

        <div class="row">
            <h4 class="red-text lighten-2">Book</h4>
        </div>


        <div class="row">
            <div class="col m12">
                <div class="card-panel white">
                    <form action="/books-edit" method="post">
                        <div class="row">
                            <div class="input-field">
                                <input type="hidden" value="${requestScope.userBook.book.id}" id="id" name="id">
                                <input value="${requestScope.userBook.book.title}" id="title" name="title" type="text"
                                       class="validate">
                                <label for="title">Book title</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field">
                                <input value="${requestScope.userBook.book.authorName}" id="author_name" name="author_name" type="text"
                                       class="validate">
                                <label for="author_name">Author name</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field">
                                <input value="${requestScope.userBook.book.authorSurname}" id="author_surname" name="author_surname"
                                       type="text"
                                       class="validate">
                                <label for="author_surname">Author surname</label>
                            </div>
                        </div>
                        <div class="row">
                            <button class="btn waves-effect waves-light red lighten-2 white-text" type="submit" name="action">Update
                                book
                                <i class="material-icons right">edit</i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col m12">
                <div class="card-panel white">

                    <form action="/account/books-edit?bookId=${requestScope.userBook.book.id}&action=rate"
                          method="post">

                        <div class="row">
                            <label class="col m2 s4">Rate Select</label><br>
                            <select class="browser-default col m2 s4" name="rate">
                                <option value="" disabled selected>Choose</option>
                                <option value="1" <c:if test="${requestScope.userBook.rate eq '1'}">selected</c:if>>☆
                                </option>
                                <option value="2" <c:if test="${requestScope.userBook.rate eq '2'}">selected</c:if>>☆☆
                                </option>
                                <option value="3" <c:if test="${requestScope.userBook.rate eq '3'}">selected</c:if>>
                                    ☆☆☆
                                </option>
                                <option value="4" <c:if test="${requestScope.userBook.rate eq '4'}">selected</c:if>>☆☆☆☆
                                </option>
                                <option value="5" <c:if test="${requestScope.userBook.rate eq '5'}">selected</c:if>>
                                    ☆☆☆☆☆
                                </option>
                            </select>
                        </div>
                        <div class="row">
                            <button class="btn waves-effect waves-light red lighten-2 white-text" type="submit"
                                    name="action">Rate book
                                <i class="material-icons right">sort</i>
                            </button>
                        </div>

                    </form>


                </div>
            </div>
        </div>


        <div class="row">
            <div class="col m12">
                <div class="card-panel white">

                    <form id="review_form"
                          action="/account/books-edit?bookId=${requestScope.userBook.book.id}&action=review"
                          method="post">
                        <div class="row">
                            <div class="input-field col s12">
                        <textarea id="review" name="review" form="review_form"
                                  class="materialize-textarea">${requestScope.userBook.review}</textarea>
                                <label for="review">Review book</label>
                            </div>
                        </div>
                        <div class="row">
                            <button class="btn waves-effect waves-light red lighten-2 white-text" type="submit" name="action">Review book
                                <i class="material-icons right">rate_review</i>
                            </button>
                        </div>
                    </form>

                </div>
            </div>
        </div>


    </div>
</div>

<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>

</html>