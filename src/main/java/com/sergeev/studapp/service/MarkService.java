package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Mark;

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

    public static void delete(String id) {
        try {
            markDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}