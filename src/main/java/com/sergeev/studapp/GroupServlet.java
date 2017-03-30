package com.sergeev.studapp;

import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "GroupServlet", urlPatterns = {"/group","/group/*"})
public class GroupServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GroupServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");
        String title;
        System.out.println(path);
        if (path.matches("^/group/?")){
            if ("create".equals(action)){
                title = request.getParameter("title");
                try {
                    GroupService.create(title);
                } catch (ApplicationException e) {
                    LOG.info("Group cannot be created.");
                    response.sendRedirect("/group/new");
                    return;
                }
                LOG.info("Group '{}' successfully created.", title);
                response.sendRedirect("group");
                return;
            }

        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        Map<Group, Integer> groupsStudents;
        System.out.println(path);
        if (path.matches("^/group/new/?")) {
            request.getRequestDispatcher("/add-group.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/group/?")) {
            groupsStudents = GroupService.studentsCount();
            request.setAttribute("groupsStudents", groupsStudents);
            request.getRequestDispatcher("/groups.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("/");
    }
}
