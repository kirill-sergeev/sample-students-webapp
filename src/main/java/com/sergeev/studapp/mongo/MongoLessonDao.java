package com.sergeev.studapp.mongo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MongoLessonDao extends MongoGenericDao<Lesson> implements LessonDao {

    protected static final String DATE = "date";
    protected static final String TYPE = "type";
    protected static final String ORDINAL = "ordinal";
    protected static final String COURSE = "course";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("lessons");
    }

    @Override
    protected Document getDocument(Lesson object) throws PersistentException {
        return doc = new Document(DATE, Date.valueOf(object.getDate())).append(TYPE, object.getType().name()).append(ORDINAL, object.getOrder().ordinal() + 1).append(COURSE, object.getCourse().getId());
    }

    @Override
    protected Lesson parseDocument(Document doc) throws PersistentException {
        Lesson lesson = new Lesson();
        ObjectId oid = (ObjectId) doc.get(ID);
        lesson.setId(String.valueOf(oid));
        LocalDate date = Instant.ofEpochMilli(doc.getDate(DATE).toInstant().toEpochMilli()).atZone(ZoneId.of("UTC")).toLocalDate();
        lesson.setDate(date);
        lesson.setType(Lesson.Type.valueOf(String.valueOf(doc.get(TYPE))));
        lesson.setOrder(Lesson.Order.values()[(int) doc.get(ORDINAL) - 1]);
        MongoCourseDao mcd = new MongoCourseDao();
        lesson.setCourse(mcd.getById(String.valueOf(doc.get(COURSE))));
        return lesson;
    }

    @Override
    public List<Lesson> getByGroup(String groupId) throws PersistentException {
        List<Lesson> list = new ArrayList<>();
                Bson lookup = new Document("$lookup",
                new Document("from", "courses")
                        .append("localField", "id")
                        .append("foreignField", "course")
                        .append("as", "look_coll"));
        Bson match = new Document("$match", new Document("look_coll.group", groupId));
        Bson sort = new Document("$sort", new Document("date", 1));
        List<Bson> filters = new ArrayList<>();
        filters.add(lookup);
        filters.add(match);
        filters.add(sort);
        AggregateIterable<Document> it = collection.aggregate(filters);
        for (Document row : it) {
            Lesson lesson = parseDocument(row);
            if (groupId.equals(lesson.getCourse().getGroup().getId())) {
                list.add(lesson);
            }
        }
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }
}
