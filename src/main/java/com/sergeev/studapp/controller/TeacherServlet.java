package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.CourseService;
import com.sergeev.studapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TeacherServlet", urlPatterns = {"/teacher", "/teacher/*"})
public class TeacherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        Integer id;
        String firstName;
        String lastName;

        if (path.matches("^/teacher/?")) {

            if ("save".equals(action)) {
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                try {
                    UserService.addTeacher(firstName, lastName);
                } catch (ApplicationException e) {
                    LOG.info("Teacher cannot be created.");
                    response.sendRedirect("/teacher/new");
                    return;
                }
                LOG.info("Teacher '{}' successfully created.", firstName+" "+lastName);
                response.sendRedirect("teacher");
                return;

            } else if ("update".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                try {
                    UserService.updateTeacher(firstName, lastName, id);
                } catch (ApplicationException e) {
                    LOG.info("Teacher cannot be updated.");
                    e.printStackTrace();
                    response.sendRedirect("/teacher/" + id + "/change");
                    return;
                }
                LOG.info("Teacher '{}' successfully updated.", firstName+" "+lastName);
                response.sendRedirect("/teacher");
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    UserService.remove(id);
                } catch (ApplicationException e) {
                    LOG.info("Teacher cannot be deleted, because teacher doesn't exist.");
                    response.sendRedirect("/teacher");
                    return;
                }
                LOG.info("Teacher successfully deleted.");
                response.sendRedirect("/teacher");
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        User teacher;
        List<User> teachers;
        List<Course> courses;

        if (path.matches("^/teacher/?")) {
            teachers = UserService.getAll(User.Role.TEACHER);
            courses = CourseService.getAll();
            request.setAttribute("courses", courses);
            request.setAttribute("teachers", teachers);
            request.getRequestDispatcher("/teachers.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/teacher/new/?")) {
            request.getRequestDispatcher("/add-teacher.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/teacher/[^/]+/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                teacher = UserService.get(id);
                courses = CourseService.getByTeacher(id);
            } catch (ApplicationException e) {
                LOG.info("Teacher not found.");
                response.sendRedirect("/teacher");
                return;
            }
            request.setAttribute("teacher", teacher);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/teacher.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/teacher/[^/]+/change/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                teacher = UserService.get(id);
            } catch (ApplicationException e) {
                LOG.info("Teacher cannot be updated, because teacher doesn't exist.");
                response.sendRedirect("/teacher");
                return;
            }
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/change-teacher.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}
