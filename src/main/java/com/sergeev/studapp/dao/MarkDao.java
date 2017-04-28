package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Mark;

import java.util.List;

public interface MarkDao extends GenericDao<Mark> {

    Double getAvgMark(Integer studentId, Integer disciplineId);

    List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId);

    List<Mark> getByLesson(Integer lessonId);

}
