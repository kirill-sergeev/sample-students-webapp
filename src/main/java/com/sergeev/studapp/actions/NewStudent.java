package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.model.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "NewStudent", urlPatterns = "/new-student")
public class NewStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        GroupDao gd = pgFactory.getGroupDao();
        ArrayList<Group> gr = new ArrayList<>();

        try {
            gr = (ArrayList<Group>) gd.getAll();
        } catch (PersistException e) {
            e.printStackTrace();
        }

        request.setAttribute("groups", gr);
        request.getRequestDispatcher("new-student.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
