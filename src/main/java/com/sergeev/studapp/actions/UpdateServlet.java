package com.sergeev.studapp.actions;

import com.sergeev.studapp.service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateServlet", urlPatterns = {"/update-course", "/update-discipline", "/update-group", "/update-lesson", "/update-student", "/update-teacher"})
public class UpdateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");

        String date;
        String order;
        String title;
        String firstName;
        String lastName;
        String disciplineId;
        String groupId;
        String teacherId;
        String typeId;

        switch (path) {
            case "/update-course":
                disciplineId = request.getParameter("discipline");
                groupId = request.getParameter("group");
                teacherId = request.getParameter("teacher");

                CourseService.update(disciplineId, groupId, teacherId, id);
                response.sendRedirect("group?id=" + groupId);
                break;
            case "/update-discipline":
                title = request.getParameter("title");

                DisciplineService.update(title, id);
                response.sendRedirect("disciplines");
                break;
            case "/update-group":
                title = request.getParameter("title");

                GroupService.update(title, id);
                response.sendRedirect("groups");
                break;
            case "/update-lesson":
                groupId = request.getParameter("group");
                disciplineId = request.getParameter("discipline");
                typeId = request.getParameter("type");
                order = request.getParameter("number");
                date = request.getParameter("date");

                LessonService.update(groupId, disciplineId, typeId, order, date, id);
                response.sendRedirect("lessons?group=" + groupId);
                break;
            case "/update-student":
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");
                groupId = request.getParameter("group");

                UserService.updateStudent(firstName, lastName, groupId, id);
                response.sendRedirect("students");
                break;
            case "/update-teacher":
                firstName = request.getParameter("first-name");
                lastName = request.getParameter("last-name");

                UserService.updateTeacher(firstName, lastName, id);
                response.sendRedirect("teachers");
                break;
            default:
                response.sendRedirect("");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
