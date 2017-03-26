package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.AccountService;
import com.sergeev.studapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

import static com.sergeev.studapp.actions.LoginFilter.LOGIN_COOKIE;

@WebServlet(name = "SessionServlet", urlPatterns = {"/login", "/logout"})
public class SessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpSession session = request.getSession(true);

        Cookie loginCookie;
        User user;
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
                user = UserService.readByLogin(login, password);
                remember = request.getParameter("remember");

                if (user == null) {
                    request.setAttribute("login", login);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("user", user);
                session.setAttribute("remember", remember);

                if ("remember".equals(remember)) {
                    token = UUID.randomUUID().toString();
                    user.getAccount().setToken(token);
                    AccountService.update(user);

                    loginCookie = new Cookie(LOGIN_COOKIE, token);
                    loginCookie.setPath(request.getContextPath());
                    loginCookie.setMaxAge(24 * 60 * 60);
                    response.addCookie(loginCookie);
                }
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
                response.sendRedirect("/login");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
