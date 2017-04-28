package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;
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

@WebServlet(name = "MarkServlet", urlPatterns = {"/mark", "/mark/*"})
public class MarkServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MarkServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        Integer id;
        Integer lessonId;
        Integer studentId;

        if (path.matches("^/mark/?")) {
            if ("create".equals(action)) {
                lessonId = Integer.valueOf(request.getParameter("lesson"));
                studentId = Integer.valueOf(request.getParameter("student"));
                Integer value = Integer.valueOf((request.getParameter("value")));
                try {
                    MarkService.create(lessonId, studentId, value);
                } catch (ApplicationException e) {
                    LOG.info("Mark cannot be created.");
                    response.sendRedirect("/lesson/" + lessonId);
                    return;
                }
                LOG.info("Mark successfully created.");
                response.sendRedirect("/lesson/" + lessonId);
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    lessonId = MarkService.read(id).getLesson().getId();
                    MarkService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Mark cannot be deleted, because mark doesn't exist.");
                    response.sendRedirect("/");
                    return;
                }
                LOG.info("Mark successfully deleted.");
                response.sendRedirect("/lesson/" + lessonId);
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        Integer disciplineId;
        Integer groupId;
        Integer studentId;
        Discipline discipline;
        User student;
        Integer lessonId;
        Lesson lesson;
        List<Mark> marks;
        List<User> students;

        if (path.matches("^/mark/student/[^/]+/discipline/[^/]+/?")) {
            studentId = Integer.valueOf(path.split("/")[3]);
            disciplineId = Integer.valueOf(path.split("/")[5]);
            try {
                marks = MarkService.readByDisciplineAndStudent(disciplineId, studentId);
                student = UserService.read(studentId);
                discipline = DisciplineService.read(disciplineId);
            } catch (ApplicationException e) {
                LOG.info("Mark not found.");
                response.sendRedirect("/mark");
                return;
            }
            request.setAttribute("student", student);
            request.setAttribute("discipline", discipline);
            request.setAttribute("marks", marks);
            request.getRequestDispatcher("/marks.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/mark/new/group/[^/]+/lesson/[^/]+/?")) {
            groupId = Integer.valueOf(path.split("/")[4]);
            lessonId = Integer.valueOf(path.split("/")[6]);

            try {
                lesson = LessonService.read(lessonId);
                students = UserService.readByGroup(groupId);
            } catch (ApplicationException e) {
                LOG.info("Mark cannot be created.");
                response.sendRedirect("/");
                return;
            }

            request.setAttribute("lesson", lesson);
            request.setAttribute("students", students);
            request.getRequestDispatcher("/add-mark.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}
