<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="courses" scope="request" type="java.util.List<com.sergeev.studapp.model.Course>"/>
<jsp:useBean id="groupsStudents" scope="request"
             type="java.util.LinkedHashMap<com.sergeev.studapp.model.Group, java.lang.Integer>"/>

<c:set var="title" scope="request" value="All groups"/>
<jsp:include flush="true" page="partial/header.jsp"/>


<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Groups List</h3>
            <c:choose>
                <c:when test="${empty groupsStudents}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No students!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Group Title</th>
                            <th>Number of students</th>
                            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                <th>Actions</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${groupsStudents}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/group/${item.key.id}">${item.key.title}</a>
                                </td>
                                <td>${item.value}</td>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <td>
                                        <form id="delete${item.key.id}"
                                              action="${pageContext.request.contextPath}/group"
                                              method="POST">
                                            <input type="hidden" name="id" value="${item.key.id}">
                                            <input type="hidden" name="action" value="delete">
                                        </form>

                                        <div class="btn-group btn-group-sm" role="group">
                                            <button class="btn btn-info btn-secondary" type="button"
                                                    onclick="location.href='${pageContext.request.contextPath}/group/${item.key.id}/change'">
                                                Change
                                            </button>
                                            <button class="btn btn-danger btn-secondary" type="submit"
                                                    form="delete${item.key.id}"
                                                    <c:forEach var="course" items="${courses}">
                                                        <c:if test="${item.key.id == course.group.id}">disabled</c:if>
                                                    </c:forEach>
                                                    <c:if test="${item.value!=0}">disabled</c:if>>
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
                        onclick="location.href='${pageContext.request.contextPath}/group/new'">
                    Add a new group
                </button>
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='${pageContext.request.contextPath}/course/new'">
                    Add a new course
                </button>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
