<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>
<jsp:useBean id="students" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Student>"/>
<jsp:useBean id="courses" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Course>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Group - ${group.title}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Students in group <a href="${pageContext.request.contextPath}/group?id=${group.id}">${group.title}</a>
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
                                    <a href="${pageContext.request.contextPath}/student?id=${student.id}">${student.firstName} ${student.lastName}</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:if test="${not empty students}">
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
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="course" items="${courses}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/discipline?id=${course.discipline.id}">${course.discipline.title}</a>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/teacher?id=${course.teacher.id}">${course.teacher.firstName} ${course.teacher.lastName}</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty courses}">
        <div class="row justify-content-md-center">
            <div class="col-8">
                <h3><a href="${pageContext.request.contextPath}/lessons?id=${group.id}">All lessons in
                    group ${group.title}</a></h3>
            </div>
        </div>
    </c:if>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>