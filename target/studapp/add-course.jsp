<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="disciplines" scope="request" type="java.util.List<com.sergeev.studapp.model.Discipline>"/>
<jsp:useBean id="groups" scope="request" type="java.util.List<com.sergeev.studapp.model.Group>"/>
<jsp:useBean id="teachers" scope="request" type="java.util.List<com.sergeev.studapp.model.User>"/>

<c:set var="title" scope="request" value="Add a new course"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-3">
            <br>
            <label>Add a new course</label>
            <form action="${pageContext.request.contextPath}/course" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="save">
                    <label>Discipline  </label>
                        <select class="form-control" name="discipline">
                            <option disabled selected value>< select an option ></option>
                            <c:forEach items="${disciplines}" var="discipline">
                                <option value="${discipline.id}">${discipline.title}</option>
                            </c:forEach>
                        </select>
                </div>
                <div class="form-group">
                    <label>Group</label>
                        <select class="form-control" name="group">
                            <option disabled selected value>< select an option ></option>
                            <c:forEach items="${groups}" var="group">
                                <option value="${group.id}">${group.title}</option>
                            </c:forEach>
                        </select>
                </div>
                <div class="form-group">
                    <label>Teacher   </label>
                        <select class="form-control" name="teacher">
                            <option disabled selected value>< select an option ></option>
                            <c:forEach items="${teachers}" var="teacher">
                                <option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>
                            </c:forEach>
                        </select>
                </div>
                <button type="submit" class="btn  btn-block btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>