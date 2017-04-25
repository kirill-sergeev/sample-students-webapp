package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.bson.Document;

import static com.sergeev.studapp.model.Constants.*;

public class MongoGroupDao extends MongoGenericDao<Group> implements GroupDao {

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db){
        return collection = db.getCollection(GROUPS);
    }

    @Override
    protected Document getDocument(Group object) throws PersistentException {
        doc = new Document(TITLE, object.getTitle());
        if (object.getId() == null){
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Group parseDocument(Document doc) {
        Group group = new Group();
        group.setId(doc.getInteger(ID));
        group.setTitle(String.valueOf(doc.get(TITLE)));
        return group;
    }
}
