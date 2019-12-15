<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<navigator>
    <nav>
        <div class="nav-wrapper">
            <a href="/" class="brand-logo">Readers</a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="/account">My account</a></li>
                <li><a href="/account/books">My books</a></li>
                <li><a href="/books">Find book</a></li>

                <c:choose>
                    <c:when test="${sessionScope.userId == null}">
                        <li><a href="/login">Login</a></li>

                    </c:when>
                    <c:otherwise>
                        <li><a href="/logout">Logout</a></li>
                    </c:otherwise>
                </c:choose>

            </ul>
        </div>
    </nav>

</navigator>
