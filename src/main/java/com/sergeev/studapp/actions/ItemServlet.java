package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.CourseService;
import com.sergeev.studapp.service.DisciplineService;
import com.sergeev.studapp.service.GroupService;
import com.sergeev.studapp.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ItemServlet", urlPatterns = {"/discipline", "/group", "/lesson", "/mark", "/student", "/teacher"})
public class ItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");

        Group group;
        List<User> students;
        List<Course> courses;
        Discipline discipline;


        switch (path) {
            case "/discipline":
                discipline = DisciplineService.find(id);
                courses = CourseService.readByDiscipline(id);

                request.setAttribute("discipline", discipline);
                request.setAttribute("courses", courses);
                break;
            case "/group":
                group = GroupService.find(id);
                courses = CourseService.readByGroup(id);
                students = UserService.readByGroup(id);

                request.setAttribute("group", group);
                request.setAttribute("students", students);
                request.setAttribute("courses", courses);
                break;
            case "/lesson":


                break;
            case "/mark":

                break;
            case "/student":

                break;
            case "/teacher":

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
