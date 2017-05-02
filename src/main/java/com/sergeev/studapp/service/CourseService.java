package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
    private static final CourseDao COURSE_DAO = DaoFactory.getDaoFactory().getCourseDao();

    public static Course save(int disciplineId, int groupId, int teacherId) {
        Discipline discipline = DisciplineService.get(disciplineId);
        Group group = GroupService.get(groupId);
        User teacher = UserService.get(teacherId);
        Course course = new Course()
                .setDiscipline(discipline)
                .setGroup(group)
                .setTeacher(teacher);
        
        COURSE_DAO.save(course);
        return course;
    }

    public static Course update(int disciplineId, int groupId, int teacherId, int id) {
        Discipline discipline = DisciplineService.get(disciplineId);
        Group group = GroupService.get(groupId);
        User teacher = UserService.get(teacherId);
        Course course = new Course()
                .setId(id)
                .setDiscipline(discipline)
                .setGroup(group)
                .setTeacher(teacher);

        COURSE_DAO.update(course);
        return course;
    }

    public static void remove(int id) {
        COURSE_DAO.remove(id);
    }
    
    public static Course get(int id) {
        return COURSE_DAO.getById(id);
    }

    public static List<Course> getAll() {
        return COURSE_DAO.getAll();
    }

    public static List<Course> getByGroup(int groupId) {
        return COURSE_DAO.getByGroup(groupId);
    }

    public static List<Course> getByTeacher(int teacherId) {
        return COURSE_DAO.getByTeacher(teacherId);
    }

    public static List<Course> getByDiscipline(int disciplineId) {
        return COURSE_DAO.getByDiscipline(disciplineId);
    }

    static Course getByDisciplineAndGroup(int disciplineId, int groupId) {
        return COURSE_DAO.getByDisciplineAndGroup(disciplineId, groupId);
    }

    private CourseService() {
    }

}
