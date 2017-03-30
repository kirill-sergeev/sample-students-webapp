package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.*;
import com.sergeev.studapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AddServlet", urlPatterns = {"/add-course", "/add-discipline", "/add-group", "/add-lesson", "/add-mark", "/add-student", "/add-teacher"})
public class AddServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AddServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        String groupId;
        String lessonId;
        Group group;
        Lesson lesson;
        List<Course> courses;
        List<Discipline> disciplines;
        List<Group> groups;
        List<User> students;
        List<User> teachers;
        Lesson.Type[] types;
        Lesson.Order[] orders;

        switch (path) {
            case "/add-course":
                disciplines = DisciplineService.readAll();
                groups = GroupService.readAll();
                teachers = UserService.readAll(User.Role.TEACHER);

                request.setAttribute("disciplines", disciplines);
                request.setAttribute("groups", groups);
                request.setAttribute("teachers", teachers);
                break;
            case "/add-discipline":
            case "/add-group":
                break;
            case "/add-lesson":
                groupId = request.getParameter("group");

                types = Lesson.Type.values();
                orders = Lesson.Order.values();
                group = GroupService.read(groupId);
                courses = CourseService.readByGroup(groupId);

                request.setAttribute("types", types);
                request.setAttribute("orders", orders);
                request.setAttribute("group", group);
                request.setAttribute("courses", courses);
                break;
            case "/add-mark":
                groupId = request.getParameter("group");
                lessonId = request.getParameter("lesson");

                lesson = LessonService.read(lessonId);
                students = UserService.readByGroup(groupId);

                request.setAttribute("lesson", lesson);
                request.setAttribute("students", students);
                break;
            case "/add-student":
                groups = GroupService.readAll();

                request.setAttribute("groups", groups);
                break;
            case "/add-teacher":
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
