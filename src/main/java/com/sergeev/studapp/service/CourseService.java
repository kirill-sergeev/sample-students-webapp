package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import java.util.List;

public class CourseService {

    private static CourseDao courseDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getCourseDao();

    public static Course create(String disciplineId, String groupId, String teacherId) {
        Discipline discipline = DisciplineService.read(disciplineId);
        Group group = GroupService.read(groupId);
        User teacher = UserService.read(teacherId);

        Course course = new Course();
        course.setDiscipline(discipline);
        course.setGroup(group);
        course.setTeacher(teacher);

        try {
            course = courseDao.persist(course);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static Course read(String id) {
        Course course = null;

        try {
            course = courseDao.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static List<Course> readAll() {
        List<Course> courses = null;

        try {
            courses = courseDao.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public static List<Course> readByGroup(String groupId) {
        List<Course> courses = null;

        try {
            courses = courseDao.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public static List<Course> readByTeacher(String teacherId) {
        List<Course> courses = null;

        try {
            courses = courseDao.getByTeacher(teacherId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public static List<Course> readByDiscipline(String disciplineId) {
        List<Course> courses = null;

        try {
            courses = courseDao.getByDiscipline(disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public static Course readByDisciplineAndGroup(String disciplineId, String groupId) {
        Course course = null;

        try {
            course = courseDao.getByDisciplineAndGroup(disciplineId, groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static Course update(String disciplineId, String groupId, String teacherId, String courseId) {
        Discipline discipline = DisciplineService.read(disciplineId);
        Group group = GroupService.read(groupId);
        User teacher = UserService.read(teacherId);

        Course course = new Course();
        course.setId(courseId);
        course.setDiscipline(discipline);
        course.setGroup(group);
        course.setTeacher(teacher);

        try {
            courseDao.update(course);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static void delete(String courseId) {
        try {
            courseDao.delete(courseId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}
