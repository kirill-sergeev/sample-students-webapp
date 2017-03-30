<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="groupsStudents" scope="request"
             type="java.util.LinkedHashMap<com.sergeev.studapp.model.Group, java.lang.Integer>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Groups List"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Groups List</h3>
            <c:choose>
                <c:when test="${empty groupsStudents}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No students!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Group Title</th>
                            <th>Number of students</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${groupsStudents}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/group?id=${item.key.id}">${item.key.title}</a>
                                </td>
                                <td>${item.value}</td>
                                <td>
                                    <form action="change-group" method="POST">
                                        <input type="hidden" name="id" value="${item.key.id}"/>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button type="submit" class="btn btn-info btn-secondary">Change</button>
                                            <button type="submit" class="btn btn-danger btn-secondary"
                                                    formaction="remove-group" <c:if test="${item.value!=0}">disabled</c:if>>Delete
                                            </button>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <h3><a href="group/new">Add a new group...</a></h3>
            <h3><a href="add-course">Add a new course...</a></h3>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
