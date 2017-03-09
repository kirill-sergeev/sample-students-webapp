package com.sergeev.studapp.actions.group;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GroupListServlet", urlPatterns = "/groups")
public class GroupListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Group> groups = new ArrayList<>();
        Map<Group, Integer> groupsStudents = new LinkedHashMap<>();

        try {
            groups = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getAll();

            int studentCount;
            for(Group group: groups) {
                 studentCount = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getByGroup(group.getId()).size();
                 groupsStudents.put(group, studentCount);
            }

        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("groupsStudents", groupsStudents);
        request.getRequestDispatcher("groups.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
