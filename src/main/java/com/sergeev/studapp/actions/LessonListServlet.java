package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LessonListServlet", urlPatterns = "/lessons")
public class LessonListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer groupId = Integer.valueOf(request.getParameter("id"));
        List<Lesson> lessons = new ArrayList<>();
        Group group = new Group();

        try {
            group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getByPK(groupId);
            lessons = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao().getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("group", group);
        request.setAttribute("lessons", lessons);
        request.getRequestDispatcher("lessons.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
