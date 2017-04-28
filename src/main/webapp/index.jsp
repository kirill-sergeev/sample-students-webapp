<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" scope="request" value="Main page"/>
<jsp:include flush="true" page="partial/header.jsp"/>
<br>
<img src="res/img/student.png"  class="rounded mx-auto d-block">
<jsp:useBean id="result" scope="request" type="java.lang.String"/>
<p>${result}</p>
<jsp:include flush="true" page="partial/footer.jsp"/>