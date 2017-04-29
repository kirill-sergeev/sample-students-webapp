package com.sergeev.studapp.mongo;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import static com.sergeev.studapp.model.Constants.*;

public class MongoCourseDao extends MongoGenericDao<Course> implements CourseDao {

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(COURSES);
    }

    @Override
    protected Document createDocument(Course object) {
        Document doc = new Document(DISCIPLINE_ID, object.getDiscipline().getId())
                .append(GROUP_ID, object.getGroup().getId())
                .append(USER_ID, object.getTeacher().getId());
        if (object.getId() == null){
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
        List<Course> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Course item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(DISCIPLINE_ID, disciplineId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByGroup(Integer groupId) {
        List<Course> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Course item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(GROUP_ID, groupId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByTeacher(Integer userId) {
        List<Course> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Course item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(USER_ID,  userId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) {
        List<Course> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Course item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(and(eq(DISCIPLINE_ID, disciplineId), eq(GROUP_ID, groupId))).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.listIterator().next();
    }

    @Override
    public List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Course item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find().forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

}
