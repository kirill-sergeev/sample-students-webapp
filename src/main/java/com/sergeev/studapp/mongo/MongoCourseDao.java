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

public class MongoCourseDao extends MongoGenericDao<Course> implements CourseDao {

    protected static final String DISCIPLINE = "discipline";
    protected static final String GROUP = "group";
    protected static final String TEACHER = "teacher";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("courses");
    }

    @Override
    protected Document getDocument(Course object) throws PersistentException {
        doc = new Document(ID, getNextId())
                .append(DISCIPLINE, object.getDiscipline().getId())
                .append(GROUP, object.getGroup().getId())
                .append(TEACHER, object.getTeacher().getId());
        return doc;
    }

    @Override
    protected Course parseDocument(Document doc) throws PersistentException {
        Course course = new Course();
        course.setId(doc.getInteger(ID));

        MongoDisciplineDao mdd = new MongoDisciplineDao();
        course.setDiscipline(mdd.getById(doc.getInteger(DISCIPLINE)));

        MongoGroupDao mgd = new MongoGroupDao();
        course.setGroup(mgd.getById(doc.getInteger(GROUP)));

        MongoUserDao mud = new MongoUserDao();
        course.setTeacher(mud.getById(doc.getInteger(TEACHER)));
        return course;
    }

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) throws PersistentException {
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
        collection.find(eq(DISCIPLINE, disciplineId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByGroup(Integer groupId) throws PersistentException {
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
        collection.find(eq(GROUP, groupId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByTeacher(Integer userId) throws PersistentException {
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
        collection.find(eq(TEACHER,  userId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) throws PersistentException {
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
        collection.find(and(eq(DISCIPLINE, disciplineId), eq(GROUP, groupId))).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.listIterator().next();
    }

    @Override
    public List<Course> getAll() throws PersistentException {
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
