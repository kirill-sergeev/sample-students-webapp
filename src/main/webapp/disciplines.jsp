<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="disciplines" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Discipline>"/>
<jsp:useBean id="courses" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Course>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Disciplines List"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Disciplines List</h3>
            <c:choose>
                <c:when test="${empty disciplines}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No disciplines!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="discipline" items="${disciplines}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/discipline?id=${discipline.id}">${discipline.title}</a>
                                </td>
                                <td>
                                    <form action="change-discipline" method="POST">
                                        <input type="hidden" name="id" value="${discipline.id}"/>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button type="submit" class="btn btn-info btn-secondary">Change</button>
                                            <button type="submit" class="btn btn-danger btn-secondary"
                                                    formaction="remove-discipline" <c:forEach var="course" items="${courses}">
                                                <c:if test="${discipline.id == course.discipline.id}">disabled</c:if>
                                            </c:forEach>>Delete
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
            <h3><a href="new-discipline">Add a new discipline...</a></h3>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
