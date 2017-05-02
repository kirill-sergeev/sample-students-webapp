package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class MarkService {

    private static final Logger LOG = LoggerFactory.getLogger(MarkService.class);
    private static final MarkDao MARK_DAO = DaoFactory.getDaoFactory().getMarkDao();

    public static Mark save(int lessonId, int studentId, int value) {
        if (value < 0 || value > 100) {
            throw new ApplicationException("Bad parameters.");
        }
        Mark mark = new Mark()
                .setValue(value)
                .setLesson(LessonService.get(lessonId))
                .setStudent(UserService.get(studentId));
        MARK_DAO.save(mark);
        return mark;
    }

    public static Mark get(int id) {
        return MARK_DAO.getById(id);
    }

    public static List<Mark> getByLesson(int lessonId) {
        return MARK_DAO.getByLesson(lessonId);
    }

    public static List<Mark> readAll() {
        return MARK_DAO.getAll();
    }

    public static List<Mark> getByDisciplineAndStudent(int disciplineId, int studentId) {
        return MARK_DAO.getByDisciplineAndStudent(disciplineId, studentId);
    }

    public static void delete(int id) {
        MARK_DAO.remove(id);
    }

    static Double getAvgMark(int studentId, int disciplineId) {
        return MARK_DAO.getAvgMark(studentId, disciplineId);
    }

    private MarkService() {
    }

}