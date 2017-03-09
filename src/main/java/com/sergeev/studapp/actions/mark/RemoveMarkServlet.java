package com.sergeev.studapp.actions.mark;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveMarkServlet", urlPatterns = "/remove-mark")
public class RemoveMarkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer markId = Integer.valueOf(request.getParameter("id"));

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().delete(markId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
