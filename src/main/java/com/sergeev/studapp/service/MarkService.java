package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Mark;

import java.util.List;

public class MarkService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static MarkDao markDao = daoFactory.getMarkDao();
    private static LessonDao lessonDao = daoFactory.getLessonDao();
    private static UserDao userDao = daoFactory.getUserDao();

    public static Mark create(String lessonId, String studentId, String value){
        Mark mark = new Mark();
        mark.setValue(Integer.valueOf(value));

        try {
            mark.setLesson(lessonDao.getById(lessonId));
            mark.setStudent(userDao.getById(studentId));
            mark = markDao.persist(mark);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return mark;
    }

    public static List<Mark> readByLesson(String lessonId) {
        List<Mark> marks = null;

        try {
            marks = markDao.getByLesson(lessonId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return marks;
    }

    public static List<Mark> readByDisciplineAndStudent(String disciplineId, String studentId) {
        List<Mark> marks = null;

        try {
            marks = markDao.getByDisciplineAndStudent(disciplineId, studentId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return marks;
    }

    public static void delete(String id) {
        try {
            markDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

    public static Double calculateAvgMark(String studentId, String disciplineId) {
        Double avgMark = null;

        try {
            avgMark = markDao.getAvgMark(studentId, disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return avgMark;
    }

}