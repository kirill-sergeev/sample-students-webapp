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

public class MongoUserDao extends MongoGenericDao<User> implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(MongoUserDao.class);

    protected static final String FIRST_NAME = "first_name";
    protected static final String LAST_NAME = "last_name";
    protected static final String ACCOUNT = "account";
    protected static final String GROUP = "group";
    protected static final String ROLE = "role";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("users");
    }

    @Override
    protected Document getDocument(User object) throws PersistentException{
        doc = new Document(ID, getNextId())
                .append(FIRST_NAME, object.getFirstName())
                .append(LAST_NAME, object.getLastName())
                .append(ROLE, object.getRole().name())
                .append(ACCOUNT, object.getAccount().getId());
        if (object.getRole() == User.Role.STUDENT) {
            doc.append(GROUP, object.getGroup().getId());
        }

        if(doc == null || doc.isEmpty()){
            throw new PersistentException("Bad fields for entity " + object.getClass().getName());
        }
        return doc;
    }

    @Override
    protected User parseDocument(Document doc) throws PersistentException {
        User user = new User();
        user.setId(doc.getInteger(ID));
        user.setFirstName(String.valueOf(doc.get(FIRST_NAME)));
        user.setLastName(String.valueOf(doc.get(LAST_NAME)));
        user.setRole(User.Role.valueOf(String.valueOf(doc.get(ROLE))));
        MongoAccountDao mad = new MongoAccountDao();
        user.setAccount(mad.getById(doc.getInteger(ACCOUNT)));
        if (user.getRole() == User.Role.STUDENT) {
            MongoGroupDao mgd = new MongoGroupDao();
            user.setGroup(mgd.getById(doc.getInteger(GROUP)));
        }
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role role) throws PersistentException {
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
    public List<User> getByGroup(Integer groupId) throws PersistentException {
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
        collection.find(eq(GROUP, groupId)).sort(new BasicDBObject(FIRST_NAME, 1).append(LAST_NAME, 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getAll(User.Role role) throws PersistentException {
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
    public User getByToken(String token) throws PersistentException {
        Account account = new MongoAccountDao().getByToken(token);
        doc = collection.find(eq(ACCOUNT, account.getId())).first();
        if(doc == null || doc.isEmpty()){
            throw new PersistentException("Record not found.");
        }
        return parseDocument(doc);
    }

    @Override
    public User getByLogin(String login, String password) throws PersistentException {
        Account account = new MongoAccountDao().getByLogin(login, password);
        doc = collection.find(eq(ACCOUNT, account.getId())).first();
        if(doc == null || doc.isEmpty()){
            throw new PersistentException("Record not found.");
        }
        return parseDocument(doc);
    }
}

