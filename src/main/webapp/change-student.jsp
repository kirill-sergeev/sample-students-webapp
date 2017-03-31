<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="student" scope="request" type="com.sergeev.studapp.model.User"/>
<jsp:useBean id="groups" scope="request" type="java.util.List<com.sergeev.studapp.model.Group>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Change student - ${student.firstName} ${student.lastName}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="${pageContext.request.contextPath}/student" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" class="form-control" value="${student.id}">
                </div>
                <div class="form-group">
                    <input type="text" name="first-name" class="form-control" value="${student.firstName}"
                           placeholder="First name...">
                </div>
                <div class="form-group">
                    <input type="text" name="last-name" class="form-control" value="${student.lastName}"
                           placeholder="Last name...">
                </div>
                <div class="form-group">
                    <label>Group
                        <select class="form-control" name="group">
                            <c:forEach items="${groups}" var="group">
                                <option value="${group.id}" ${group.id == student.group.id ? 'selected="selected"' : ''}>${group.title}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
