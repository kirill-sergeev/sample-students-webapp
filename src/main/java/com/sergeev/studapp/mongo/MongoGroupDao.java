package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.model.Group;
import org.bson.Document;

public class MongoGroupDao extends MongoGenericDao<Group> implements GroupDao {

    protected static final String GROUP_TITLE = "title";

    private Document doc;
    private Group group;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("groups");
    }

    @Override
    protected Document getDocument(Group object) {
        return doc = new Document(GROUP_TITLE, object.getTitle());
    }

    @Override
    protected Group parseResult(Document doc) {
        group = new Group();
        group.setId(String.valueOf(doc.get(ID)));
        group.setTitle(String.valueOf(doc.get(GROUP_TITLE)));
        return group;
    }
}
