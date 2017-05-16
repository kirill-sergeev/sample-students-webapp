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

public final class LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
    private static LessonDao lessonDao = DaoFactory.getDaoFactory().getLessonDao();

    public static Lesson save(int groupId, int disciplineId, String type, int order, LocalDate date) {
        Lesson lesson = null;
        try (DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            Course course = dao.getCourseDao().getByDisciplineAndGroup(disciplineId, groupId);
            lesson = new Lesson()
                    .setType(Lesson.Type.valueOf(type))
                    .setOrder(Lesson.Order.values()[order])
                    .setDate(date)
                    .setCourse(course);
            dao.getLessonDao().save(lesson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lesson;
    }

    public static Lesson update(int groupId, int disciplineId, String type, int order, LocalDate date, int id) {
        Lesson lesson = null;
        try (DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            Course course = dao.getCourseDao().getByDisciplineAndGroup(disciplineId, groupId);
            lesson = new Lesson()
                    .setId(id)
                    .setType(Lesson.Type.valueOf(type))
                    .setOrder(Lesson.Order.values()[order])
                    .setDate(date)
                    .setCourse(course);
            dao.getLessonDao().update(lesson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lesson;
    }

    public static void remove(int id) {
        lessonDao.remove(id);
    }

    public static Lesson get(int id) {
        return lessonDao.getById(id);
    }

    public static List<Lesson> getAll(int groupId) {
        List<Lesson> lessons;
        try {
            lessons = lessonDao.getByGroup(groupId);
        } catch (PersistentException e){
            lessons = Collections.emptyList();
        }
        return lessons;
    }

    private LessonService() {
    }

}