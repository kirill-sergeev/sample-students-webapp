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

    public static Course create(Integer disciplineId, Integer groupId, Integer teacherId) throws ApplicationException {
        Discipline discipline = DisciplineService.read(disciplineId);
        Group group = GroupService.read(groupId);
        User teacher = UserService.read(teacherId);

        Course course = new Course();
        course.setDiscipline(discipline);
        course.setGroup(group);
        course.setTeacher(teacher);

        try {
            COURSE_DAO.save(course);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save course.", e);
        }

        return course;
    }

    public static Course read(Integer id) throws ApplicationException {
        if (id == null) {
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

    public static List<Course> readByGroup(Integer groupId) throws ApplicationException {
        if (groupId == null) {
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

    public static List<Course> readByTeacher(Integer teacherId) throws ApplicationException {
        if (teacherId == null) {
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

    public static List<Course> readByDiscipline(Integer disciplineId) throws ApplicationException {
        if (disciplineId == null) {
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

    public static Course update(Integer disciplineId, Integer groupId, Integer teacherId, Integer id) throws ApplicationException {
        if (id == null) {
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

    public static void delete(Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException("Bad parameters.");
        }

        try {
            COURSE_DAO.remove(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot remove course, because course not found.", e);
        }
    }

    protected static Course readByDisciplineAndGroup(Integer disciplineId, Integer groupId) throws ApplicationException {
        if (disciplineId == null || groupId == null) {
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
