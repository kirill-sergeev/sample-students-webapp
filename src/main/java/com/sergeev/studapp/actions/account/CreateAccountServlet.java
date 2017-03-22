package com.sergeev.studapp.actions.account;

import com.sergeev.studapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateAccountServlet", urlPatterns = {"/create-student", "/create-teacher"})
public class CreateAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String groupId = request.getParameter("group");

        switch (path) {
            case "/create-student":
                UserService.createStudent(firstName, lastName, groupId);
                response.sendRedirect("students");
                break;
            case "/create-teacher":
                UserService.createTeacher(firstName, lastName);
                response.sendRedirect("teachers");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}