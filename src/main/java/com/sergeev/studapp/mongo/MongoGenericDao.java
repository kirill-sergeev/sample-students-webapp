package com.sergeev.studapp.mongo;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class MongoGenericDao<T extends Identified> implements GenericDao<T> {

    private Datastore store = MongoDaoFactory.getStore();

    @Override
    public T persist(T object) throws PersistentException {
        store.save(object);
        object.setId((String) store.getKey(object).getId());
        return object;
    }

    @Override
    public void update(T object) throws PersistentException {
//        query = store.createQuery(UserData.class).field("uUid").equal("1234");
//        ops = store.createUpdateOperations(UserData.class).set("status", "active");
//
//        store.update(query, ops);
//
    }

    @Override
    public void delete(T object) throws PersistentException {
        ObjectId oid = new ObjectId(object.getId());
        store.delete(getGenericParameterClass(getClass(), 0), oid);
    }

    @Override
    public void delete(String key) throws PersistentException {
        ObjectId oid = new ObjectId(key);
        store.delete(getGenericParameterClass(getClass(), 0), oid);
    }

    @Override
    public List<T> getAll() throws PersistentException {
        return null;
    }

    private static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
    }

}
