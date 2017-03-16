package com.sergeev.studapp.actions.discipline;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DisciplineServlet", urlPatterns = "/discipline")
public class DisciplineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String disciplineId = request.getParameter("id");

        Discipline discipline = new Discipline();
        List<Course> courses = new ArrayList<>();

        try {
            discipline = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getById(disciplineId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByDiscipline(disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("discipline", discipline);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("discipline.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
