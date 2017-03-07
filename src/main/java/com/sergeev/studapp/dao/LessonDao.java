package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Lesson;

import java.util.List;

public interface LessonDao extends GenericDao<Lesson, Integer> {
    List<Lesson> getByGroup(Integer courseId) throws PersistentException;
}
