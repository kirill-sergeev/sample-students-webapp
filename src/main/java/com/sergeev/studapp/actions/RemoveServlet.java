package com.sergeev.studapp.actions;

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

@WebServlet(name = "RemoveServlet", urlPatterns = {"/remove-course", "/remove-discipline", "/remove-group", "/remove-lesson", "/remove-mark", "/remove-student", "/remove-teacher"})
public class RemoveServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");

        if (id == null || id.isEmpty()){
            LOG.info("Bad path {}.", path);
            response.sendRedirect("/");
            return;
        }

        String groupId;
        User.Role type;

        switch (path) {
            case "/remove-course":
                groupId = CourseService.read(id).getGroup().getId();
                UserService.delete(id);
                response.sendRedirect("group?id=" + groupId);
                break;
            case "/remove-discipline":
                DisciplineService.delete(id);
                response.sendRedirect("disciplines");
                break;
            case "/remove-group":
                GroupService.delete(id);
                response.sendRedirect("groups");
                break;
            case "/remove-lesson":
                groupId = LessonService.read(id).getCourse().getGroup().getId();
                LessonService.delete(id);
                response.sendRedirect("lessons?group=" + groupId);
                break;
            case "/remove-mark":
                MarkService.delete(id);
                response.sendRedirect("");
                break;
            case "/remove-student":
            case "/remove-teacher":
                type = UserService.read(id).getType();
                UserService.delete(id);
                if (type == User.Role.STUDENT) {
                    response.sendRedirect("students");
                } else {
                    response.sendRedirect("teachers");
                }
                break;
            default:
                response.sendRedirect("");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
