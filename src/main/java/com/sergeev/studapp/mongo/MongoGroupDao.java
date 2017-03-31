package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoGroupDao extends MongoGenericDao<Group> implements GroupDao {

    protected static final String GROUP_TITLE = "title";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db){
        return collection = db.getCollection("groups");
    }

    @Override
    protected Document getDocument(Group object) throws PersistentException {
        return doc = new Document(GROUP_TITLE, object.getTitle());
    }

    @Override
    protected Group parseDocument(Document doc) {
        Group group = new Group();
        ObjectId oid = (ObjectId) doc.get(ID);
        group.setId(String.valueOf(oid));
        group.setTitle(String.valueOf(doc.get(GROUP_TITLE)));
        return group;
    }
}
