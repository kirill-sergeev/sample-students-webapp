package com.sergeev.studapp.actions.lesson;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeLessonServlet", urlPatterns = "/change-lesson")
public class ChangeLessonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String LessonId = request.getParameter("id");

        Lesson lesson = new Lesson();
        Lesson.Type[] types = Lesson.Type.values();
        Lesson.Order[] orders = Lesson.Order.values();

        try {
            lesson = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getById(LessonId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("lesson", lesson);
        request.setAttribute("types", types);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("change-lesson.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
