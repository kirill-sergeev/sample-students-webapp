package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Mark;

import java.util.List;

public interface MarkDao extends GenericDao<Mark> {
    Double getAvgMark(Integer studentId, Integer disciplineId) throws PersistentException;

    List<Mark> getByLesson(Integer lessonId) throws PersistentException;

    List<Mark> getByStudentAndDiscipline(Integer studentId, Integer disciplineId) throws PersistentException;
}
