package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.model.Discipline;
import org.bson.Document;

import static com.sergeev.studapp.model.Constants.DISCIPLINES;
import static com.sergeev.studapp.model.Constants.TITLE;

public class MongoDisciplineDao extends MongoGenericDao<Discipline> implements DisciplineDao {

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection(DISCIPLINES);
    }

    @Override
    protected Document getDocument(Discipline object) {
        doc = new Document(TITLE, object.getTitle());
        if (object.getId() == null){
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Discipline parseDocument(Document doc) {
        Discipline discipline = new Discipline();
        discipline.setId(doc.getInteger(ID));
        discipline.setTitle(String.valueOf(doc.get(TITLE)));
        return discipline;
    }
}
