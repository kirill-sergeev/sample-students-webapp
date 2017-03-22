package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search-student", "/search-teacher"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String name = request.getParameter("name").toLowerCase();

        if (name.length() < 2) {
            response.sendRedirect("");
        }

        switch (path) {
            case "/search-student":
                List<User> students = UserService.find(User.AccountType.STUDENT, name);
                request.setAttribute("students", students);
                request.getRequestDispatcher("students.jsp").forward(request, response);
                break;
            case "/search-teacher":
                List<User> teachers = UserService.find(User.AccountType.TEACHER, name);
                request.setAttribute("teachers", teachers);
                request.getRequestDispatcher("teachers.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
