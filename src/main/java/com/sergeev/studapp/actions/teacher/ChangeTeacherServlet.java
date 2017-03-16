package com.sergeev.studapp.actions.teacher;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Teacher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeTeacherServlet", urlPatterns = "/change-teacher")
public class ChangeTeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacherId = request.getParameter("id");

        Teacher teacher = new Teacher();

        try {
            teacher = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getTeacherDao().getById(teacherId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("teacher", teacher);
        request.getRequestDispatcher("change-teacher.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
