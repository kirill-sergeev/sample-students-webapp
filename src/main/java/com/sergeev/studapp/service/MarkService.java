package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class MarkService {

    private static final Logger LOG = LoggerFactory.getLogger(MarkService.class);
    private static final MarkDao MARK_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getMarkDao();

    private static Mark mark;
    private static List<Mark> marks;

    public static Mark create(String lessonId, String studentId, String value) throws ApplicationException {
        if (!checkValue(value)){
            throw new ApplicationException("Bad parameters.");
        }

        mark = new Mark();
        mark.setValue(Integer.valueOf(value));
        mark.setLesson(LessonService.read(lessonId));
        mark.setStudent(UserService.read(studentId));

        try {
            mark = MARK_DAO.persist(mark);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save mark.", e);
        }

        return mark;
    }

    public static Mark read(String id) throws ApplicationException {
        if (id == null || id.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            mark = MARK_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Mark not found.", e);
        }

        return mark;
    }

    public static List<Mark> readByLesson(String lessonId) throws ApplicationException {
        if (lessonId == null || lessonId.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            marks = MARK_DAO.getByLesson(lessonId);
        } catch (PersistentException e) {
            marks  = Collections.emptyList();
        }

        return marks;
    }

    public static List<Mark> readByDisciplineAndStudent(String disciplineId, String studentId) throws ApplicationException {
        if (disciplineId == null || studentId == null || disciplineId.isEmpty() || studentId.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            marks = MARK_DAO.getByDisciplineAndStudent(disciplineId, studentId);
        } catch (PersistentException e) {
            marks = Collections.emptyList();
        }
        return marks;
    }

    public static void delete(String id) throws ApplicationException {
        if (id == null || id.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }

        try {
            MARK_DAO.delete(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot delete mark, because mark not found.", e);
        }
    }

    protected static Double calculateAvgMark(String studentId, String disciplineId) throws ApplicationException {
        Double avgMark;

        try {
            avgMark = MARK_DAO.getAvgMark(studentId, disciplineId);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot calculate average mark on student.", e);
        }

        return avgMark;
    }

    private static boolean checkValue(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        int mark = Integer.parseInt(value);
        return mark >= 0 && mark <= 100;
    }

}