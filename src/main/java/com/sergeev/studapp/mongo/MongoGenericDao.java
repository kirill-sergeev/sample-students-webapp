package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Updates.inc;
import static com.sergeev.studapp.model.Constants.TITLE;

public abstract class MongoGenericDao<T extends Identified> implements GenericDao<T> {

    private static final MongoDatabase DB = MongoDaoFactory.getConnection();
    private static final MongoCollection<Document> COUNTERS = DB.getCollection("counters");
    protected static final String ID = "_id";
    protected MongoCollection<Document> collection = getCollection(DB);

    protected abstract MongoCollection<Document> getCollection(MongoDatabase db);

    protected abstract Document createDocument(T object);

    protected abstract T parseDocument(Document doc);

    @Override
    public void save(T object) {
        Document doc = createDocument(object);
        collection.insertOne(doc);
        object.setId(doc.getInteger(ID));
    }

    @Override
    public void update(T object) {
        Document doc = createDocument(object);
        UpdateResult updateResult = collection.replaceOne(eq(ID, object.getId()), doc);
        if (updateResult.getModifiedCount() == 0) {
            throw new PersistentException("Nothing updated!");
        }
    }

    @Override
    public void remove(Integer id) {
        DeleteResult deleteResult = collection.deleteOne(eq(ID, id));
        if (deleteResult.getDeletedCount() == 0) {
            throw new PersistentException("Nothing deleted!");
        }
    }

    @Override
    public T getById(Integer id) {
        Document doc = collection.find(eq(ID, id)).first();
        return parseDocument(doc);
    }

    @Override
    public List<T> getAll() {
        return getByParams(new Document(), ascending(TITLE));
    }

    protected List<T> getByParams(Bson filter, Bson sort){
        List<T> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection
                .find(filter)
                .sort(sort)
                .iterator()) {
            while (cursor.hasNext()) {
                T item = parseDocument(cursor.next());
                list.add(item);
            }
        }
        return list;
    }

    protected Integer getNextId() {
        String attribute = "seq";
        String value = collection.getNamespace().getCollectionName();
        Document current = COUNTERS.findOneAndUpdate(eq(ID, value), inc(attribute, 1));

        if (current == null) {
            current = collection.find().sort(ascending(ID)).first();
            Integer number;
            if (current == null || current.isEmpty()) {
                number = 1;
            } else {
                number = current.getInteger(ID);
            }
            Document document = new Document()
                    .append(ID, value)
                    .append(attribute, number + 1);
            COUNTERS.insertOne(document);
            return number;
        }
        return current.getInteger(attribute);
    }

}
