package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;
import org.bson.Document;

import static com.sergeev.studapp.model.Constants.DISCIPLINES;
import static com.sergeev.studapp.model.Constants.TITLE;

public class MongoDisciplineDao extends MongoGenericDao<Discipline> implements DisciplineDao {

    public MongoDisciplineDao() {
        IndexOptions options = new IndexOptions().background(true).unique(true);
        collection.createIndex(Indexes.ascending(TITLE), options);
    }

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(DISCIPLINES);
    }

    @Override
    protected Document createDocument(Discipline object) {
        Document doc = new Document(TITLE, object.getTitle());
        if (object.getId() == null) {
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Discipline parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        return new Discipline()
                .setId(doc.getInteger(ID))
                .setTitle(String.valueOf(doc.get(TITLE)));
    }

}
