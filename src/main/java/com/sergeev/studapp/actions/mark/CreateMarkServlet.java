package com.sergeev.studapp.actions.mark;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateMarkServlet", urlPatterns = "/create-mark")
public class CreateMarkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer lessonId = Integer.valueOf(request.getParameter("lesson"));
        Integer studentId = Integer.valueOf(request.getParameter("student"));
        Integer value = Integer.valueOf(request.getParameter("value"));

        Mark mark = new Mark();
        mark.setValue(value);

        try {
            Lesson lesson = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getByPK(lessonId);
            Student student = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getByPK(studentId);
            mark.setLesson(lesson);
            mark.setStudent(student);
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().persist(mark);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/lesson?id="+lessonId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
