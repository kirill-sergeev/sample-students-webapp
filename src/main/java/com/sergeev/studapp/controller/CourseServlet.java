package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CourseServlet", urlPatterns = {"/course", "/course/*"})
public class CourseServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CourseServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        Integer id;
        Integer disciplineId;
        Integer groupId;
        Integer teacherId;

        if (path.matches("^/course/?")) {

            if ("create".equals(action)) {
                disciplineId = Integer.valueOf(request.getParameter("discipline"));
                groupId = Integer.valueOf(request.getParameter("group"));
                teacherId = Integer.valueOf(request.getParameter("teacher"));

                try {
                    CourseService.create(disciplineId, groupId, teacherId);
                } catch (ApplicationException e) {
                    LOG.info("Course cannot be created.");
                    response.sendRedirect("/course/new");
                    return;
                }
                LOG.info("Course successfully created.");
                response.sendRedirect("/group/" + groupId);
                return;

            } else if ("update".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                disciplineId = Integer.valueOf(request.getParameter("discipline"));
                groupId = Integer.valueOf(request.getParameter("group"));
                teacherId = Integer.valueOf(request.getParameter("teacher"));
                try {
                    CourseService.update(disciplineId, groupId, teacherId, id);
                } catch (ApplicationException e) {
                    LOG.info("Course cannot be updated.");
                    response.sendRedirect("/course/" + id + "/change");
                    return;
                }
                LOG.info("Course successfully updated.");
                response.sendRedirect("/group/" + groupId);
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    groupId = CourseService.read(id).getGroup().getId();
                } catch (ApplicationException e) {
                    LOG.info("Course cannot be deleted, because course doesn't exist.");
                    response.sendRedirect("/");
                    return;
                }
                try {
                    CourseService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Course cannot be deleted, because course contains lessons.");
                    response.sendRedirect("/group/" + groupId);
                    return;
                }
                LOG.info("Course successfully deleted.");
                response.sendRedirect("/group/" + groupId);
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        Course course;
        List<Discipline> disciplines;
        List<Group> groups;
        List<User> teachers;

        if (path.matches("^/course/new/?")) {
            disciplines = DisciplineService.readAll();
            groups = GroupService.readAll();
            teachers = UserService.readAll(User.Role.TEACHER);

            request.setAttribute("disciplines", disciplines);
            request.setAttribute("groups", groups);
            request.setAttribute("teachers", teachers);
            request.getRequestDispatcher("/add-course.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/course/[^/]+/change/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                course = CourseService.read(id);
            } catch (ApplicationException e) {
                LOG.info("Course cannot be updated, because course doesn't exist.");
                response.sendRedirect("/course");
                return;
            }
            disciplines = DisciplineService.readAll();
            groups = GroupService.readAll();
            teachers = UserService.readAll(User.Role.TEACHER);

            request.setAttribute("course", course);
            request.setAttribute("disciplines", disciplines);
            request.setAttribute("groups", groups);
            request.setAttribute("teachers", teachers);
            request.getRequestDispatcher("/change-course.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}