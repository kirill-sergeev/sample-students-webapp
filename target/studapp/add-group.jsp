<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request" value="Add a new group"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-3">
            <br>
            <form action="${pageContext.request.contextPath}/group" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="create">
                    <label>Add a new group</label>
                    <input type="text" name="title" class="form-control" placeholder="Title...">
                </div>
                <button type="submit" class="btn btn-block btn-primary">Create</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>