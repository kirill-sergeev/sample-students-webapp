package com.sergeev.studapp.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public abstract class MongoGenericDao<T extends Identified> implements GenericDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MongoGenericDao.class);

    private static MongoDatabase db = MongoDaoFactory.getConnection();
    private static MongoCollection<Document> counters = db.getCollection("counters");
    private MongoCollection<Document> collection = getCollection(db);

    private Document doc;

    protected static final String ID = "_id";

    protected abstract MongoCollection<Document> getCollection(MongoDatabase db);

    protected abstract Document getDocument(T object) throws PersistentException;

    protected abstract T parseDocument(Document doc) throws PersistentException;

    @Override
    public T save(T object) throws PersistentException {
        doc = getDocument(object);
        collection.insertOne(doc);
        object.setId((Integer) doc.get(ID));
        return object;
    }

    @Override
    public T getById(Integer id) throws PersistentException {
        doc = collection.find(eq(ID, id)).first();
        if (doc == null) {
            throw new PersistentException("Object not found.");
        }
        return parseDocument(doc);
    }

    @Override
    public T update(T object) throws PersistentException {
        doc = getDocument(object);
        UpdateResult updateResult = collection.replaceOne(eq(ID, object.getId()), doc);
        if (updateResult.getModifiedCount() == 0) {
            throw new PersistentException("Nothing updated!");
        }
        return  object;
    }

    @Override
    public void delete(Integer id) throws PersistentException {
        DeleteResult deleteResult = collection.deleteOne(eq(ID, id));
        if (deleteResult.getDeletedCount() == 0) {
            throw new PersistentException("Nothing deleted!");
        }
    }

    @Override
    public List<T> getAll() throws PersistentException {
        List<T> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            T item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find().sort(new BasicDBObject("title", 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    protected Integer getNextId() {
        String name = this.getClass().getSimpleName().toLowerCase()
                .replace("mongo", "")
                .replace("dao", "");
        Document searchQuery = new Document("_id", name);
        Document increase = new Document("seq", 1);
        Document updateQuery = new Document("$inc", increase);

        Document result = counters.findOneAndUpdate(searchQuery, updateQuery);
        if (result == null) {
            Document document = new Document()
                    .append("_id", name)
                    .append("seq", 2);
            counters.insertOne(document);
            return 1;
        }
        return result.getInteger("seq");
    }
}
