<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>
<jsp:useBean id="dateNow" scope="request" type="java.time.LocalDate"/>
<jsp:useBean id="lessons" scope="request" type="java.util.List<com.sergeev.studapp.model.Lesson>"/>

<c:set var="title" scope="request" value="Lessons - ${group.id}"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-10">
            <h3>Lessons in group <a href="${pageContext.request.contextPath}/group/${group.id}">${group.title}</a>
            </h3>
            <c:choose>
                <c:when test="${empty lessons}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No lessons!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Discipline</th>
                            <th>Teacher</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Start time</th>
                            <th>End time</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="lesson" items="${lessons}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/discipline/${lesson.course.discipline.id}">${lesson.course.discipline.title}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher/${lesson.course.teacher.id}">${lesson.course.teacher.firstName} ${lesson.course.teacher.lastName}</a>
                                </td>
                                <td>${lesson.type.name().toLowerCase()}</td>
                                <td>${lesson.date}</td>
                                <td>${lesson.order.startTime}</td>
                                <td>${lesson.order.endTime}</td>
                                <td>

                                    <form id="delete${lesson.id}" action="${pageContext.request.contextPath}/lesson"
                                          method="POST">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${lesson.id}">
                                    </form>
                                    <div class="btn-group btn-group-sm" role="group">
                                        <button class="btn btn-info btn-secondary"
                                                onclick="location.href='${pageContext.request.contextPath}/lesson/${lesson.id}'">
                                            Info
                                        </button>
                                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                            <c:if test="${lesson.date>=dateNow}">
                                                <button class="btn btn-info btn-secondary" type="button"
                                                        onclick="location.href='${pageContext.request.contextPath}/lesson/${lesson.id}/change'">
                                                    Change
                                                </button>
                                                <button class="btn btn-danger btn-secondary" type="submit"
                                                        form="delete${lesson.id}">Delete
                                                </button>
                                            </c:if>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/lesson/new/group/${group.id}'">
                    Add a new lesson
                </button>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>