package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.AccountService;
import com.sergeev.studapp.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        final String path = req.getRequestURI().substring(req.getContextPath().length());

        Cookie loginCookie = null;
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("MY_SESSION_COOKIE")) {
                loginCookie = cookie;
                break;
            }
        }

        if (path.startsWith("/login")) {
            if (loginCookie != null) {
                resp.addCookie(loginCookie);
            }
            chain.doFilter(request, response);
            return;
        }

        if (req.getSession().getAttribute("user") == null) {
            if (loginCookie != null) {
                User user = UserService.readByAccount(AccountService.readByToken(loginCookie.getValue()));
                //datastore.updateTokenLastActivity(loginCookie.getValue());
                req.getSession().setAttribute("user", user);
            }
        }

        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
        } else {
            resp.addCookie(loginCookie);
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
