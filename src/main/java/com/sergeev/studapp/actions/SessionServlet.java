package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SessionServlet", urlPatterns = {"/login", "/logout"})
public class SessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpSession session = request.getSession(true);

        switch (path) {
            case "/login":
                String login = request.getParameter("login").toLowerCase();
                String password = request.getParameter("password");
                User user = UserService.logIn(login, password);
                if (user == null){
                    response.sendRedirect("/");
                }
                session.setAttribute("user", user);
                if (user.getType() == User.Role.STUDENT) {
                    response.sendRedirect("/students");
                    return;
                }
                if (user.getType() == User.Role.TEACHER) {
                    response.sendRedirect("/teachers");
                    return;
                }
                break;

            case "/logout":
                session.invalidate();
                response.sendRedirect("/");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
