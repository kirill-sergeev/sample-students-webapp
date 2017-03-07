package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteStudent", urlPatterns = "/delete-student")
public class DeleteStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("student");
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao sd = pgFactory.getStudentDao();
        Student student = new Student();
        student.setId(Integer.parseInt(studentId));

        try {
            sd.delete(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
