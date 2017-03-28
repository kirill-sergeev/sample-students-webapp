package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.model.Account;
import org.bson.Document;

public class MongoAccountDao extends MongoGenericDao<Account> implements AccountDao {

    protected static final String LOGIN = "login";
    protected static final String PASSWORD= "password";
    protected static final String TOKEN= "token";

    private Account account;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection("accounts");
    }

    @Override
    protected Document getDocument(Account object) {
        return new Document(LOGIN, object.getLogin()).append(PASSWORD, object.getPassword()).append(TOKEN, object.getToken());
    }

    @Override
    protected Account parseResult(Document doc) {
        account = new Account();
        account.setId(String.valueOf(doc.get(ID)));
        account.setLogin(String.valueOf(doc.get(LOGIN)));
        account.setPassword(String.valueOf(doc.get(PASSWORD)));
        account.setToken(String.valueOf(doc.get(TOKEN)));
        return account;
    }
}
