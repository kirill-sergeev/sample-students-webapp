package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ChangeStudentServlet", urlPatterns = "/change-student")
public class ChangeStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("id");

        Student student = new Student();
        List<Group> groups = new ArrayList<>();

        try {
            student = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getById(studentId);
            groups = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("student", student);
        request.setAttribute("groups", groups);
        request.getRequestDispatcher("change-student.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
