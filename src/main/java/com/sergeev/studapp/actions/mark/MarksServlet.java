package com.sergeev.studapp.actions.mark;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Mark;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MarksServlet", urlPatterns = "/marksa")
public class MarksServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("student");
        String disciplineId = request.getParameter("discipline");

        User student = new User();
        Discipline discipline = new Discipline();
        List<Mark> marks = new ArrayList<>();

        try {
            student = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao().getById(studentId);
            discipline = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getById(disciplineId);
            marks = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().getByStudentAndDiscipline(studentId, disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("student", student);
        request.setAttribute("discipline", discipline);
        request.setAttribute("marks", marks);
        request.getRequestDispatcher("marks.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
