package com.sergeev.studapp.actions.course;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Teacher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ChangeCourseServlet", urlPatterns = "/change-course")
public class ChangeCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer courseId = Integer.valueOf(request.getParameter("id"));

        Course course = new Course();
        List<Discipline> disciplines = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        try {
            disciplines = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getAll();
            groups = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getAll();
            teachers = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getTeacherDao().getAll();
            course = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByPK(courseId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("course", course);
        request.setAttribute("disciplines", disciplines);
        request.setAttribute("groups", groups);
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("change-course.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}