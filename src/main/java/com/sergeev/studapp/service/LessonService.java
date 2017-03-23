package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LessonService {

    private static LessonDao lessonDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getLessonDao();

    public static Lesson create(String groupId, String disciplineId, String typeId, String order, String date) {
        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        Lesson lesson = new Lesson();
        lesson.setType(Lesson.Type.getById(typeId));
        lesson.setOrder(Lesson.Order.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));
        lesson.setCourse(course);

        try {
            lesson = lessonDao.persist(lesson);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public static Lesson read(String id) {
        Lesson lesson = null;

        try {
            lesson = lessonDao.getById(id);
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
        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setType(Lesson.Type.getById(typeId));
        lesson.setOrder(Lesson.Order.getByNumber(Integer.valueOf(order)));
        lesson.setDate(Date.valueOf(date));
        lesson.setCourse(course);

        try {
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