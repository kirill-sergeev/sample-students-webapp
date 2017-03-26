package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.model.Discipline;
import org.bson.Document;

public class MongoDisciplineDao extends MongoGenericDao<Discipline> implements DisciplineDao {

    protected static final String DISCIPLINE_TITLE = "title";

    private Document doc;
    private Discipline discipline;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("disciplines");
    }

    @Override
    protected Document getDocument(Discipline object) {
        return doc = new Document(DISCIPLINE_TITLE, object.getTitle());
    }

    @Override
    protected Discipline parseResult(Document doc) {
        discipline = new Discipline();
        discipline.setId(String.valueOf(doc.get(ID)));
        discipline.setTitle(String.valueOf(doc.get(DISCIPLINE_TITLE)));
        return discipline;
    }
}
