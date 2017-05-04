package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.sergeev.studapp.model.Constants.*;

public class MongoCourseDao extends MongoGenericDao<Course> implements CourseDao {

    public MongoCourseDao() {
        IndexOptions options = new IndexOptions().background(true);
        collection.createIndex(Indexes.ascending(DISCIPLINE_ID, GROUP_ID), options);
        collection.createIndex(Indexes.ascending(DISCIPLINE_ID), options);
        collection.createIndex(Indexes.ascending(GROUP_ID), options);
        collection.createIndex(Indexes.ascending(USER_ID), options);

    }

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(COURSES);
    }

    @Override
    protected Document createDocument(Course object) {
        Document doc = new Document(DISCIPLINE_ID, object.getDiscipline().getId())
                .append(GROUP_ID, object.getGroup().getId())
                .append(USER_ID, object.getTeacher().getId());
        if (object.getId() == null) {
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Course parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }

        MongoDisciplineDao disciplineDao = new MongoDisciplineDao();
        MongoGroupDao groupDao = new MongoGroupDao();
        MongoUserDao userDao = new MongoUserDao();

        return new Course().setId(doc.getInteger(ID))
                .setDiscipline(disciplineDao.getById(doc.getInteger(DISCIPLINE_ID)))
                .setGroup(groupDao.getById(doc.getInteger(GROUP_ID)))
                .setTeacher(userDao.getById(doc.getInteger(USER_ID)));
    }

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) {
        return getByParams((eq(DISCIPLINE_ID, disciplineId)), null);
    }

    @Override
    public List<Course> getByGroup(Integer groupId) {
        return getByParams((eq(GROUP_ID, groupId)), null);
    }

    @Override
    public List<Course> getByTeacher(Integer userId) {
        return getByParams((eq(USER_ID, userId)), null);
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) {
        return getByParams((and(eq(DISCIPLINE_ID, disciplineId), eq(GROUP_ID, groupId))), null).get(0);
    }

}
