package com.sergeev.studapp.mongo;

import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoMarkDao extends MongoGenericDao<Mark> implements MarkDao {

    protected static final String LESSON = "lesson";
    protected static final String STUDENT = "student";
    protected static final String VALUE = "value";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("marks");
    }

    @Override
    protected Document getDocument(Mark object) throws PersistentException {
        doc = new Document(LESSON, new DBRef("lessons", object.getLesson().getId()))
                .append(STUDENT, new DBRef("users", object.getStudent().getId()))
                .append(VALUE, object.getValue());
        return doc;
    }

    @Override
    protected Mark parseDocument(Document doc) throws PersistentException {
        Mark mark = new Mark();
        ObjectId oid = (ObjectId) doc.get(ID);
        mark.setId(String.valueOf(oid));
        mark.setValue((Integer) doc.get(VALUE));

        MongoLessonDao mld = new MongoLessonDao();
        DBRef lessonRef = (DBRef) doc.get(LESSON);
        mark.setLesson(mld.getById(String.valueOf(lessonRef.getId())));

        MongoUserDao mud = new MongoUserDao();
        DBRef userRef = (DBRef) doc.get(STUDENT);
        mark.setStudent(mud.getById(String.valueOf(userRef.getId())));
        return mark;
    }

    @Override
    public Double getAvgMark(String studentId, String disciplineId) throws PersistentException {
        return null;
    }

    @Override
    public List<Mark> getByLesson(String lessonId) throws PersistentException {
        return null;
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(String disciplineId, String studentId) throws PersistentException {
        return null;
    }
}
