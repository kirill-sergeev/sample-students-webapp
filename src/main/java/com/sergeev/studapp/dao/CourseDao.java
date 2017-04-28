package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Course;

import java.util.List;

public interface CourseDao extends GenericDao<Course> {

    Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId);

    List<Course> getByDiscipline(Integer disciplineId);

    List<Course> getByGroup(Integer groupId);

    List<Course> getByTeacher(Integer userId);

}
