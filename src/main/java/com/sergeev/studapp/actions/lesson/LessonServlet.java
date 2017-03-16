package com.sergeev.studapp.actions.lesson;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet(name = "LessonServlet", urlPatterns = "/lesson")
public class LessonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer lessonId = Integer.valueOf(request.getParameter("id"));

        Lesson lesson = new Lesson();
        List<Mark> marks = new ArrayList<>();

        try {
            lesson = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getById(lessonId);
            marks = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().getByLesson(lessonId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        Date dateNow = new Date(Calendar.getInstance().getTimeInMillis());

        request.setAttribute("lesson", lesson);
        request.setAttribute("dateNow", dateNow);
        request.setAttribute("marks", marks);
        request.getRequestDispatcher("lesson.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
