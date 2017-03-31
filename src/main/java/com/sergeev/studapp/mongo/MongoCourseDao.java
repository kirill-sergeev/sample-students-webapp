package com.sergeev.studapp.mongo;

import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

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
        doc = new Document(DISCIPLINE, new DBRef("disciplines", object.getDiscipline().getId()))
                .append(GROUP, new DBRef("groups", object.getGroup().getId()))
                .append(TEACHER, new DBRef("users", object.getTeacher().getId()));
        return doc;
    }

    @Override
    protected Course parseDocument(Document doc) throws PersistentException {
        Course course = new Course();
        ObjectId oid = (ObjectId) doc.get(ID);
        course.setId(String.valueOf(oid));

        MongoDisciplineDao mdd = new MongoDisciplineDao();
        DBRef disciplineRef = (DBRef) doc.get(DISCIPLINE);
        course.setDiscipline(mdd.getById(String.valueOf(disciplineRef.getId())));

        MongoGroupDao mgd = new MongoGroupDao();
        DBRef groupRef = (DBRef) doc.get(GROUP);
        course.setGroup(mgd.getById(String.valueOf(groupRef.getId())));

        MongoUserDao mud = new MongoUserDao();
        DBRef teacherRef = (DBRef) doc.get(TEACHER);
        course.setTeacher(mud.getById(String.valueOf(teacherRef.getId())));
        return course;
    }

    @Override
    public List<Course> getByDiscipline(String disciplineId) throws PersistentException {
        return null;
    }

    @Override
    public List<Course> getByGroup(String groupId) throws PersistentException {
        return null;
    }

    @Override
    public List<Course> getByTeacher(String userId) throws PersistentException {
        return null;
    }

    @Override
    public Course getByDisciplineAndGroup(String disciplineId, String groupId) throws PersistentException {
        return null;
    }

}
