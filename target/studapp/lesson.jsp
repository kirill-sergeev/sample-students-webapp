<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="lesson" scope="request" type="com.sergeev.studapp.model.Lesson"/>
<jsp:useBean id="dateNow" scope="request" type="java.time.LocalDate"/>
<jsp:useBean id="marks" scope="request" type="java.util.List<com.sergeev.studapp.model.Mark>"/>

<c:set var="title" scope="request" value="Lesson in group ${lesson.course.group.title} - ${lesson.course.discipline.title} - ${lesson.date}"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <table class="table table-hover table-sm">
                <thead>
                <tr>
                    <th>Group</th>
                    <th>Discipline</th>
                    <th>Teacher</th>
                    <th>Type</th>
                    <th>Date</th>
                    <th>Start time</th>
                    <th>End time</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/group/${lesson.course.group.id}">${lesson.course.group.title}</a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/discipline/${lesson.course.discipline.id}">${lesson.course.discipline.title}</a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/teacher/${lesson.course.teacher.id}">${lesson.course.teacher.firstName} ${lesson.course.teacher.lastName}</a>
                    </td>
                    <td>${lesson.type}</td>
                    <td>${lesson.date}</td>
                    <td>${lesson.order.startTime}</td>
                    <td>${lesson.order.endTime}</td>
                </tr>
                </tbody>
            </table>

            <c:if test="${lesson.date<=dateNow}">
                <h3>Marks</h3>
                <c:choose>
                    <c:when test="${empty marks}">
                        <div class="alert alert-warning text-center" role="alert">
                            <strong>No marks!</strong>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-hover table-sm">
                            <thead>
                            <tr>
                                <th>Student</th>
                                <th>Mark</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="mark" items="${marks}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/student/${mark.student.id}">${mark.student.firstName} ${mark.student.lastName}</a>
                                    </td>
                                    <td>${mark.value}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/mark"
                                              method="POST">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${mark.id}">
                                            <button class="btn btn-danger btn-sm btn-secondary" type="submit">Delete
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/mark/new/group/${lesson.course.group.id}/lesson/${lesson.id}'">
                    Add a new mark
                </button>
            </c:if>
            <c:if test="${lesson.date>=dateNow}">
                <form id="delete${lesson.id}" action="${pageContext.request.contextPath}/lesson"
                      method="POST">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${lesson.id}">
                </form>
                <div class="btn-group btn-group-sm" role="group">
                    <button class="btn btn-info btn-secondary" type="button"
                            onclick="location.href='${pageContext.request.contextPath}/lesson/${lesson.id}/change'">
                        Change
                    </button>
                    <button class="btn btn-danger btn-secondary" type="submit"
                            form="delete${lesson.id}">Delete
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>