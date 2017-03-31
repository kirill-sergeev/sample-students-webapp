<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Change group - ${group.title}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="${pageContext.request.contextPath}/group" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" class="form-control" value="${group.id}">
                </div>
                <div class="form-group">
                    <input type="text" name="title" class="form-control" value="${group.title}"
                           placeholder="Title...">
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>