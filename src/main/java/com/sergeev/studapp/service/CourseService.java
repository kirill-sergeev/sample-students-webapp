package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
    private static final CourseDao COURSE_DAO = DaoFactory.getDaoFactory().getCourseDao();

    public static Course create(String disciplineId, String groupId, String teacherId) throws ApplicationException {
        Discipline discipline = DisciplineService.read(disciplineId);
        Group group = GroupService.read(groupId);
        User teacher = UserService.read(teacherId);

        Course course = new Course();
        course.setDiscipline(discipline);
        course.setGroup(group);
        course.setTeacher(teacher);

        try {
            course = COURSE_DAO.persist(course);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save course.", e);
        }

        return course;
    }

    public static Course read(String id) throws ApplicationException {
        if (id == null || id.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        Course course;
        try {
            course = COURSE_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Course not found.", e);
        }

        return course;
    }

    public static List<Course> readAll() {
        List<Course> courses;
        try {
            courses = COURSE_DAO.getAll();
        } catch (PersistentException e) {
            courses = Collections.emptyList();
        }
        return courses;
    }

    public static List<Course> readByGroup(String groupId) throws ApplicationException {
        if (groupId == null || groupId.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        List<Course> courses;
        try {
            courses = COURSE_DAO.getByGroup(groupId);
        } catch (PersistentException e) {
            courses = Collections.emptyList();
        }

        return courses;
    }

    public static List<Course> readByTeacher(String teacherId) throws ApplicationException {
        if (teacherId == null || teacherId.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        List<Course> courses;
        try {
            courses = COURSE_DAO.getByTeacher(teacherId);
        } catch (PersistentException e) {
            courses = Collections.emptyList();
        }

        return courses;
    }

    public static List<Course> readByDiscipline(String disciplineId) throws ApplicationException {
        if (disciplineId == null || disciplineId.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        List<Course> courses;
        try {
            courses = COURSE_DAO.getByDiscipline(disciplineId);
        } catch (PersistentException e) {
            courses = Collections.emptyList();
        }

        return courses;
    }

    public static Course update(String disciplineId, String groupId, String teacherId, String id) throws ApplicationException {
        if (id == null || id.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }

        Discipline discipline = DisciplineService.read(disciplineId);
        Group group = GroupService.read(groupId);
        User teacher = UserService.read(teacherId);

        Course course = new Course();
        course.setId(id);
        course.setDiscipline(discipline);
        course.setGroup(group);
        course.setTeacher(teacher);

        try {
            COURSE_DAO.update(course);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update course.", e);
        }

        return course;
    }

    public static void delete(String id) throws ApplicationException {
        if (id == null || id.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }

        try {
            COURSE_DAO.delete(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot delete course, because course not found.", e);
        }
    }

    protected static Course readByDisciplineAndGroup(String disciplineId, String groupId) throws ApplicationException {
        if (disciplineId == null || groupId == null || disciplineId.isEmpty() || groupId.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        Course course;
        try {
            course = COURSE_DAO.getByDisciplineAndGroup(disciplineId, groupId);
        } catch (PersistentException e) {
            throw new ApplicationException("Course not found.", e);
        }

        return course;
    }
}
