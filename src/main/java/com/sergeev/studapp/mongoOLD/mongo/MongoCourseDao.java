package com.sergeev.studapp.mongoOLD.mongo;

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
    public Course getByDisciplineAndGroup(String disciplineId, String groupId) throws PersistentException {
        return null;
    }
}
