package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateStudentServlet", urlPatterns = "/update-student")
public class UpdateStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("id");
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String groupId = request.getParameter("group");

        User student = new User();
        student.setId(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setType(User.AccountType.STUDENT);

        try {
            Group group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getById(groupId);
            student.setGroup(group);
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao().update(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("students");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
