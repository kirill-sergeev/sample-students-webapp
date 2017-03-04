package com.sergeev.studapp.actions;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateStudent", urlPatterns = "/update-student")
public class UpdateStudent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("student");
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String groupId = request.getParameter("group");

        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao sd = pgFactory.getStudentDao();
        GroupDao grd = pgFactory.getGroupDao();
        Student st = new Student();
        Group gr = null;

        try {
            gr = grd.getByPK(Integer.valueOf(groupId));
        } catch (PersistException e) {
            e.printStackTrace();
        }

        st.setId(Integer.valueOf(studentId));
        st.setFirstName(firstName);
        st.setLastName(lastName);
        st.setGroup(gr);

        try {
            sd.update(st);
        } catch (PersistException e) {
            e.printStackTrace();
        }
        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
