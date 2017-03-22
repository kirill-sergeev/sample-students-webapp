package com.sergeev.studapp.mongo;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;

import java.util.List;

public class MongoCourseDao extends MongoGenericDao<Course> implements CourseDao {
    @Override
    public List<Course> getByDiscipline(String disciplineId) throws PersistentException {
        return null;
    }

    @Override
    public List<Course> getByGroup(String groupId) throws PersistentException {
        return null;
    }

    @Override
    public List<Course> getByTeacher(String teacherId) throws PersistentException {
        return null;
    }

    @Override
    public Course getByGroupAndDiscipline(String groupId, String disciplineId) throws PersistentException {
        return null;
    }

    @Override
    public Course getById(String key) throws PersistentException {
        return null;
    }
}
