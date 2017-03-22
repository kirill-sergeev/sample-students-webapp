package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveServlet", urlPatterns = {"/remove-course", "/remove-discipline", "/remove-group", "/remove-lesson", "/remove-mark", "/remove-student", "/remove-teacher"})
public class RemoveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String id = request.getParameter("id");
        DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);

        String groupId;
        User.AccountType type;

        try {
            switch (path) {
                case "/remove-course":
                    groupId = daoFactory.getCourseDao().getById(id).getGroup().getId();
                    daoFactory.getUserDao().delete(id);
                    response.sendRedirect("group?id=" + groupId);
                    break;
                case "/remove-discipline":
                    daoFactory.getDisciplineDao().delete(id);
                    response.sendRedirect("disciplines");
                    break;
                case "/remove-group":
                    daoFactory.getGroupDao().delete(id);
                    response.sendRedirect("groups");
                    break;
                case "/remove-lesson":
                    groupId = daoFactory.getLessonDao().getById(id).getCourse().getGroup().getId();
                    daoFactory.getLessonDao().delete(id);
                    response.sendRedirect("lessons?group=" + groupId);
                    break;
                case "/remove-mark":
                    daoFactory.getMarkDao().delete(id);
                    response.sendRedirect("");
                    break;
                case "/remove-student":
                case "/remove-teacher":
                    type = daoFactory.getUserDao().getById(id).getType();
                    daoFactory.getUserDao().delete(id);
                    if (type == User.AccountType.STUDENT) {
                        response.sendRedirect("students");
                    } else {
                        response.sendRedirect("teachers");
                    }
                    break;
                default:
                    response.sendRedirect("");
            }
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
