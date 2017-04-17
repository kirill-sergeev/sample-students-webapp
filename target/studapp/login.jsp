<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="exception">
    <jsp:useBean id="login" scope="request" type="java.lang.String"/>
</c:catch>
<c:set var="title" scope="request" value="Log in"/>
<c:set var="noNavbar" scope="request" value="true"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form class="form-signin" action="login" method="POST">
                <h2 class="form-signin-heading">Please sign in</h2>

                <label for="inputLogin" class="sr-only">Login</label>
                <input type="text" name="login"
                <c:if test="${login != null}"> value="${login}"</c:if>
                       id="inputLogin" class="form-control" placeholder="Login"
                       <c:if test="${login == null}">autofocus</c:if> required>

                <label for="inputPassword" class="sr-only">Password</label>
                <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password"
                       required <c:if test="${login != null}">autofocus</c:if>>

                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="remember" value="remember"> Remember me
                    </label>
                </div>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>