package com.sergeev.studapp.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
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
    protected static final String TYPE = "type";

    private Document doc;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("users");
    }

    @Override
    protected Document getDocument(User object) throws PersistentException{
        doc = new Document(FIRST_NAME, object.getFirstName()).append(LAST_NAME, object.getLastName())
                .append(TYPE, object.getType().getId()).append(ACCOUNT, object.getAccount().getId());
        if (object.getType() == User.Role.STUDENT) {
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
        ObjectId oid = (ObjectId) doc.get(ID);
        user.setId(String.valueOf(oid));
        user.setFirstName(String.valueOf(doc.get(FIRST_NAME)));
        user.setLastName(String.valueOf(doc.get(LAST_NAME)));
        user.setType(User.Role.getById(String.valueOf(doc.get(TYPE))));
        MongoAccountDao mad = new MongoAccountDao();
        user.setAccount(mad.getById(String.valueOf(doc.get(ACCOUNT))));
        if (user.getType() == User.Role.STUDENT) {
            MongoGroupDao mgd = new MongoGroupDao();
            user.setGroup(mgd.getById(String.valueOf(doc.get(GROUP))));
        }
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role type) throws PersistentException {
        List<User> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(FIRST_NAME).regex(pattern).and(TYPE).is(type.getId()).get();
        DBObject query2 = QueryBuilder.start(LAST_NAME).regex(pattern).and(TYPE).is(type.getId()).get();
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
        collection.find(query).sort(new BasicDBObject("first_name", 1).append("last_name", 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getByGroup(String groupId) throws PersistentException {
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
        collection.find(eq(GROUP, groupId)).sort(new BasicDBObject("first_name", 1).append("last_name", 1)).forEach(documents);
        if (list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getAll(User.Role type) throws PersistentException {
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
        collection.find(eq(TYPE, type.getId())).sort(new BasicDBObject("first_name", 1).append("last_name", 1)).forEach(documents);
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

