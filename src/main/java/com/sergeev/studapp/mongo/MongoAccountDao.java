package com.sergeev.studapp.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

import static com.sergeev.studapp.model.Constants.*;

public class MongoAccountDao extends MongoGenericDao<Account> implements AccountDao {

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection(ACCOUNTS);
    }

    @Override
    protected Document getDocument(Account object) {
        doc = new Document(LOGIN, object.getLogin())
                .append(PASSWORD, object.getPassword())
                .append(TOKEN, object.getToken());
        if (object.getId() == null) {
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected Account parseDocument(Document doc) {
        Account account = new Account();
        account.setId(doc.getInteger(ID));
        account.setLogin(String.valueOf(doc.get(LOGIN)));
        account.setPassword(String.valueOf(doc.get(PASSWORD)));
        account.setToken(String.valueOf(doc.get(TOKEN)));
        return account;
    }

    protected Account getByToken(String token) {
        doc = collection.find(eq(TOKEN, token)).first();
        if (doc == null) {
            throw new PersistentException("Record not found.");
        }
        return parseDocument(doc);
    }

    protected Account getByLogin(String login, String password) {
        List<Account> list = new ArrayList<>();
        Pattern patternLogin = Pattern.compile(login, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        Pattern patternPassword = Pattern.compile(password, Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(LOGIN).regex(patternLogin).and(PASSWORD).regex(patternPassword).get();
        BasicDBList or = new BasicDBList();
        or.add(query1);
        BasicDBObject query = new BasicDBObject("$or", or);

        Block<Document> documents = doc -> {
            Account item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(query).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.listIterator().next();
    }
}
