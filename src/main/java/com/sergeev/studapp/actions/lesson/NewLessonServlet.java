package com.sergeev.studapp.actions.lesson;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewLessonServlet", urlPatterns = "/new-lesson")
public class NewLessonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupId = request.getParameter("group");

        Group group = new Group();
        Lesson.LessonType[] types = Lesson.LessonType.values();
        Lesson.LessonOrder[] orders = Lesson.LessonOrder.values();
        List<Course> courses = new ArrayList<>();

        try {
            group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getById(groupId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("types", types);
        request.setAttribute("orders", orders);
        request.setAttribute("group", group);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("new-lesson.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}