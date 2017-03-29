package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
    private static final LessonDao LESSON_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao();

    public static Lesson create(String groupId, String disciplineId, String typeId, String order, String date) {
        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        Lesson lesson = new Lesson();
        lesson.setType(Lesson.Type.getById(typeId));
        lesson.setOrder(Lesson.Order.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));
        lesson.setCourse(course);

        try {
            lesson = LESSON_DAO.persist(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static Lesson read(String id) {
        Lesson lesson = null;

        try {
            lesson = LESSON_DAO.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static List<Lesson> readAll(String groupId){
        List<Lesson> lessons = new ArrayList<>();

        try {
            lessons = LESSON_DAO.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lessons;
    }

    public static Lesson update(String groupId, String disciplineId, String typeId, String order, String date, String lessonId) {
        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setType(Lesson.Type.getById(typeId));
        lesson.setOrder(Lesson.Order.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));
        lesson.setCourse(course);

        try {
            LESSON_DAO.update(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static void delete(String id) {
        try {
            LESSON_DAO.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}