<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>${param.title}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" >
    <%--<link rel='stylesheet' href='webjars/bootstrap/4.0.0-alpha.6-1/css/bootstrap.min.css'>--%>
</head>
<body>

<nav class="navbar navbar-light navbar-toggleable-md bg-faded" style="background-color: #e3f2fd;">
    <a class="navbar-brand" href="/">Student search</a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="groups">Groups</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="students">Students</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="teachers">Teachers</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="disciplines">Disciplines</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0" action="search-student" method="POST">

            <div class="input-group">
                <input type="text" name="name" class="form-control" placeholder="Search for students...">
                <span class="input-group-btn">
                        <button class="btn btn-secondary" type="submit">Go!</button>
                    </span>
            </div>
        </form>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">Log In</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="logout">Log Out</a>
            </li>
        </ul>
    </div>
</nav>