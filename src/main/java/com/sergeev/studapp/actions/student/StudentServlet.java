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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StudentServlet", urlPatterns = "/student")
public class StudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("id");

        Student student = new Student();
        List<Course> courses;
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        try {
            student = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().getById(studentId);
            courses = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao().getByGroup(student.getGroup().getId());

            double avgMark;
            for(Course course: courses){
                avgMark = (DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao().getAvgMark(studentId, course.getDiscipline().getId()));
                coursesMarks.put(course, avgMark);
            }

        } catch (PersistentException e) {
            e.printStackTrace();
        }

        request.setAttribute("coursesMarks", coursesMarks);
        request.setAttribute("student", student);
        request.getRequestDispatcher("student.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
