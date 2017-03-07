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
import java.util.ArrayList;

@WebServlet(name = "SearchStudent", urlPatterns = "/search-student")
public class SearchStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao sd = pgFactory.getStudentDao();
        ArrayList<Student> st = new ArrayList<>();

        try {
            st = (ArrayList<Student>) sd.getByName(name);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("students", st);
        request.getRequestDispatcher("students.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
