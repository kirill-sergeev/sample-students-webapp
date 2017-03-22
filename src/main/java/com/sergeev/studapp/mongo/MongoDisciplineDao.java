package com.sergeev.studapp.mongo;

import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;

public class MongoDisciplineDao extends MongoGenericDao<Discipline> implements DisciplineDao {

    @Override
    public Discipline getById(String key) throws PersistentException {
        return null;
    }
}
