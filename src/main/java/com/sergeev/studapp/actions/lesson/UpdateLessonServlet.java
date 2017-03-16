package com.sergeev.studapp.actions.lesson;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "UpdateLessonServlet", urlPatterns = "/update-lesson")
public class UpdateLessonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonId = request.getParameter("id");
        String groupId = request.getParameter("group");
        String disciplineId = request.getParameter("discipline");
        String typeId = request.getParameter("type");
        Integer number = Integer.valueOf(request.getParameter("number"));
        Date date = Date.valueOf((request.getParameter("date")));

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setType(Lesson.Type.getById(typeId));
        lesson.setOrder(Lesson.Order.getByNumber(number));
        lesson.setDate(date);

        try {
            Course course = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByGroupAndDiscipline(groupId, disciplineId);
            lesson.setCourse(course);
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().update(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("lessons?group="+groupId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}