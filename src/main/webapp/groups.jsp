<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Groups"/>
</jsp:include>
<jsp:useBean id="groups" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Group>"/>

<div class="container">
    <div class="row justify-content-md-center">
        <c:choose>
        <c:when test="${empty groups}">
            <div class="alert alert-warning text-center" role="alert">
                <strong>No students!</strong>
            </div>
        </c:when>
        <c:otherwise>
        <div class="col-8">
            <table class="table table-hover table-sm">
                <thead>
                <tr>
                    <th>Group Title</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="group" items="${groups}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/group?id=${group.id}">${group.title}</a></td>
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
