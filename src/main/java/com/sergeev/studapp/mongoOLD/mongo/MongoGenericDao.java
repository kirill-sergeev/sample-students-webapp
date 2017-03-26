package com.sergeev.studapp.mongoOLD.mongo;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class MongoGenericDao<T extends Identified> implements GenericDao<T> {

    private Datastore store = MongoDaoFactory.getStore();
    private Class clazz = getGenericParameterClass(getClass(), 0);

    @Override
    public T persist(T object) throws PersistentException {
        store.save(object);
        object.setId((String) store.getKey(object).getId());
        return object;
    }

    @Override
    public void update(T object) throws PersistentException {
        store.merge(object);
    }

    @Override
    public void delete(T object) throws PersistentException {
        ObjectId oid = new ObjectId(object.getId());
        store.delete(clazz, oid);
    }

    @Override
    public void delete(String key) throws PersistentException {
        ObjectId oid = new ObjectId(key);
        store.delete(clazz, oid);
    }

    @Override
    public List<T> getAll() throws PersistentException {
        return store.find(clazz).asList();
        // return store.createQuery(clazz).asList();
    }

    @Override
    public T getById(String key) throws PersistentException {
        ObjectId oid = new ObjectId(key);
        List<T> list = store.find(clazz, "id =", oid).asList();
        return  list.listIterator().next();
    }

    public List<T> read(String query, String value)
    {
        if (query == null && value == null) {
            return store.find(clazz).asList();
        }
        return store.find(clazz, query, value).asList();
    }

    private static Class getGenericParameterClass(Class actualClass, int index) {
        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[index];
    }
}
