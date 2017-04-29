<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="students" scope="request" type="java.util.List<com.sergeev.studapp.model.User>"/>

<c:set var="title" scope="request" value="All students"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-6">
            <h3>Students List</h3>
            <c:choose>
                <c:when test="${empty students}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No students!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Full Name</th>
                            <th>Group</th>
                            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                <th>Actions</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/student/${student.id}">${student.firstName} ${student.lastName}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/group/${student.group.id}">${student.group.title}</a>
                                </td>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <td>
                                        <form id="delete${student.id}"
                                              action="${pageContext.request.contextPath}/teacher"
                                              method="POST">
                                            <input type="hidden" name="id" value="${student.id}">
                                            <input type="hidden" name="action" value="remove">
                                        </form>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button class="btn btn-info btn-secondary" type="button"
                                                    onclick="location.href='${pageContext.request.contextPath}/student/${student.id}/change'">
                                                Change
                                            </button>
                                            <button class="btn btn-danger btn-secondary" type="submit"
                                                    form="delete${student.id}">Delete
                                            </button>
                                        </div>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/student/new'">
                    New student
                </button>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>

