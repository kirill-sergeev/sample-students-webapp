<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${title}</title>
    <link rel="shortcut icon" href="../res/img/favicon.ico" type="image/x-icon">
    <link rel="icon" href="../res/img/favicon.ico" type="image/x-icon">
    <link rel='stylesheet' href='${pageContext.request.contextPath}/webjars/bootstrap/4.0.0-alpha.6-1/css/bootstrap.min.css'>
</head>

<c:if test="${!noNavbar}">
    <jsp:include flush="true" page="navbar.jsp"/>
</c:if>
<body>