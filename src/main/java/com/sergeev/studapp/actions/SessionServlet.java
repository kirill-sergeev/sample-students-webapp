package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.AccountService;
import com.sergeev.studapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SessionServlet", urlPatterns = {"/login", "/logout"})
public class SessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpSession session = request.getSession(true);

        Cookie loginCookie = null;
        User user = null;
        String login;
        String password;
        String token;

        switch (path) {
            case "/login":

                if (session.getAttribute("user") != null) {
                    response.sendRedirect("/");
                    return;
                }

                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("MY_SESSION_COOKIE")) {
                        loginCookie = cookie;
                        break;
                    }
                }

                if (loginCookie != null) {
                    user = UserService.readByAccount(AccountService.readByToken(loginCookie.getValue()));
                }

                if (user == null) {
                    login = request.getParameter("login").toLowerCase();
                    password = request.getParameter("password");
                    user = UserService.readByAccount(login, password);

                    if (user == null) {
                        response.sendRedirect("login.jsp");
                        return;
                    }

                    token = UUID.randomUUID().toString();
                    user.getAccount().setToken(token);
                    AccountService.update(user);

                    loginCookie = new Cookie("MY_SESSION_COOKIE", token);
                    loginCookie.setPath(request.getContextPath());
                    loginCookie.setMaxAge(60);

                    response.addCookie(loginCookie);
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
                loginCookie = new Cookie("MY_SESSION_COOKIE", "");
                loginCookie.setPath(request.getContextPath());
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);

                session.invalidate();
                response.sendRedirect("/login.jsp");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
