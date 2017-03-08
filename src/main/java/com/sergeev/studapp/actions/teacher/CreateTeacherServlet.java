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

@WebServlet(name = "CreateTeacherServlet", urlPatterns = "/create-teacher")
public class CreateTeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");

        Teacher teacher = new Teacher();

        if (firstName.length() >= 2 && lastName.length() >= 2) {
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
        } else{
            response.sendRedirect("/new-teacher");
            return;
        }

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getTeacherDao().persist(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/teachers");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
