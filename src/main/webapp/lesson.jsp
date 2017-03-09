<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="lesson" scope="request" type="com.sergeev.studapp.model.Lesson"/>
<jsp:useBean id="marks" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Mark>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title"
               value="Lesson in group ${lesson.course.group.title} - ${lesson.course.discipline.title} - ${lesson.date}"/>
</jsp:include>

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
                        <a href="${pageContext.request.contextPath}/group?id=${lesson.course.group.id}">${lesson.course.group.title}</a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/discipline?id=${lesson.course.discipline.id}">${lesson.course.discipline.title}</a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/teacher?id=${lesson.course.teacher.id}">${lesson.course.teacher.firstName} ${lesson.course.teacher.lastName}</a>
                    </td>
                    <td>${lesson.type}</td>
                    <td>${lesson.date}</td>
                    <td>${lesson.order.startTime}</td>
                    <td>${lesson.order.endTime}</td>
                </tr>
                </tbody>
            </table>

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
                                    <a href="${pageContext.request.contextPath}/student?id=${mark.student.id}">${mark.student.firstName} ${mark.student.lastName}</a>
                                </td>
                                <td>${mark.value}</td>
                                <td>
                                    <form action="remove-mark" method="POST">
                                        <input type="hidden" name="id" value="${mark.id}"/>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button type="submit" class="btn btn-danger">Delete</button>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <form action="change-lesson" method="POST">
                <input type="hidden" name="id" value="${lesson.id}"/>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="submit" class="btn btn-info btn-secondary">Change lesson</button>
                    <button type="submit" class="btn btn-danger btn-secondary"
                            formaction="remove-lesson">Delete lesson
                    </button>
                </div>
            </form>
            <form action="new-mark" method="POST">
                <input type="hidden" name="lesson" value="${lesson.id}"/>
                <input type="hidden" name="group" value="${lesson.course.group.id}"/>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="submit" class="btn btn-info">Add a new mark</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>