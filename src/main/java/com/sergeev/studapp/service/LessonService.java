package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
    private static final LessonDao LESSON_DAO = DaoFactory.getDaoFactory().getLessonDao();

    private static Lesson lesson;
    private static List<Lesson> lessons;

    public static Lesson create(Integer groupId, Integer disciplineId, String type, Integer order, LocalDate date) throws ApplicationException {
        if (date == null){
            throw new ApplicationException("Bad parameters.");
        }

        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        lesson = new Lesson();
        lesson.setType(Lesson.Type.valueOf(type));
        lesson.setOrder(Lesson.Order.values()[order]);
        lesson.setDate(date);
        lesson.setCourse(course);

        try {
            LESSON_DAO.save(lesson);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save lesson.", e);
        }

        return lesson;
    }

    public static Lesson read(Integer id) throws ApplicationException {
        if (id == null){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            lesson = LESSON_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Lesson not found.", e);
        }

        return lesson;
    }

    public static List<Lesson> readAll(Integer groupId){
        try {
            lessons = LESSON_DAO.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
            lessons  = Collections.emptyList();
        }

        return lessons;
    }

    public static Lesson update(Integer groupId, Integer disciplineId, String type, Integer order, LocalDate date, Integer id) throws ApplicationException {
        if (id == null|| date == null){
            throw new ApplicationException("Bad parameters.");
        }

        Course course = CourseService.readByDisciplineAndGroup(disciplineId, groupId);

        lesson = new Lesson();
        lesson.setId(id);
        lesson.setType(Lesson.Type.valueOf(type));
        lesson.setOrder(Lesson.Order.values()[order]);
        lesson.setDate(date);
        lesson.setCourse(course);

        try {
            LESSON_DAO.update(lesson);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update lesson.", e);
        }

        return lesson;
    }

    public static void delete(Integer id) throws ApplicationException {
        try {
            LESSON_DAO.remove(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot remove lesson, because lesson not found.", e);
        }
    }
}