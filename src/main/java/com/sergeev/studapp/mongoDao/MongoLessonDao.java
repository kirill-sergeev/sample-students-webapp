package com.sergeev.studapp.mongoDao;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;

import java.util.List;

public class MongoLessonDao extends MongoGenericDao<Lesson> implements LessonDao {
    @Override
    public List<Lesson> getByGroup(String courseId) throws PersistentException {
        return null;
    }

    @Override
    public Lesson getById(String key) throws PersistentException {
        return null;
    }
}
