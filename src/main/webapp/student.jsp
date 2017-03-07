<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="student" scope="request" type="com.sergeev.studapp.model.Student"/>
<jsp:useBean id="map" scope="request" type="java.util.Map<com.sergeev.studapp.model.Course, java.lang.Double>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Student - ${student.firstName} ${student.lastName}"/>
</jsp:include>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Student's info page</h3>
            <c:if test="${not empty student}">
                <br>
                <h4>${student.firstName} ${student.lastName} from <a
                        href="${pageContext.request.contextPath}/group?id=${student.group.id}">${student.group.title}</a>
                </h4>
                <br>
                <h4>Courses</h4>
                <c:choose>
                    <c:when test="${empty map}">
                        <div class="alert alert-warning text-center" role="alert">
                            <strong>No courses!</strong>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-hover table-sm">
                            <thead>
                            <tr>
                                <th>Discipline</th>
                                <th>Avg. mark</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="map" items="${map}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/discipline?id=${map.key.discipline.id}">${map.key.discipline.title}</a>
                                    </td>
                                    <td>
                                        ${map.value}
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>