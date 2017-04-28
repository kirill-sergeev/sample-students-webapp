package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.CourseService;
import com.sergeev.studapp.service.DisciplineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DisciplineServlet", urlPatterns = {"/discipline", "/discipline/*"})
public class DisciplineServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplineServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        String title;
        Integer id;

        if (path.matches("^/discipline/?")) {

            if ("create".equals(action)) {
                title = request.getParameter("title");
                try {
                    DisciplineService.create(title);
                } catch (ApplicationException e) {
                    LOG.info("Discipline cannot be created.");
                    response.sendRedirect("/discipline/new");
                    return;
                }
                LOG.info("Discipline '{}' successfully created.", title);
                response.sendRedirect("discipline");
                return;

            } else if ("update".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                title = request.getParameter("title");
                try {
                    DisciplineService.update(title, id);
                } catch (ApplicationException e) {
                    LOG.info("Discipline cannot be updated.");
                    response.sendRedirect("/discipline/" + id + "/change");
                    return;
                }
                LOG.info("Discipline '{}' successfully updated.", title);
                response.sendRedirect("/discipline");
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    DisciplineService.delete(id);
                } catch (ApplicationException e) {
                    LOG.info("Discipline cannot be deleted, because discipline doesn't exist.");
                    response.sendRedirect("/discipline");
                    return;
                }
                LOG.info("Discipline successfully deleted.");
                response.sendRedirect("/discipline");
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        Discipline discipline;
        List<Course> courses;
        List<Discipline> disciplines;

        if (path.matches("^/discipline/?")) {
            disciplines = DisciplineService.readAll();
            courses = CourseService.readAll();

            request.setAttribute("disciplines", disciplines);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/disciplines.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/discipline/new/?")) {
            request.getRequestDispatcher("/add-discipline.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/discipline/[^/]+/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                discipline = DisciplineService.read(id);
                courses = CourseService.readByDiscipline(id);
            } catch (ApplicationException e) {
                LOG.info("Discipline not found.");
                response.sendRedirect("/discipline");
                return;
            }
            request.setAttribute("discipline", discipline);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/discipline.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/discipline/[^/]+/change/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                discipline = DisciplineService.read(id);
            } catch (ApplicationException e) {
                LOG.info("Discipline cannot be updated, because discipline doesn't exist.");
                response.sendRedirect("/discipline");
                return;
            }
            request.setAttribute("discipline", discipline);
            request.getRequestDispatcher("/change-discipline.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}