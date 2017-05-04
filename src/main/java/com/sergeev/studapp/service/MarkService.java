package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class MarkService {

    private static final Logger LOG = LoggerFactory.getLogger(MarkService.class);
    private static MarkDao markDao = DaoFactory.getDaoFactory().getMarkDao();

    public static Mark save(int lessonId, int studentId, int value) {
        if (value < 0 || value > 100) {
            throw new ApplicationException("Bad parameters.");
        }
        Mark mark = null;
        try (DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            mark = new Mark()
                    .setValue(value)
                    .setLesson(dao.getLessonDao().getById(lessonId))
                    .setStudent(dao.getUserDao().getById(studentId));
            dao.getMarkDao().save(mark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mark;
    }

    public static Mark get(int id) {
        return DaoFactory.getDaoFactory().getMarkDao().getById(id);
    }

    public static List<Mark> getByLesson(int lessonId) {
        return markDao.getByLesson(lessonId);
    }

    public static List<Mark> getByDisciplineAndStudent(int disciplineId, int studentId) {
        return markDao.getByDisciplineAndStudent(disciplineId, studentId);
    }

    public static void delete(int id) {
        markDao.remove(id);
    }

    private MarkService() {
    }

}