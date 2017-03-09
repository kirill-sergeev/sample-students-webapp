package com.sergeev.studapp.actions.discipline;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateDisciplineServlet", urlPatterns = "/create-discipline")
public class CreateDisciplineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");

        Discipline discipline = new Discipline();
        discipline.setTitle(title);

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().persist(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/disciplines");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}