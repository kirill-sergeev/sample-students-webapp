package com.sergeev.studapp.mongo;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class MongoUserDao extends MongoGenericDao<User> implements UserDao{
    private Datastore store = MongoDaoFactory.getStore();

    public List<User> getByName(String name) throws PersistentException {
        Query<User> query = store.createQuery(User.class).filter("firstName =", name);
        return query.asList();
    }

    @Override
    public List<User> getByName(String name, User.Role type) throws PersistentException {
        return null;
    }

    @Override
    public List<User> getByGroup(String groupTitle) throws PersistentException {
        Query<User> query = store.createQuery(User.class).filter("group.title =", groupTitle);
        return query.asList();
    }

    @Override
    public List<User> getAll(User.Role type) throws PersistentException {
        return null;
    }

    @Override
    public User getByAccount(String login, String password) throws PersistentException {
        return null;
    }

    @Override
    public User getById(String key) throws PersistentException {
        ObjectId oid = new ObjectId(key);
        return store.find(User.class).field("id").equal(oid).get();
    }

    @Override
    public List<User> getAll() throws PersistentException {
        return MongoDaoFactory.getStore().createQuery(User.class).asList();
    }
}
