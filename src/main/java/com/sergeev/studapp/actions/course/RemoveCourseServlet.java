package com.sergeev.studapp.actions.course;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveCourseServlet", urlPatterns = "/remove-course")
public class RemoveCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("id");
        String groupId = null;

        try {
            groupId = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getById(courseId).getGroup().getId();
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().delete(courseId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("group?id=" + groupId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}