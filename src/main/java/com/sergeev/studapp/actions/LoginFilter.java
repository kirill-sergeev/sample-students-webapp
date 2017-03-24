//package com.sergeev.studapp.actions;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.UUID;
//
//@WebFilter(filterName = "LoginFilter")
//public class LoginFilter implements Filter {
//    public void destroy() {
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//
//        // Java 1.8 stream API used here
//        Cookie loginCookie = Arrays.stream(req.getCookies()).filter(c -> c.getName()
//                .equals("MY_SESSION_COOKIE")).findAny().orElse(null);
//
//        // if we don't have the user already in session, check our cookie MY_SESSION_COOKIE
//        if (req.getSession().getAttribute("currentUser") == null) {
//            // if the cookie is not present, add it
//            if (loginCookie == null) {
//                loginCookie = new Cookie("MY_SESSION_COOKIE", UUID.randomUUID().toString());
//                // Store that cookie only for our app. You can store it under "/",
//                // if you wish to cover all webapps on the server, but the same datastore
//                // needs to be available for all webapps.
//                loginCookie.setPath(req.getContextPath());
//                loginCookie.setMaxAge(24*60*60); // valid for one day, choose your value
//                resp.addCookie(loginCookie);
//            }
//            // if we have our cookie, check it
//            else {
//                String userId = datastore.getLoggedUserForToken(loginCookie.getValue());
//                // the datastore returned null, if it does not know the token, or
//                // if the token is expired
//                req.getSession().setAttribute("currentUser", userId);
//            }
//        }
//        else {
//            if (loginCookie != null)
//                datastore.updateTokenLastActivity(loginCookie.getValue());
//        }
//
//        // if we still don't have the userId, forward to login
//        if (req.getSession().getAttribute("currentUser") == null)
//            resp.sendRedirect("login.jsp");
//            // else return the requested resource
//        else
//            chain.doFilter(request, response);
//    }
//
//    public void init(FilterConfig config) throws ServletException {
//
//    }
//
//}
