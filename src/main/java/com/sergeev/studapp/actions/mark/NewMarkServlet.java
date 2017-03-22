package com.sergeev.studapp.actions.mark;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewMarkServlet", urlPatterns = "/new-mark")
public class NewMarkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonId = request.getParameter("lesson");
        String groupId = request.getParameter("group");

        Lesson lesson = new Lesson();
        List<User> students = new ArrayList<>();

        try {
            lesson = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getById(lessonId);
            students = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao().getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("lesson", lesson);
        request.setAttribute("students", students);
        request.getRequestDispatcher("new-mark.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
