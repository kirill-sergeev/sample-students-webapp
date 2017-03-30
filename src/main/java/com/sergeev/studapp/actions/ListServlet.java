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
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ListServlet", urlPatterns = {"/disciplines", "/groups", "/lessons", "/marks", "/students", "/teachers"})
public class ListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ListServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        String disciplineId;
        String groupId;
        String studentId;
        Date dateNow;
        Discipline discipline;
        Group group;
        User student;
        List<Discipline> disciplines;
        List<Course> courses;
        List<Lesson> lessons;
        List<Mark> marks;
        List<User> students;
        List<User> teachers;
        Map<Group, Integer> groupsStudents;

        switch (path) {
            case "/disciplines":
                disciplines = DisciplineService.readAll();
                courses = CourseService.readAll();

                request.setAttribute("disciplines", disciplines);
                request.setAttribute("courses", courses);
                break;
            case "/groups":
                groupsStudents = GroupService.studentsCount();
                request.setAttribute("groupsStudents", groupsStudents);
                break;
            case "/lessons":
                groupId = request.getParameter("group");

                group = GroupService.read(groupId);
                lessons = LessonService.readAll(groupId);
                dateNow = new Date(Calendar.getInstance().getTimeInMillis());

                request.setAttribute("group", group);
                request.setAttribute("dateNow", dateNow);
                request.setAttribute("lessons", lessons);
                break;
            case "/marks":
                studentId = request.getParameter("student");
                disciplineId = request.getParameter("discipline");

                student = UserService.read(studentId);
                discipline = DisciplineService.read(disciplineId);
                marks = MarkService.readByDisciplineAndStudent(disciplineId, studentId);

                request.setAttribute("student", student);
                request.setAttribute("discipline", discipline);
                request.setAttribute("marks", marks);
                break;
            case "/students":
                students = UserService.readAll(User.Role.STUDENT);

                request.setAttribute("students", students);
                break;
            case "/teachers":
                teachers = UserService.readAll(User.Role.TEACHER);
                courses = CourseService.readAll();

                request.setAttribute("courses", courses);
                request.setAttribute("teachers", teachers);
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
