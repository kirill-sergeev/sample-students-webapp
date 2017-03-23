package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Mark;

import java.util.ArrayList;
import java.util.List;

public class MarkService {

    private static MarkDao markDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao();

    public static Mark create(String lessonId, String studentId, String value){
        Mark mark = new Mark();
        mark.setValue(Integer.valueOf(value));
        mark.setLesson(LessonService.read(lessonId));
        mark.setStudent(UserService.read(studentId));

        try {
            mark = markDao.persist(mark);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return mark;
    }

    public static List<Mark> readByLesson(String lessonId) {
        List<Mark> marks = new ArrayList<>();

        try {
            marks = markDao.getByLesson(lessonId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return marks;
    }

    public static List<Mark> readByDisciplineAndStudent(String disciplineId, String studentId) {
        List<Mark> marks = new ArrayList<>();

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