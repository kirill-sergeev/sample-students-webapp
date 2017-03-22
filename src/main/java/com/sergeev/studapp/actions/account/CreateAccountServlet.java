package com.sergeev.studapp.actions.account;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateAccountServlet", urlPatterns = {"/create-student", "/create-teacher"})
public class CreateAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String groupId = request.getParameter("group");
        String typeId = request.getParameter("type");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(firstName.toLowerCase() + "_" + lastName.toLowerCase());
        user.setPassword("pass");
        user.setType(User.AccountType.getById(typeId));

        try {
            if (user.getType() == User.AccountType.STUDENT) {
                Group group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getById(groupId);
                user.setGroup(group);
            }
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao().persist(user);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        if (user.getType() == User.AccountType.STUDENT) {
            response.sendRedirect("students");
        } else {
            response.sendRedirect("teachers");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}