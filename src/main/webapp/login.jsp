<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Main page"/>
</jsp:include>

<div class="container">

    <form class="form-signin" action="login" method="POST">
        <h2 class="form-signin-heading">Please sign in</h2>

        <label for="inputLogin" class="sr-only">Login</label>
        <input type="text" name="login"  id="inputLogin" class="form-control" placeholder="Login" required autofocus>

        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>

        <div class="checkbox">
            <label>
                <input type="checkbox" value="remember"> Remember me
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>

</div>


<jsp:include flush="true" page="partial/footer.jsp"/>