package com.sergeev.studapp.actions.course;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateCourseServlet", urlPatterns = "/create-course")
public class CreateCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String disciplineId = request.getParameter("discipline");
        String groupId = request.getParameter("group");
        String teacherId  = request.getParameter("teacher");

        try {
            Course course = new Course();

            Discipline discipline = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao().getById(disciplineId);
            Group group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getById(groupId);
            User teacher = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao().getById(teacherId);

            course.setDiscipline(discipline);
            course.setGroup(group);
            course.setTeacher(teacher);

            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().persist(course);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("group?id="+groupId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
