package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.sergeev.studapp.model.Constants.*;

public class MongoMarkDao extends MongoGenericDao<Mark> implements MarkDao {

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection(MARKS);
    }

    @Override
    protected Document createDocument(Mark object) {
        Document doc = new Document(LESSON_ID, object.getLesson().getId())
                .append(USER_ID, object.getStudent().getId())
                .append(VALUE, object.getValue());
        if (object.getId() == null){
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Mark parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        MongoLessonDao lessonDao = new MongoLessonDao();
        MongoUserDao userDao = new MongoUserDao();
        return new Mark()
                .setId(doc.getInteger(ID))
                .setValue((Integer) doc.get(VALUE))
                .setLesson(lessonDao.getById(doc.getInteger(LESSON_ID)))
                .setStudent(userDao.getById(doc.getInteger(USER_ID)));
    }

    @Override
    public Double getAvgMark(Integer studentId, Integer disciplineId) {
        return 0.0;
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) {
        List<Mark> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection
                .find(eq(LESSON_ID, lessonId))
                .iterator()) {
            while (cursor.hasNext()) {
                Mark item = parseDocument(cursor.next());
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId) {
        return Collections.emptyList();
    }

}
