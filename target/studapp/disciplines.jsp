<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="disciplines" scope="request" type="java.util.List<com.sergeev.studapp.model.Discipline>"/>
<jsp:useBean id="courses" scope="request" type="java.util.List<com.sergeev.studapp.model.Course>"/>

<c:set var="title" scope="request" value="All disciplines"/>
<jsp:include flush="true" page="partial/header.jsp"/>


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
                                    <a href="${pageContext.request.contextPath}/discipline/${discipline.id}">${discipline.title}</a>
                                </td>
                                <td>
                                    <form id="delete${discipline.id}"
                                          action="${pageContext.request.contextPath}/discipline" method="POST">
                                        <input type="hidden" name="id" value="${discipline.id}">
                                        <input type="hidden" name="action" value="delete">
                                    </form>
                                    <div class="btn-group btn-group-sm" role="group">
                                        <button class="btn btn-info btn-secondary" type="button"
                                                onclick="location.href='${pageContext.request.contextPath}/discipline/${discipline.id}/change'">
                                            Change
                                        </button>
                                        <button class="btn btn-danger btn-secondary" type="submit"
                                                form="delete${discipline.id}"
                                                <c:forEach var="course" items="${courses}">
                                                    <c:if test="${discipline.id == course.discipline.id}">disabled</c:if>
                                                </c:forEach>>Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <button class="btn btn-info btn-secondary" type="button"
                    onclick="location.href='${pageContext.request.contextPath}/discipline/new'">
                Add a new discipline
            </button>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
