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

@WebServlet(name = "DisciplineListServlet", urlPatterns = "/disciplines")
public class DisciplineListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Discipline> disciplines = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        try {
            disciplines = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getAll();
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("disciplines", disciplines);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("disciplines.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
