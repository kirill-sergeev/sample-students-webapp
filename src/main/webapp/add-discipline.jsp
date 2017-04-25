<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request" value="Add a new discipline"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-3">
            <form action="${pageContext.request.contextPath}/discipline" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="create">
                    <br>
                    <label>Add a new discipline</label>
                    <input type="text" name="title" class="form-control" placeholder="Title...">
                </div>
                <button type="submit" class="btn btn-block btn-primary">Create</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>