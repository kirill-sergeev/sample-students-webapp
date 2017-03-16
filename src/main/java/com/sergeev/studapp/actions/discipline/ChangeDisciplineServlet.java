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

@WebServlet(name = "ChangeDisciplineServlet", urlPatterns = "/change-discipline")
public class ChangeDisciplineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer disciplineId = Integer.valueOf(request.getParameter("id"));

        Discipline discipline = new Discipline();

        try {
            discipline = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getById(disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("discipline", discipline);
        request.getRequestDispatcher("change-discipline.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
