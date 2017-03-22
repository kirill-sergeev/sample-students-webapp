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

@WebServlet(name = "ChangeServlet", urlPatterns = {"/change-course", "/change-discipline", "/change-group", "/change-lesson", "/change-student", "/change-teacher"})
public class ChangeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");
        DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);

        Course course;
        Group group;
        Discipline discipline;
        Lesson lesson;
        User student;
        User teacher;
        List<Discipline> disciplines;
        List<Group> groups;
        List<User> teachers;
        Lesson.LessonType[] types;
        Lesson.LessonOrder[] orders;

        try {
            switch (path) {
                case "/change-course":
                    course = daoFactory.getCourseDao().getById(id);
                    disciplines = daoFactory.getDisciplineDao().getAll();
                    groups = daoFactory.getGroupDao().getAll();
                    teachers = daoFactory.getUserDao().getAll(User.AccountType.TEACHER);

                    request.setAttribute("course", course);
                    request.setAttribute("disciplines", disciplines);
                    request.setAttribute("groups", groups);
                    request.setAttribute("teachers", teachers);
                    break;
                case "/change-discipline":
                    discipline = daoFactory.getDisciplineDao().getById(id);

                    request.setAttribute("discipline", discipline);
                    break;
                case "/change-group":
                    group = daoFactory.getGroupDao().getById(id);

                    request.setAttribute("group", group);
                    break;
                case "/change-lesson":
                    lesson = daoFactory.getLessonDao().getById(id);
                    types = Lesson.LessonType.values();
                    orders = Lesson.LessonOrder.values();

                    request.setAttribute("lesson", lesson);
                    request.setAttribute("types", types);
                    request.setAttribute("orders", orders);
                    break;
                case "/change-student":
                    student = daoFactory.getUserDao().getById(id);
                    groups = daoFactory.getGroupDao().getAll();

                    request.setAttribute("student", student);
                    request.setAttribute("groups", groups);
                    break;
                case "/change-teacher":
                    teacher = daoFactory.getUserDao().getById(id);

                    request.setAttribute("teacher", teacher);
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
