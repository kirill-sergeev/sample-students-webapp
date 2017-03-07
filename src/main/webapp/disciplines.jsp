<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="disciplines" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Discipline>"/>

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
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="discipline" items="${disciplines}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/discipline?id=${discipline.id}">${discipline.title}</a>
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
