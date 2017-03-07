package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TeacherServlet", urlPatterns = "/teacher")
public class TeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courses = new ArrayList<>();
        Integer teacherId = Integer.valueOf(request.getParameter("id"));
        try {
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByTeacher(teacherId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("teacher.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
