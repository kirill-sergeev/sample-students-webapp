package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LessonService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static LessonDao lessonDao = daoFactory.getLessonDao();
    private static CourseDao courseDao = daoFactory.getCourseDao();

    public static Lesson create(String groupId, String disciplineId, String typeId, String order, String date) {
        Lesson lesson = new Lesson();
        lesson.setType(Lesson.LessonType.getById(typeId));
        lesson.setOrder(Lesson.LessonOrder.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));

        try {
            Course course = courseDao.getByGroupAndDiscipline(groupId, disciplineId);
            lesson.setCourse(course);
            lesson = lessonDao.persist(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static List<Lesson> readAll(String groupId){
        List<Lesson> lessons = new ArrayList<>();

        try {
            lessons = lessonDao.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lessons;
    }

    public static Lesson update(String groupId, String disciplineId, String typeId, String order, String date, String lessonId) {
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setType(Lesson.LessonType.getById(typeId));
        lesson.setOrder(Lesson.LessonOrder.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));

        try {
            Course course = courseDao.getByGroupAndDiscipline(groupId, disciplineId);
            lesson.setCourse(course);
            lessonDao.update(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static void delete(String id) {
        try {
            lessonDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}