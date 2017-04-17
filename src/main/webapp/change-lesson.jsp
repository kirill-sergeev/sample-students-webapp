<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="lesson" scope="request" type="com.sergeev.studapp.model.Lesson"/>
<jsp:useBean id="types" scope="request" type="com.sergeev.studapp.model.Lesson.Type[]"/>
<jsp:useBean id="orders" scope="request" type="com.sergeev.studapp.model.Lesson.Order[]"/>

<c:set var="title" scope="request" value="Change lesson in course ${lesson.course.discipline.title} - ${lesson.course.group.title} - ${lesson.date}"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="${pageContext.request.contextPath}/lesson" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" class="form-control" value="${lesson.id}">
                    <input type="hidden" name="group" class="form-control" value="${lesson.course.group.id}">
                    <input type="hidden" name="discipline" class="form-control" value="${lesson.course.discipline.id}">
                </div>
                <div class="form-group">
                    <label>Type
                        <select class="form-control" name="type">
                            <c:forEach items="${types}" var="type">
                                <option value="${type.name()}" ${type.name() == lesson.type.name() ? 'selected="selected"' : ''}>${type.name()}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <label>Time
                        <select class="form-control" name="number">
                            <c:forEach items="${orders}" var="order">
                                <option value="${order.ordinal()}" ${order.ordinal() == lesson.order.ordinal() ? 'selected="selected"' : ''}>${order.startTime} - ${order.endTime}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <label>Date
                        <input type="date" name="date" class="form-control" value="${lesson.date}">
                    </label>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
