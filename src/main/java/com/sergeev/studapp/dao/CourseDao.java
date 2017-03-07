package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Course;

import java.util.List;

public interface CourseDao extends GenericDao<Course, Integer> {
    List<Course> getByGroup(Integer groupId) throws PersistentException;
    List<Course> getByTeacher(Integer teacherId) throws PersistentException;
}
