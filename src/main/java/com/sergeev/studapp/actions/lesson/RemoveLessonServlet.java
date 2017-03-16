package com.sergeev.studapp.actions.lesson;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveLessonServlet", urlPatterns = "/remove-lesson")
public class RemoveLessonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonId = request.getParameter("id");

        String groupId = null;

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().delete(lessonId);
            groupId = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getById(lessonId).getCourse().getGroup().getId();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        if (groupId == null) {
            response.sendRedirect("");
            return;
        }

        response.sendRedirect("lessons?group=" + groupId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}