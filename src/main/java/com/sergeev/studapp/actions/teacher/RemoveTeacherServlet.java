package com.sergeev.studapp.actions.teacher;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveTeacherServlet", urlPatterns = "/remove-teacher")
public class RemoveTeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer teacherId = Integer.valueOf(request.getParameter("id"));

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getTeacherDao().delete(teacherId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("teachers");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
