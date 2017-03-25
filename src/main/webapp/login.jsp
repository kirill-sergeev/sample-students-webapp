<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css">
</head>
<body>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form class="form-signin" action="login" method="POST">
                <h2 class="form-signin-heading">Please sign in</h2>

                <label for="inputLogin" class="sr-only">Login</label>
                <input type="text" name="login" id="inputLogin" class="form-control" placeholder="Login" required
                       autofocus>

                <label for="inputPassword" class="sr-only">Password</label>
                <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password"
                       required>

                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="remember"> Remember me
                    </label>
                </div>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
    </div>
</div>


<jsp:include flush="true" page="partial/footer.jsp"/>