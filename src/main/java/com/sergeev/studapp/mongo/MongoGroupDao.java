package com.sergeev.studapp.mongo;

import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

public class MongoGroupDao extends MongoGenericDao<Group> implements GroupDao {

    @Override
    public Group getById(String key) throws PersistentException {
        return null;
    }
}
