package com.sergeev.studapp.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

import static com.sergeev.studapp.model.Constants.*;

public class MongoUserDao extends MongoGenericDao<User> implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(MongoUserDao.class);

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection(USERS);
    }

    @Override
    protected Document createDocument(User object){
        Document doc = new Document(FIRST_NAME, object.getFirstName())
                .append(LAST_NAME, object.getLastName())
                .append(ROLE, object.getRole().name())
                .append(ACCOUNT_ID, object.getAccount().getId());
        if (object.getRole() == User.Role.STUDENT) {
            doc.append(GROUP_ID, object.getGroup().getId());
        }
        if (object.getId() == null){
            doc.append(ID, getNextId());
        } else {
            doc.append(ID, object.getId());
        }
        return doc;
    }

    @Override
    protected User parseDocument(Document doc) {
        if (doc == null || doc.isEmpty()) {
            throw new PersistentException("Empty document.");
        }
        MongoAccountDao accountDao = new MongoAccountDao();
        User user = new User()
                .setId(doc.getInteger(ID))
                .setFirstName(String.valueOf(doc.get(FIRST_NAME)))
                .setLastName(String.valueOf(doc.get(LAST_NAME)))
                .setRole(User.Role.valueOf(String.valueOf(doc.get(ROLE))))
                .setAccount(accountDao.getById(doc.getInteger(ACCOUNT_ID)));

        if (user.getRole() == User.Role.STUDENT) {
            MongoGroupDao groupDao = new MongoGroupDao();
            user.setGroup(groupDao.getById(doc.getInteger(GROUP_ID)));
        }
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role role) {
        List<User> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(FIRST_NAME).regex(pattern).and(ROLE).is(role.name()).get();
        DBObject query2 = QueryBuilder.start(LAST_NAME).regex(pattern).and(ROLE).is(role.name()).get();
        BasicDBList or = new BasicDBList();
        or.add(query1);
        or.add(query2);
        BasicDBObject query = new BasicDBObject("$or", or);

        Block<Document> documents = doc -> {
            User item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(query).sort(new BasicDBObject(FIRST_NAME, 1).append(LAST_NAME, 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getByGroup(Integer groupId) {
        List<User> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            User item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(GROUP_ID, groupId)).sort(new BasicDBObject(FIRST_NAME, 1).append(LAST_NAME, 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getAll(User.Role role) {
        List<User> list = new ArrayList<>();
        Block<Document> documents = doc -> {
            User item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(eq(ROLE, role.name())).sort(new BasicDBObject(FIRST_NAME, 1).append(LAST_NAME, 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public User getByToken(String token) {
        Account account = new MongoAccountDao().getByToken(token);
        Document doc = collection.find(eq(ACCOUNT_ID, account.getId())).first();
        if(doc == null || doc.isEmpty()){
            throw new PersistentException("Record not found.");
        }
        return parseDocument(doc);
    }

    @Override
    public User getByLogin(String login, String password) {
        Account account = new MongoAccountDao().getByLogin(login, password);
        Document doc = collection.find(eq(ACCOUNT_ID, account.getId())).first();
        if(doc == null || doc.isEmpty()){
            throw new PersistentException("Record not found.");
        }
        return parseDocument(doc);
    }
}

