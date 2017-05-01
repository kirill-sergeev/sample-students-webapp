package com.sergeev.studapp.mongo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.sergeev.studapp.model.Constants.*;

public class MongoLessonDao extends MongoGenericDao<Lesson> implements LessonDao {

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(LESSONS);
    }

    @Override
    protected Document createDocument(Lesson object) {
        Document doc = new Document(DATE, Date.valueOf(object.getDate()))
                .append(TYPE, object.getType().name())
                .append(ORDER, object.getOrder().ordinal() + 1)
                .append(COURSE_ID, object.getCourse().getId());
        if (object.getId() == null){
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Lesson parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        LocalDate date = Instant.ofEpochMilli(
                doc.getDate(DATE).toInstant().toEpochMilli()).atZone(ZoneId.of("UTC")).toLocalDate();
        MongoCourseDao courseDao = new MongoCourseDao();
        return new Lesson()
                .setId(doc.getInteger(ID))
                .setDate(date)
                .setType(Lesson.Type.valueOf(String.valueOf(doc.get(TYPE))))
                .setOrder(Lesson.Order.values()[(int) doc.get(ORDER) - 1])
                .setCourse(courseDao.getById(doc.getInteger(COURSE_ID)));
    }

    @Override
    public List<Lesson> getByGroup(Integer groupId) {
        List<Lesson> list = new ArrayList<>();
        Bson lookup = Aggregates.lookup(COURSES, "id", COURSE_ID, "course");
        Bson match = Aggregates.match(eq("course." + GROUP_ID, groupId));
        Bson sort = Aggregates.sort(Sorts.ascending(DATE));

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
        return list;
    }
}
