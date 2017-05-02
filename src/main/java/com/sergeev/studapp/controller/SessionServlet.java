package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SessionServlet", urlPatterns = {"/login", "/logout"})
public class SessionServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SessionServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpSession session = request.getSession(true);

        Cookie loginCookie;
        User user = null;
        String login;
        String password;
        String token;
        String remember;

        switch (path) {
            case "/login":
                if (!request.getParameterMap().containsKey("login")) {
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                login = request.getParameter("login").toLowerCase();
                password = request.getParameter("password");
                try {
                    user = UserService.getByLogin(login, password);
                } catch (ApplicationException e) {
                    LOG.info("Bad login/password.", e);
                }

                if (user == null) {
                    request.setAttribute("login", login);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                remember = request.getParameter("remember");
                session.setAttribute("user", user);
                session.setAttribute("remember", remember);

                if ("remember".equals(remember)) {
                    token = UUID.randomUUID().toString();
                    try {
                        UserService.updateAccount(token, user.getId());
                    } catch (ApplicationException e) {
                        LOG.info("Cannot update token.");
                    }

                    loginCookie = new Cookie(LoginFilter.LOGIN_COOKIE, token);
                    loginCookie.setPath(request.getContextPath());
                    loginCookie.setMaxAge(24 * 60 * 60);
                    response.addCookie(loginCookie);
                }
                if (user.getRole() == User.Role.STUDENT) {
                    response.sendRedirect("/student/"+user.getId());
                    return;
                }else if (user.getRole() == User.Role.TEACHER) {
                    response.sendRedirect("/teacher/"+user.getId());
                    return;
                }else if (user.getRole() == User.Role.ADMIN) {
                    response.sendRedirect("/group/");
                    return;
                }
            case "/logout":
                loginCookie = new Cookie("MY_SESSION_COOKIE", "");
                loginCookie.setPath(request.getContextPath());
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);

                session.invalidate();
                response.sendRedirect("/login");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}