package com.sergeev.studapp.mongo;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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
        doc = new Document(LESSON, object.getLesson().getId())
                .append(STUDENT, object.getStudent().getId())
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
        mark.setLesson(mld.getById(String.valueOf(doc.get(LESSON))));

        MongoUserDao mud = new MongoUserDao();
        mark.setStudent(mud.getById(String.valueOf(doc.get(STUDENT))));
        return mark;
    }

    @Override
    public Double getAvgMark(String studentId, String disciplineId) throws PersistentException {
        return 0.0;
    }

    @Override
    public List<Mark> getByLesson(String lessonId) throws PersistentException {
        List<Mark> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            Mark item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(LESSON, lessonId)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(String disciplineId, String studentId) throws PersistentException {
        return Collections.emptyList();
    }
}
