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
    private static final MarkDao MARK_DAO = DaoFactory.getDaoFactory().getMarkDao();

    private static Mark mark;
    private static List<Mark> marks;

    public static Mark create(Integer lessonId, Integer studentId, Integer value) throws ApplicationException {
        if (value < 0 || value > 100){
            throw new ApplicationException("Bad parameters.");
        }

        mark = new Mark();
        mark.setValue(value);
        mark.setLesson(LessonService.read(lessonId));
        mark.setStudent(UserService.read(studentId));

        try {
            mark = MARK_DAO.save(mark);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save mark.", e);
        }

        return mark;
    }

    public static Mark read(Integer id) throws ApplicationException {
        if (id == null){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            mark = MARK_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Mark not found.", e);
        }

        return mark;
    }

    public static List<Mark> readByLesson(Integer lessonId) throws ApplicationException {
        if (lessonId == null){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            marks = MARK_DAO.getByLesson(lessonId);
        } catch (PersistentException e) {
            marks  = Collections.emptyList();
        }

        return marks;
    }

    public static List<Mark> readAll(){

        try {
            marks = MARK_DAO.getAll();
        } catch (PersistentException e) {
            marks  = Collections.emptyList();
        }

        return marks;
    }

    public static List<Mark> readByDisciplineAndStudent(Integer disciplineId, Integer studentId) throws ApplicationException {
        if (disciplineId == null || studentId == null){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            marks = MARK_DAO.getByDisciplineAndStudent(disciplineId, studentId);
        } catch (PersistentException e) {
            marks = Collections.emptyList();
        }
        return marks;
    }

    public static void delete(Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException("Bad parameters.");
        }

        try {
            MARK_DAO.delete(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot delete mark, because mark not found.", e);
        }
    }

    protected static Double calculateAvgMark(Integer studentId, Integer disciplineId) throws ApplicationException {
        Double avgMark;

        try {
            avgMark = MARK_DAO.getAvgMark(studentId, disciplineId);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot calculate average mark on student.", e);
        }

        return avgMark;
    }

}