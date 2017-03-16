package com.sergeev.studapp.actions.group;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateGroupServlet", urlPatterns = "/update-group")
public class UpdateGroupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupId = request.getParameter("id");
        String title = request.getParameter("title");

        Group group = new Group();
        group.setId(groupId);
        group.setTitle(title);

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().update(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("groups");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
