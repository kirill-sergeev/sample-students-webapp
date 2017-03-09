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

@WebServlet(name = "UpdateDisciplineServlet", urlPatterns = "/update-discipline")
public class UpdateDisciplineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer disciplineId = Integer.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");

        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        discipline.setTitle(title);

        try {
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().update(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/disciplines");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
