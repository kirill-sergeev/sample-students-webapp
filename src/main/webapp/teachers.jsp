<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="teachers" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Teacher>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Teachers List"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Teachers List</h3>
            <c:choose>
                <c:when test="${empty teachers}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No teachers!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="teacher" items="${teachers}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher?id=${teacher.id}">${teacher.firstName} ${teacher.lastName}</a>
                                </td>
                                <td>
                                    <form action="change-teacher" method="POST">
                                        <input type="hidden" name="id" value="${teacher.id}"/>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button type="submit" class="btn btn-info btn-secondary">Change</button>
                                            <button type="submit" class="btn btn-danger btn-secondary"
                                                    formaction="remove-teacher">Delete
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
            <h3><a href="new-teacher">Add a new teacher...</a></h3>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
