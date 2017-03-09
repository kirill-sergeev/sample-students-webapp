<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="students" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Student>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Sudents List"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
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
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/student?id=${student.id}">${student.firstName} ${student.lastName}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/group?id=${student.group.id}">${student.group.title}</a>
                                </td>
                                <td>
                                    <form action="change-student" method="POST">
                                        <input type="hidden" name="id" value="${student.id}"/>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button type="submit" class="btn btn-info btn-secondary">Change</button>
                                            <button type="submit" class="btn btn-danger btn-secondary"
                                                    formaction="remove-student">Delete
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
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>

