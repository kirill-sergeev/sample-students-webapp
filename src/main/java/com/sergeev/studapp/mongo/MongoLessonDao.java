package com.sergeev.studapp.mongo;

import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

 public class MongoLessonDao extends MongoGenericDao<Lesson> implements LessonDao {

    protected static final String DATE  = "date";
    protected static final String TYPE  = "type";
    protected static final String ORDER  = "order";
    protected static final String COURSE  = "course";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("lessons");
    }

    @Override
    protected Document getDocument(Lesson object) throws PersistentException {
        return doc = new Document(DATE , Date.valueOf(object.getDate())).append(TYPE, object.getType().getId()).append(ORDER, object.getOrder().getNumber()).append(COURSE, new DBRef("courses", object.getCourse().getId()));
    }

    @Override
    protected Lesson parseDocument(Document doc) throws PersistentException {
        Lesson lesson = new Lesson();
        ObjectId oid = (ObjectId) doc.get(ID);
        lesson.setId(String.valueOf(oid));
        LocalDate date = Instant.ofEpochMilli(doc.getDate(DATE).toInstant().toEpochMilli()).atZone(ZoneOffset.ofHours(2)).toLocalDate();
        lesson.setDate(date);
        lesson.setType(Lesson.Type.getById(String.valueOf(doc.get(TYPE))));
        lesson.setOrder(Lesson.Order.getByNumber((Integer) doc.get(ORDER)));
        MongoCourseDao mcd = new MongoCourseDao();
        DBRef courseRef = (DBRef) doc.get(COURSE);
        lesson.setCourse(mcd.getById(String.valueOf(courseRef.getId())));
        return lesson;
    }

    @Override
    public List<Lesson> getByGroup(String courseId) throws PersistentException {
        return null;
    }
}
