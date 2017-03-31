<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="teacher" scope="request" type="com.sergeev.studapp.model.User"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Change teacher - ${teacher.firstName} ${teacher.lastName}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="${pageContext.request.contextPath}/teacher" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" class="form-control" value="${teacher.id}">
                </div>
                <div class="form-group">
                    <input type="text" name="first-name" class="form-control" value="${teacher.firstName}"
                           placeholder="First name...">
                </div>
                <div class="form-group">
                    <input type="text" name="last-name" class="form-control" value="${teacher.lastName}"
                           placeholder="Last name...">
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
