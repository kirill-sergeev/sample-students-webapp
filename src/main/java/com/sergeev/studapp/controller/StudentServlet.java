package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.GroupService;
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
import java.util.Map;


@WebServlet(name = "StudentServlet", urlPatterns = {"/student", "/student/*"})
public class StudentServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(StudentServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        Integer id;
        String firstName;
        String lastName;
        Integer groupId;
        List<User> students;

        if (path.matches("^/student/?")) {

            if ("create".equals(action)) {
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                groupId = Integer.valueOf(request.getParameter("group"));
                try {
                    UserService.createStudent(firstName, lastName, groupId);
                } catch (ApplicationException e) {
                    LOG.info("Student cannot be created.");
                    response.sendRedirect("/student/new");
                    return;
                }
                LOG.info("Student '{}' successfully created.", firstName+" "+lastName);
                response.sendRedirect("student");
                return;

            } else if ("update".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                groupId = Integer.valueOf(request.getParameter("group"));
                try {
                    UserService.updateStudent(firstName, lastName, groupId, id);
                } catch (ApplicationException e) {
                    LOG.info("Student cannot be updated.");
                    e.printStackTrace();
                    response.sendRedirect("/student/" + id + "/change");
                    return;
                }
                LOG.info("Student '{}' successfully updated.", firstName+" "+lastName);
                response.sendRedirect("/student");
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    UserService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Student cannot be deleted, because student doesn't exist.");
                    response.sendRedirect("/student");
                    return;
                }
                LOG.info("Student successfully deleted.");
                response.sendRedirect("/student");
                return;
            }
        } else if ("search".equals(action)) {
            String name = request.getParameter("name").toLowerCase();
            try {
                students = UserService.find(User.Role.STUDENT, name);
            } catch (ApplicationException e) {
                LOG.info("Student not found.");
                response.sendRedirect("/student");
                return;
            }
            request.setAttribute("students", students);
            request.getRequestDispatcher("/students.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        User student;
        List<Group> groups;
        List<User> students;
        Map<Course, Double> coursesMarks;

        if (path.matches("^/student/?")) {
            students = UserService.readAll(User.Role.STUDENT);
            request.setAttribute("students", students);
            request.getRequestDispatcher("/students.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/student/new/?")) {
            groups = GroupService.readAll();
            request.setAttribute("groups", groups);
            request.getRequestDispatcher("/add-student.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/student/[^/]+/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                student = UserService.read(id);
                coursesMarks = UserService.studentAvgMarks(id);
            } catch (ApplicationException e) {
                LOG.info("Student not found.", e);
                response.sendRedirect("/student");
                return;
            }
            request.setAttribute("coursesMarks", coursesMarks);
            request.setAttribute("student", student);
            request.getRequestDispatcher("/student.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/student/[^/]+/change/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                student = UserService.read(id);
            } catch (ApplicationException e) {
                LOG.info("Student cannot be updated, because student doesn't exist.");
                response.sendRedirect("/student");
                return;
            }
            groups = GroupService.readAll();
            request.setAttribute("student", student);
            request.setAttribute("groups", groups);
            request.getRequestDispatcher("/change-student.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("/");
    }
}

