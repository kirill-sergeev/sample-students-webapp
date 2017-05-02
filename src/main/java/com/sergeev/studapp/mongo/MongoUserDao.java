package com.sergeev.studapp.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;
import org.bson.Document;

import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.sergeev.studapp.model.Constants.*;

public class MongoUserDao extends MongoGenericDao<User> implements UserDao {

    public MongoUserDao() {
        IndexOptions options = new IndexOptions().background(true).sparse(true);
        collection.createIndex(Indexes.ascending(FIRST_NAME), options);
        collection.createIndex(Indexes.ascending(LAST_NAME), options);
        collection.createIndex(Indexes.ascending(GROUP_ID), options);
        collection.createIndex(Indexes.ascending(ROLE), options);
        collection.createIndex(Indexes.ascending(LOGIN, PASSWORD), options);
        collection.createIndex(Indexes.ascending(TOKEN), options);
    }

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return db.getCollection(USERS);
    }

    @Override
    protected Document createDocument(User object) {
        Document doc = new Document(FIRST_NAME, object.getFirstName())
                .append(LAST_NAME, object.getLastName())
                .append(ROLE, object.getRole().name())
                .append(LOGIN, object.getAccount().getLogin())
                .append(PASSWORD, object.getAccount().getPassword());
        if (object.getRole() == User.Role.STUDENT) {
            doc.append(GROUP_ID, object.getGroup().getId());
        }
        if (object.getId() == null) {
            doc.append(ID, getNextId());
            doc.append(TOKEN, object.getAccount().getToken());
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
        User user = new User()
                .setId(doc.getInteger(ID))
                .setFirstName(String.valueOf(doc.get(FIRST_NAME)))
                .setLastName(String.valueOf(doc.get(LAST_NAME)))
                .setRole(User.Role.valueOf(String.valueOf(doc.get(ROLE))));
        user.setAccount(new User.Account().setLogin(String.valueOf(doc.get(LOGIN)))
                .setPassword(String.valueOf(doc.get(PASSWORD)))
                .setToken(String.valueOf(doc.get(TOKEN))));
        if (user.getRole() == User.Role.STUDENT) {
            MongoGroupDao groupDao = new MongoGroupDao();
            user.setGroup(groupDao.getById(doc.getInteger(GROUP_ID)));
        }
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role role) {
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(FIRST_NAME).regex(pattern).and(ROLE).is(role.name()).get();
        DBObject query2 = QueryBuilder.start(LAST_NAME).regex(pattern).and(ROLE).is(role.name()).get();
        BasicDBList or = new BasicDBList();
        or.add(query1);
        or.add(query2);
        BasicDBObject query = new BasicDBObject("$or", or);
        return getBy(query, Sorts.ascending(FIRST_NAME, LAST_NAME));
    }

    @Override
    public List<User> getByGroup(Integer groupId) {
        return getBy((eq(GROUP_ID, groupId)), Sorts.ascending(FIRST_NAME, LAST_NAME));
    }

    @Override
    public List<User> getAll(User.Role role) {
        return getBy((eq(ROLE, role.name())), Sorts.ascending(FIRST_NAME, LAST_NAME));
    }

    @Override
    public User getByToken(String token) {
        return getBy((eq(TOKEN, token)), null).get(0);
    }

    @Override
    public User getByLogin(String login, String password) {
        return getBy(and(eq(LOGIN, login), eq(PASSWORD, password)), null).get(0);
    }

}

