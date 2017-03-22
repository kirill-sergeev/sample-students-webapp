<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="lesson" scope="request" type="com.sergeev.studapp.model.Lesson"/>
<jsp:useBean id="types" scope="request" type="com.sergeev.studapp.model.Lesson.LessonType[]"/>
<jsp:useBean id="orders" scope="request" type="com.sergeev.studapp.model.Lesson.LessonOrder[]"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Change lesson in course ${lesson.course.discipline.title} - ${lesson.course.group.title} - ${lesson.date}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="update-lesson" method="POST">
                <div class="form-group">
                    <input type="hidden" name="id" class="form-control" value="${lesson.id}">
                </div>
                <div class="form-group">
                    <input type="hidden" name="group" class="form-control" value="${lesson.course.group.id}">
                </div>
                <div class="form-group">
                    <input type="hidden" name="discipline" class="form-control" value="${lesson.course.discipline.id}">
                </div>
                <div class="form-group">
                    <label>Type
                        <select class="form-control" name="type">
                            <c:forEach items="${types}" var="type">
                                <option value="${type.id}" ${type.id == lesson.type.id ? 'selected="selected"' : ''}>${type.type}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <label>Time
                        <select class="form-control" name="number">
                            <c:forEach items="${orders}" var="order">
                                <option value="${order.number}" ${order.number == lesson.order.number ? 'selected="selected"' : ''}>${order.startTime} - ${order.endTime}</option>
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
