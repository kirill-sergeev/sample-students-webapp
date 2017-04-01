package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;
import com.sergeev.studapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@WebServlet(name = "LessonServlet", urlPatterns = {"/lesson", "/lesson/*"})
public class LessonServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LessonServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        String id;
        String date;
        String order;
        String disciplineId;
        String groupId;
        String typeId;

        if (path.matches("^/lesson/?")) {

            if ("create".equals(action)) {
                groupId = request.getParameter("group");
                disciplineId = request.getParameter("discipline");
                typeId = request.getParameter("type");
                order = request.getParameter("number");
                date = request.getParameter("date");
                try {
                    LessonService.create(groupId, disciplineId, typeId, order, date);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be created.");
                    e.printStackTrace();
                    response.sendRedirect("/lesson/new/group/"+groupId);
                    return;
                }
                LOG.info("Lesson successfully created.");
                response.sendRedirect("lesson/group/"+groupId);
                return;

            } else if ("update".equals(action)) {
                id = request.getParameter("id");
                groupId = request.getParameter("group");
                disciplineId = request.getParameter("discipline");
                typeId = request.getParameter("type");
                order = request.getParameter("number");
                date = request.getParameter("date");
                try {
                    LessonService.update(groupId, disciplineId, typeId, order, date, id);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be updated.");
                    response.sendRedirect("/lesson/" + id + "/change");
                    return;
                }
                LOG.info("Lesson successfully updated.");
                response.sendRedirect("lesson/group/"+groupId);
                return;

            } else if ("delete".equals(action)) {
                id = request.getParameter("id");
                try {
                    groupId = LessonService.read(id).getCourse().getGroup().getId();
                    LessonService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be deleted, because lesson doesn't exist.");
                    response.sendRedirect("/");
                    return;
                }
                LOG.info("Lesson successfully deleted.");
                response.sendRedirect("lesson/group/"+groupId);
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        String groupId;
        Group group;
        Lesson lesson;
        List<Course> courses;
        List<Lesson> lessons;
        LocalDate dateNow = LocalDate.now(ZoneOffset.ofHours(2));
        Lesson.Type[] types;
        Lesson.Order[] orders;
        List<Mark> marks;

        if (path.matches("^/lesson/group/[^/]+/?")) {
            groupId= path.split("/")[3];
            try {
                group = GroupService.read(groupId);
                lessons = LessonService.readAll(groupId);
            } catch (ApplicationException e) {
                LOG.info("Lessons in group not found, because group doesn't exist.");
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("group", group);
            request.setAttribute("dateNow", dateNow);
            request.setAttribute("lessons", lessons);
            request.getRequestDispatcher("/lessons.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/lesson/new/group/[^/]+/?")) {
            groupId= path.split("/")[4];
            types = Lesson.Type.values();
            orders = Lesson.Order.values();
            try {
                group = GroupService.read(groupId);
                courses = CourseService.readByGroup(groupId);
            } catch (ApplicationException e) {
                LOG.info("Lessons cannot be created, because group doesn't exist.");
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("types", types);
            request.setAttribute("orders", orders);
            request.setAttribute("group", group);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/add-lesson.jsp").forward(request, response);
            return;
        }
        if (path.matches("^/lesson/[^/]+/?")) {
            String id = path.split("/")[2];
            try {
                lesson = LessonService.read(id);
                marks = MarkService.readByLesson(id);
            } catch (ApplicationException e) {
                LOG.info("Lesson not found.");
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("lesson", lesson);
            request.setAttribute("marks", marks);
            request.setAttribute("dateNow", dateNow);
            request.getRequestDispatcher("/lesson.jsp").forward(request, response);
            return;
        }


        if (path.matches("^/lesson/[^/]+/change/?")) {
            String id = path.split("/")[2];
            try {
                lesson = LessonService.read(id);
            } catch (ApplicationException e) {
                LOG.info("Lesson cannot be updated, because lesson doesn't exist.");
                response.sendRedirect("/lesson");
                return;
            }
            types = Lesson.Type.values();
            orders = Lesson.Order.values();
            request.setAttribute("lesson", lesson);
            request.setAttribute("types", types);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/change-lesson.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}