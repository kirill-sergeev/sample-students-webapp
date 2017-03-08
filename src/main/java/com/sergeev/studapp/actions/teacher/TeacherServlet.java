package com.sergeev.studapp.actions.teacher;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Teacher;

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
        Integer teacherId = Integer.valueOf(request.getParameter("id"));

        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();

        try {
            teacher = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getTeacherDao().getByPK(teacherId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByTeacher(teacherId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("teacher", teacher);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("teacher.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
