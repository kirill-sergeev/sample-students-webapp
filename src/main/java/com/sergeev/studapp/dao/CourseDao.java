package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Course;

import java.util.List;

public interface CourseDao extends GenericDao<Course> {

    List<Course> getByDiscipline(Integer disciplineId) throws PersistentException;

    List<Course> getByGroup(Integer groupId) throws PersistentException;

    List<Course> getByTeacher(Integer userId) throws PersistentException;

    Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) throws PersistentException;

}
