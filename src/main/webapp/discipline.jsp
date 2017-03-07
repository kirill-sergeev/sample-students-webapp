<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Discipline info"/>
</jsp:include>
<jsp:useBean id="courses" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Course>"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Courses by discipline</h3>
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
                            <th>Teacher</th>
                            <th>Group</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher?id=${course.teacher.id}">${course.teacher.firstName} ${course.teacher.lastName}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/group?id=${course.group.id}">${course.group.title}</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>