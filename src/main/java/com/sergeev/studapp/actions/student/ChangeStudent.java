package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ChangeStudent", urlPatterns = "/change-student")
public class ChangeStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("id");

        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao sd = pgFactory.getStudentDao();
        GroupDao gd = pgFactory.getGroupDao();
        ArrayList<Group> grl = new ArrayList<>();
        Student student = new Student();
        try {
            grl = (ArrayList<Group>) gd.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        try {
            student = sd.getByPK(Integer.valueOf(studentId));
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("groups", grl);
        request.setAttribute("id", student.getId());
        request.setAttribute("firstName", student.getFirstName());
        request.setAttribute("lastName", student.getLastName());
        request.setAttribute("group", student.getGroup().getId());
        request.getRequestDispatcher("change-student.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
