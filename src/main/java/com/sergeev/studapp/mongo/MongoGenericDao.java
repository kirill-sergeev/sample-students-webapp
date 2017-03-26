package com.sergeev.studapp.mongo;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public abstract class MongoGenericDao<T extends Identified> implements GenericDao<T> {

    private MongoDatabase db = MongoDaoFactory.getConnection();
    private MongoCollection<Document> collection = getCollection(db);
    private String id;
    private Document doc;
    private ObjectId oid;

    protected static final String ID = "_id";
    protected abstract MongoCollection<Document> getCollection(MongoDatabase db);
    protected abstract Document getDocument(T object);
    protected abstract T parseResult(Document doc) throws PersistentException;

    @Override
    public T persist(T object) throws PersistentException {
        doc = getDocument(object);
        collection.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        object.setId(String.valueOf(id));
        return object;
    }

    @Override
    public T getById(String id) throws PersistentException {
        oid = new ObjectId(id);
        doc = collection.find(eq(ID, oid)).first();
        return parseResult(doc);
    }

    @Override
    public void update(T object) throws PersistentException {
        id = object.getId();
        oid = new ObjectId(id);
        doc = getDocument(object);
        UpdateResult updateResult = collection.replaceOne(eq(ID, oid), doc);
        if (updateResult.getModifiedCount() == 0) {
            throw new PersistentException("Nothing updated!");
        }
    }

    @Override
    public void delete(T object) throws PersistentException {
        id = object.getId();
        delete(id);
    }

    @Override
    public void delete(String id) throws PersistentException {
        oid = new ObjectId(id);
        DeleteResult deleteResult = collection.deleteOne(eq(ID, oid));
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
                item = parseResult(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find().forEach(documents);
        return list;
    }
}
