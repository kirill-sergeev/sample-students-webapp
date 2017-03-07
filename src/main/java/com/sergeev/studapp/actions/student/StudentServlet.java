package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StudentServlet", urlPatterns = "/student")
public class StudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer studentId = Integer.valueOf(request.getParameter("id"));
        Student student = new Student();
        List<Double> avgMark = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        try {
            student = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getByPK(studentId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByGroup(student.getGroup().getId());
            for(Course course: courses){
                avgMark.add(DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().getAvgMark(studentId, course.getDiscipline().getId()));
            }
        } catch (PersistentException e) {
            e.printStackTrace();
        }


        Map<Course, Double> map = new HashMap<>();
        for (int i = 0; i < courses.size(); i++) {
            map.put(courses.get(i), avgMark.get(i));
        }

        request.setAttribute("map", map);
        request.setAttribute("student", student);
        request.getRequestDispatcher("student.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
