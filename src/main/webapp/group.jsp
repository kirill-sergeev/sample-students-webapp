<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>
<jsp:useBean id="students" scope="request" type="java.util.List<com.sergeev.studapp.model.User>"/>
<jsp:useBean id="courses" scope="request" type="java.util.List<com.sergeev.studapp.model.Course>"/>
<jsp:useBean id="lessons" scope="request" type="java.util.List<com.sergeev.studapp.model.Lesson>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Group - ${group.title}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Students in group <a href="${pageContext.request.contextPath}/group/{group.id}">${group.title}</a>
            </h3>
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
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/student/${student.id}">${student.firstName} ${student.lastName}</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Courses in group ${group.title}</h3>
            <c:choose>
                <c:when test="${empty courses}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No courses!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Discipline</th>
                            <th>Teacher</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/discipline/${course.discipline.id}">${course.discipline.title}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher/${course.teacher.id}">${course.teacher.firstName} ${course.teacher.lastName}</a>
                                </td>
                                <td>
                                    <form id="delete${course.id}" action="${pageContext.request.contextPath}/course"
                                          method="POST">
                                        <input type="hidden" name="id" value="${course.id}">
                                        <input type="hidden" name="action" value="delete">
                                    </form>

                                    <div class="btn-group btn-group-sm" role="group">
                                        <button class="btn btn-info btn-secondary" type="button"
                                                onclick="location.href='${pageContext.request.contextPath}/course/${course.id}/change'">
                                            Change
                                        </button>
                                        <button class="btn btn-danger btn-secondary" type="submit"
                                                <c:forEach var="lesson" items="${lessons}">
                                                    <c:if test="${course.id == lesson.course.id}">disabled</c:if>
                                                </c:forEach>
                                                form="delete${course.id}">Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty courses}">
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/lesson/group/${group.id}'">
                    All lessons in group ${group.title}
                </button>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>