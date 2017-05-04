package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
    private static CourseDao courseDao = DaoFactory.getDaoFactory().getCourseDao();

    public static Course save(int disciplineId, int groupId, int teacherId) {
        Course course = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
                dao.startTransaction();
                Discipline discipline = dao.getDisciplineDao().getById(disciplineId);
                Group group = dao.getGroupDao().getById(groupId);
                User teacher = dao.getUserDao().getById(teacherId);
                course = new Course()
                        .setDiscipline(discipline)
                        .setGroup(group)
                        .setTeacher(teacher);
                dao.getCourseDao().save(course);
            } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    public static Course update(int disciplineId, int groupId, int teacherId, int id) {
        Course course = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            Discipline discipline = dao.getDisciplineDao().getById(disciplineId);
            Group group = dao.getGroupDao().getById(groupId);
            User teacher = dao.getUserDao().getById(teacherId);
            course = new Course()
                    .setId(id)
                    .setDiscipline(discipline)
                    .setGroup(group)
                    .setTeacher(teacher);
            dao.getCourseDao().update(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    public static void remove(int id) {
        courseDao.remove(id);
    }
    
    public static Course get(int id) {
        return courseDao.getById(id);
    }

    public static List<Course> getAll() {
        return courseDao.getAll();
    }

    public static List<Course> getByGroup(int groupId) {
        return courseDao.getByGroup(groupId);
    }

    public static List<Course> getByTeacher(int teacherId) {
        return courseDao.getByTeacher(teacherId);
    }

    public static List<Course> getByDiscipline(int disciplineId) {
        return courseDao.getByDiscipline(disciplineId);
    }

    private CourseService() {
    }

}
