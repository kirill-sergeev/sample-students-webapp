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
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ItemServlet", urlPatterns = {"/discipline", "/group", "/lesson", "/mark", "/student", "/teacher"})
public class ItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");

        Date dateNow;
        Discipline discipline;
        Group group;
        Lesson lesson;
        User student;
        User teacher;
        List<User> students;
        List<Course> courses;
        List<Mark> marks;
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        switch (path) {
            case "/discipline":
                discipline = DisciplineService.read(id);
                courses = CourseService.readByDiscipline(id);

                request.setAttribute("discipline", discipline);
                request.setAttribute("courses", courses);
                break;
            case "/group":
                group = GroupService.read(id);
                courses = CourseService.readByGroup(id);
                students = UserService.readByGroup(id);

                request.setAttribute("group", group);
                request.setAttribute("students", students);
                request.setAttribute("courses", courses);
                break;
            case "/lesson":
                lesson = LessonService.read(id);
                marks = MarkService.readByLesson(id);
                dateNow = new Date(Calendar.getInstance().getTimeInMillis());

                request.setAttribute("lesson", lesson);
                request.setAttribute("marks", marks);
                request.setAttribute("dateNow", dateNow);
                break;
            case "/student":
                student = UserService.read(id);
                courses = CourseService.readByDiscipline(student.getGroup().getId());

                for(Course course: courses){
                    double avgMark = MarkService.calculateAvgMark(id, course.getDiscipline().getId());
                    coursesMarks.put(course, avgMark);
                }

                request.setAttribute("coursesMarks", coursesMarks);
                request.setAttribute("student", student);
                break;
            case "/teacher":
                teacher = UserService.read(id);
                courses = CourseService.readByTeacher(id);

                request.setAttribute("teacher", teacher);
                request.setAttribute("courses", courses);
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
