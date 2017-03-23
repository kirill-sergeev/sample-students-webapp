package com.sergeev.studapp.actions;

import com.sergeev.studapp.service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateServlet", urlPatterns = {"/create-course", "/create-discipline", "/create-group", "/create-lesson", "/create-mark", "/create-student", "/create-teacher"})
public class CreateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        String date;
        String order;
        String title;
        String firstName;
        String lastName;
        String disciplineId;
        String groupId;
        String teacherId;
        String typeId;
        String lessonId;
        String studentId;

        switch (path) {
            case "/create-course":
                disciplineId = request.getParameter("discipline");
                groupId = request.getParameter("group");
                teacherId = request.getParameter("teacher");

                CourseService.create(disciplineId, groupId, teacherId);
                response.sendRedirect("group?id=" + groupId);
                break;
            case "/create-discipline":
                title = request.getParameter("title");

                DisciplineService.create(title);
                response.sendRedirect("disciplines");
                break;
            case "/create-group":
                title = request.getParameter("title");

                GroupService.create(title);
                response.sendRedirect("groups");
                break;
            case "/create-lesson":
                groupId = request.getParameter("group");
                disciplineId = request.getParameter("discipline");
                typeId = request.getParameter("type");
                order = request.getParameter("number");
                date = request.getParameter("date");

                LessonService.create(groupId, disciplineId, typeId, order, date);
                response.sendRedirect("lessons?group=" + groupId);
                break;
            case "/create-mark":
                lessonId = request.getParameter("lesson");
                studentId = request.getParameter("student");
                String value = (request.getParameter("value"));

                MarkService.create(lessonId, studentId, value);
                response.sendRedirect("lesson?id=" + lessonId);
                break;
            case "/create-student":
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                groupId = request.getParameter("group");

                UserService.createStudent(firstName, lastName, groupId);
                response.sendRedirect("students");
                break;
            case "/create-teacher":
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");

                UserService.createTeacher(firstName, lastName);
                response.sendRedirect("students");
                break;
            default:
                response.sendRedirect("");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
