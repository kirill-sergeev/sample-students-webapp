package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MarkService {

    private static final Logger LOG = LoggerFactory.getLogger(MarkService.class);
    private static final MarkDao MARK_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao();

    public static Mark create(String lessonId, String studentId, String value){
        Mark mark = new Mark();
        mark.setValue(Integer.valueOf(value));
        mark.setLesson(LessonService.read(lessonId));
        mark.setStudent(UserService.read(studentId));

        try {
            mark = MARK_DAO.persist(mark);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return mark;
    }

    public static List<Mark> readByLesson(String lessonId) {
        List<Mark> marks = new ArrayList<>();

        try {
            marks = MARK_DAO.getByLesson(lessonId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return marks;
    }

    public static List<Mark> readByDisciplineAndStudent(String disciplineId, String studentId) {
        List<Mark> marks = new ArrayList<>();

        try {
            marks = MARK_DAO.getByDisciplineAndStudent(disciplineId, studentId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return marks;
    }

    public static void delete(String id) {
        try {
            MARK_DAO.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

    public static Double calculateAvgMark(String studentId, String disciplineId) {
        Double avgMark = null;

        try {
            avgMark = MARK_DAO.getAvgMark(studentId, disciplineId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return avgMark;
    }

}