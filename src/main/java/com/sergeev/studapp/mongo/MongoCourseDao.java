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

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection(COURSES);
    }

    @Override
    protected Document getDocument(Course object) {
        doc = new Document(DISCIPLINE_ID, object.getDiscipline().getId())
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
        Course course = new Course();
        course.setId(doc.getInteger(ID));

        MongoDisciplineDao mdd = new MongoDisciplineDao();
        course.setDiscipline(mdd.getById(doc.getInteger(DISCIPLINE_ID)));

        MongoGroupDao mgd = new MongoGroupDao();
        course.setGroup(mgd.getById(doc.getInteger(GROUP_ID)));

        MongoUserDao mud = new MongoUserDao();
        course.setTeacher(mud.getById(doc.getInteger(USER_ID)));
        return course;
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
