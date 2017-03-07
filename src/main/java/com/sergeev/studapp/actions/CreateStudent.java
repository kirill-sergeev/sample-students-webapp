package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateStudent", urlPatterns = "/create-student")
public class CreateStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String group = request.getParameter("group");
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao sd = pgFactory.getStudentDao();
        GroupDao grd = pgFactory.getGroupDao();
        Student st = new Student();
        Group gr = new Group();
        st.setFirstName(firstName);
        st.setLastName(lastName);

        try {
            gr = grd.getByPK(Integer.valueOf(group));
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        st.setGroup(gr);

        try {
            sd.persist(st);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
