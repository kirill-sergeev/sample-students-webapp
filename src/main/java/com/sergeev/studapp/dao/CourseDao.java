package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Course;

import java.util.List;

public interface CourseDao extends GenericDao<Course> {

    List<Course> getByDiscipline(String disciplineId) throws PersistentException;

    List<Course> getByGroup(String groupId) throws PersistentException;

    List<Course> getByTeacher(String userId) throws PersistentException;

    Course getByGroupAndDiscipline(String groupId, String disciplineId) throws PersistentException;
}
