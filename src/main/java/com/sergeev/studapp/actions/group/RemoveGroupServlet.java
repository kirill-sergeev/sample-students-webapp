package com.sergeev.studapp.actions.group;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveGroupServlet", urlPatterns = "/remove-group")
public class RemoveGroupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupId = request.getParameter("id");

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().delete(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("groups");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
