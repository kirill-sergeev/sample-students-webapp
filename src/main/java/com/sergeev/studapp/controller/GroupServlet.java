package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;
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
import java.util.Map;

@WebServlet(name = "GroupServlet", urlPatterns = {"/group", "/group/*"})
public class GroupServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GroupServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        String title;
        String id;

        if (path.matches("^/group/?")) {

            if ("create".equals(action)) {
                title = request.getParameter("title");
                try {
                    GroupService.create(title);
                } catch (ApplicationException e) {
                    LOG.info("Group cannot be created.");
                    response.sendRedirect("/group/new");
                    return;
                }
                LOG.info("Group '{}' successfully created.", title);
                response.sendRedirect("group");
                return;

            } else if ("update".equals(action)) {
                id = request.getParameter("id");
                title = request.getParameter("title");
                try {
                    GroupService.update(title, id);
                } catch (ApplicationException e) {
                    LOG.info("Group cannot be updated.");
                    response.sendRedirect("/group/" + id + "/change");
                    return;
                }
                LOG.info("Group '{}' successfully updated.", title);
                response.sendRedirect("/group");
                return;

            } else if ("delete".equals(action)) {
                id = request.getParameter("id");
                try {
                    GroupService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Group cannot be deleted, because group doesn't exist.");
                    response.sendRedirect("/group");
                    return;
                }
                LOG.info("Group successfully deleted.");
                response.sendRedirect("/group");
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        Group group;
        List<User> students;
        List<Course> courses;
        List<Lesson> lessons;
        Map<Group, Integer> groupsStudents;

        if (path.matches("^/group/?")) {
            courses = CourseService.readAll();
            groupsStudents = GroupService.studentsCount();
            request.setAttribute("courses", courses);
            request.setAttribute("groupsStudents", groupsStudents);
            request.getRequestDispatcher("/groups.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/group/\\p{Nd}+/?")) {
            String id = path.split("/")[2];
            try {
                group = GroupService.read(id);
                lessons = LessonService.readAll(id);
                courses = CourseService.readByGroup(id);
                students = UserService.readByGroup(id);
            } catch (ApplicationException e) {
                LOG.info("Group not found.");
                response.sendRedirect("/group");
                return;
            }
            request.setAttribute("group", group);
            request.setAttribute("lessons", lessons);
            request.setAttribute("students", students);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/group.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/group/new/?")) {
            request.getRequestDispatcher("/add-group.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/group/\\p{Nd}+/change/?")) {
            String id = path.split("/")[2];
            try {
                group = GroupService.read(id);
            } catch (ApplicationException e) {
                LOG.info("Group cannot be updated, because group doesn't exist.");
                response.sendRedirect("/group");
                return;
            }
            request.setAttribute("group", group);
            request.getRequestDispatcher("/change-group.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}
