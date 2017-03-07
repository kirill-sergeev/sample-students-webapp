<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="groups" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Group>"/>
<jsp:useBean id="firstName" scope="request" type="java.lang.String"/>
<jsp:useBean id="lastName" scope="request" type="java.lang.String"/>
<jsp:useBean id="group" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="id" scope="request" type="java.lang.Integer"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Change student info"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="update-student" method="POST">
                <div class="form-group">
                    <input type="hidden" name="student" class="form-control" value="${id}">
                </div>
                <div class="form-group">
                    <input type="text" name="first-name" class="form-control" value="${firstName}"
                           placeholder="First name...">
                </div>
                <div class="form-group">
                    <input type="text" name="last-name" class="form-control" value="${lastName}"
                           placeholder="Last name...">
                </div>
                <div class="form-group">
                    <label>Group</label>
                    <select class="form-control" name="group">
                        <c:forEach items="${groups}" var="gr">
                            <option value="${gr.id}" ${gr.id == group ? 'selected="selected"' : ''}>${gr.title}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
