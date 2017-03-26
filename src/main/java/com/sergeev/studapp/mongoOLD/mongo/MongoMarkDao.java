package com.sergeev.studapp.mongoOLD.mongo;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;

import java.util.List;

public class MongoMarkDao extends MongoGenericDao<Mark> implements MarkDao {
    @Override
    public Double getAvgMark(String studentId, String disciplineId) throws PersistentException {
        return null;
    }

    @Override
    public List<Mark> getByLesson(String lessonId) throws PersistentException {
        return null;
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(String disciplineId, String studentId) throws PersistentException {
        return null;
    }

    @Override
    public Mark getById(String key) throws PersistentException {
        return null;
    }
}
