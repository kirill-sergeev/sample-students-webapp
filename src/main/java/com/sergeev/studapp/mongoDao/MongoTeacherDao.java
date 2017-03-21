package com.sergeev.studapp.mongoDao;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.TeacherDao;
import com.sergeev.studapp.model.Teacher;

public class MongoTeacherDao extends MongoGenericDao<Teacher> implements TeacherDao {
    @Override
    public Teacher getById(String key) throws PersistentException {
        return null;
    }
}
