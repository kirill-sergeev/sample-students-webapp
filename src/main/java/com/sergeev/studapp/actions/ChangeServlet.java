package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.*;
import com.sergeev.studapp.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ChangeServlet", urlPatterns = {"/change-course", "/change-discipline", "/change-group", "/change-lesson", "/change-student", "/change-teacher"})
public class ChangeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");

        Course course;
        Group group;
        Discipline discipline;
        Lesson lesson;
        User student;
        User teacher;
        List<Discipline> disciplines;
        List<Group> groups;
        List<User> teachers;
        Lesson.Type[] types;
        Lesson.Order[] orders;

        switch (path) {
            case "/change-course":
                course = CourseService.read(id);
                disciplines = DisciplineService.readAll();
                groups = GroupService.readAll();
                teachers = UserService.readAll(User.Role.TEACHER);

                request.setAttribute("course", course);
                request.setAttribute("disciplines", disciplines);
                request.setAttribute("groups", groups);
                request.setAttribute("teachers", teachers);
                break;
            case "/change-discipline":
                discipline = DisciplineService.read(id);

                request.setAttribute("discipline", discipline);
                break;
            case "/change-group":
                group = GroupService.read(id);

                request.setAttribute("group", group);
                break;
            case "/change-lesson":
                lesson = LessonService.read(id);
                types = Lesson.Type.values();
                orders = Lesson.Order.values();

                request.setAttribute("lesson", lesson);
                request.setAttribute("types", types);
                request.setAttribute("orders", orders);
                break;
            case "/change-student":
                student = UserService.read(id);
                groups = GroupService.readAll();

                request.setAttribute("student", student);
                request.setAttribute("groups", groups);
                break;
            case "/change-teacher":
                teacher = UserService.read(id);

                request.setAttribute("teacher", teacher);
                break;
            default:
                response.sendRedirect("");
        }

        RequestDispatcher view = request.getRequestDispatcher(path + ".jsp");
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
