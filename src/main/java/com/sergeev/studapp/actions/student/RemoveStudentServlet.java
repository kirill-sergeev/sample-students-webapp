package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveStudentServlet", urlPatterns = "/remove-student")
public class RemoveStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer studentId = Integer.valueOf(request.getParameter("id"));

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().delete(studentId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/students");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
