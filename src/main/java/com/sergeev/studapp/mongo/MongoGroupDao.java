package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.bson.Document;

import static com.sergeev.studapp.model.Constants.GROUPS;
import static com.sergeev.studapp.model.Constants.TITLE;

public class MongoGroupDao extends MongoGenericDao<Group> implements GroupDao {

    public MongoGroupDao() {
        IndexOptions options = new IndexOptions().background(true).unique(true);
        collection.createIndex(Indexes.ascending(TITLE), options);
    }

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(GROUPS);
    }

    @Override
    protected Document createDocument(Group object) {
        Document doc = new Document(TITLE, object.getTitle());
        if (object.getId() == null) {
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Group parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        return new Group()
                .setId(doc.getInteger(ID))
                .setTitle(String.valueOf(doc.get(TITLE)));
    }

}
