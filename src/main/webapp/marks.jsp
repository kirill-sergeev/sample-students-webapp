<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="student" scope="request" type="com.sergeev.studapp.model.User"/>
<jsp:useBean id="discipline" scope="request" type="com.sergeev.studapp.model.Discipline"/>
<jsp:useBean id="marks" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Mark>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Marks - ${student.firstName} ${student.lastName} - ${discipline.title}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>
                <a href="${pageContext.request.contextPath}/student?id=${student.id}">${student.firstName} ${student.lastName}'s</a>
                marks in <a
                    href="${pageContext.request.contextPath}/discipline?id=${discipline.id}">${discipline.title}</a>
            </h3>
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
                            <th>Teacher</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Mark</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="mark" items="${marks}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher?id=${mark.lesson.course.teacher.id}">${mark.lesson.course.teacher.firstName} ${mark.lesson.course.teacher.lastName}</a>
                                </td>
                                <td>${mark.lesson.type}</td>
                                <td>${mark.lesson.date}</td>
                                <td>${mark.value}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <form action="new-mark" method="POST">
                <input type="hidden" name="lesson" value="${marks.get(0).lesson.id}"/>
                <input type="hidden" name="group" value="${student.group.id}"/>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="submit" class="btn btn-info">Add a new mark</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>