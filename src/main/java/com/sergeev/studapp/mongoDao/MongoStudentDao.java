package com.sergeev.studapp.mongoDao;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Student;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class MongoStudentDao extends MongoGenericDao<Student> implements StudentDao{
    private Datastore store = MongoDaoFactory.getStore();
    @Override
    public List<Student> getByName(String name) throws PersistentException {
        Query<Student> query = store.createQuery(Student.class).filter("firstName =", name);
        return query.asList();
    }

    @Override
    public List<Student> getByGroup(String groupTitle) throws PersistentException {
        Query<Student> query = store.createQuery(Student.class).filter("group.title =", groupTitle);
        return query.asList();
    }

    @Override
    public Student getById(String key) throws PersistentException {
        ObjectId oid = new ObjectId(key);
        return store.find(Student.class).field("id").equal(oid).get();
    }

    @Override
    public List<Student> getAll() throws PersistentException {
        return MongoDaoFactory.getStore().createQuery(Student.class).asList();
    }
}
