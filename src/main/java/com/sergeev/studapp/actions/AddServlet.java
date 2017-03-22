package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.*;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);

        List<Course> courses;
        List<Discipline> disciplines;
        List<Group> groups;
        List<User> students;
        List<User> teachers;
        Lesson.LessonType[] types;
        Lesson.LessonOrder[] orders;
        Group group;
        Lesson lesson;
        String groupId;
        String lessonId;

        try {
            switch (path) {
                case "/add-course":
                    disciplines = daoFactory.getDisciplineDao().getAll();
                    groups = daoFactory.getGroupDao().getAll();
                    teachers = daoFactory.getUserDao().getAll(User.AccountType.TEACHER);

                    request.setAttribute("disciplines", disciplines);
                    request.setAttribute("groups", groups);
                    request.setAttribute("teachers", teachers);
                    break;
                case "/add-discipline":
                    break;
                case "/add-group":
                    break;
                case "/add-lesson":
                    groupId = request.getParameter("group");

                    types = Lesson.LessonType.values();
                    orders = Lesson.LessonOrder.values();
                    group = daoFactory.getGroupDao().getById(groupId);
                    courses = daoFactory.getCourseDao().getByGroup(groupId);

                    request.setAttribute("types", types);
                    request.setAttribute("orders", orders);
                    request.setAttribute("group", group);
                    request.setAttribute("courses", courses);
                    break;
                case "/add-mark":
                    groupId = request.getParameter("group");
                    lessonId = request.getParameter("lesson");

                    lesson = daoFactory.getLessonDao().getById(lessonId);
                    students = daoFactory.getUserDao().getByGroup(groupId);

                    request.setAttribute("lesson", lesson);
                    request.setAttribute("students", students);
                    break;
                case "/add-student":
                    groups = daoFactory.getGroupDao().getAll();

                    request.setAttribute("groups", groups);
                    break;
                case "/add-teacher":

                    break;
                default:
                    response.sendRedirect("");
            }
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        RequestDispatcher view = request.getRequestDispatcher(path + ".jsp");
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
