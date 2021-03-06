<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="teachers" scope="request" type="java.util.List<com.sergeev.studapp.model.User>"/>
<jsp:useBean id="courses" scope="request" type="java.util.List<com.sergeev.studapp.model.Course>"/>

<c:set var="title" scope="request" value="All teachers"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-5">
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
                            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                <th>Actions</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="teacher" items="${teachers}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher/${teacher.id}">${teacher.firstName} ${teacher.lastName}</a>
                                </td>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <td>
                                        <form id="delete${teacher.id}"
                                              action="${pageContext.request.contextPath}/teacher"
                                              method="POST">
                                            <input type="hidden" name="id" value="${teacher.id}">
                                            <input type="hidden" name="action" value="remove">
                                        </form>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <button class="btn btn-info btn-secondary" type="button"
                                                    onclick="location.href='${pageContext.request.contextPath}/teacher/${teacher.id}/change'">
                                                Change
                                            </button>
                                            <button class="btn btn-danger btn-secondary" type="submit"
                                                    form="delete${teacher.id}"
                                                    <c:forEach var="course" items="${courses}">
                                                        <c:if test="${teacher.id == course.teacher.id}">disabled</c:if>
                                                    </c:forEach>>
                                                Delete
                                            </button>
                                        </div>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/teacher/new'">
                    New Teacher
                </button>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
