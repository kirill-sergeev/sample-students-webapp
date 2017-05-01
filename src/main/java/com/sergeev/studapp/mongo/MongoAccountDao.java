package com.sergeev.studapp.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import org.bson.Document;

import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.sergeev.studapp.model.Constants.*;

public class MongoAccountDao extends MongoGenericDao<Account> implements AccountDao {

    public MongoAccountDao() {
        IndexOptions options = new IndexOptions().background(true).sparse(true);
        collection.createIndex(Indexes.ascending(LOGIN, PASSWORD), options);
        collection.createIndex(Indexes.ascending(TOKEN), options);
    }

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(ACCOUNTS);
    }

    @Override
    protected Document createDocument(Account account) {
        Document doc = new Document(LOGIN, account.getLogin())
                .append(PASSWORD, account.getPassword())
                .append(TOKEN, account.getToken());
        if (account.getId() == null) {
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, account.getId());
        }
        return doc;
    }

    @Override
    protected Account parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        return new Account()
                .setId(doc.getInteger(ID))
                .setLogin(String.valueOf(doc.get(LOGIN)))
                .setPassword(String.valueOf(doc.get(PASSWORD)))
                .setToken(String.valueOf(doc.get(TOKEN)));
    }

    protected Account getByToken(String token) {
        Document doc = collection.find(eq(TOKEN, token)).first();
        return parseDocument(doc);
    }

    protected Account getByLogin(String login, String password) {
        Pattern patternLogin = Pattern.compile(login, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        Pattern patternPassword = Pattern.compile(password, Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(LOGIN).regex(patternLogin).and(PASSWORD).regex(patternPassword).get();
        BasicDBList or = new BasicDBList();
        or.add(query1);
        BasicDBObject query = new BasicDBObject("$or", or);
        return getBy(query, null).get(0);
    }

}
