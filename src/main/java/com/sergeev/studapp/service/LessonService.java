package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public final class LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
    private static final LessonDao LESSON_DAO = DaoFactory.getDaoFactory().getLessonDao();

    public static Lesson save(int groupId, int disciplineId, String type, int order, LocalDate date) {
          Course course = CourseService.getByDisciplineAndGroup(disciplineId, groupId);
        Lesson lesson = new Lesson()
                .setType(Lesson.Type.valueOf(type))
                .setOrder(Lesson.Order.values()[order])
                .setDate(date)
                .setCourse(course);
        LESSON_DAO.save(lesson);
        return lesson;
    }

    public static Lesson update(int groupId, int disciplineId, String type, int order, LocalDate date, int id) {
        Course course = CourseService.getByDisciplineAndGroup(disciplineId, groupId);
        Lesson lesson = new Lesson()
                .setId(id)
                .setType(Lesson.Type.valueOf(type))
                .setOrder(Lesson.Order.values()[order])
                .setDate(date)
                .setCourse(course);
        LESSON_DAO.update(lesson);
        return lesson;
    }

    public static void remove(int id) {
        LESSON_DAO.remove(id);
    }

    public static Lesson get(int id) {
        return LESSON_DAO.getById(id);
    }

    public static List<Lesson> getAll(int groupId) {
        return LESSON_DAO.getByGroup(groupId);
    }

    private LessonService() {
    }

}