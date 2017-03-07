<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Add a new student"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="create-student" method="POST">
                <div class="form-group">
                    <input type="text" name="first-name" class="form-control" placeholder="First name...">
                </div>
                <div class="form-group">
                    <input type="text" name="last-name" class="form-control" placeholder="Last name...">
                </div>
                <div class="form-group">
                    <label>Group</label>
                    <select class="form-control" name="group">
                        <jsp:useBean id="groups" scope="request"
                                     type="java.util.ArrayList<com.sergeev.studapp.model.Group>"/>
                        <c:forEach items="${groups}" var="group">
                            <option value="${group.id}">${group.title}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>