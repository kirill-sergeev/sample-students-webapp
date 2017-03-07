package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GroupServlet", urlPatterns = "/group")
public class GroupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        Integer groupId = Integer.valueOf(request.getParameter("id"));
        try {
            students = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getByGroup(groupId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("students", students);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("group.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
