package com.sergeev.studapp.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;
import org.bson.Document;

import java.util.List;

public class MongoUserDao extends MongoGenericDao<User> implements UserDao {

    protected static final String FIRST_NAME = "first_name";
    protected static final String LAST_NAME = "last_name";
    protected static final String ACCOUNT = "account";
    protected static final String GROUP = "group";
    protected static final String TYPE = "type";

    private Document doc;
    private User user;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("users");
    }

    @Override
    protected Document getDocument(User object) {
        return doc = new Document(FIRST_NAME, object.getFirstName()).append(LAST_NAME, object.getLastName()).append(TYPE, object.getType().getId()).append(ACCOUNT, object.getAccount().getId()).append(GROUP, object.getGroup().getId());
    }

    @Override
    protected User parseResult(Document doc) throws PersistentException {
        user = new User();
        user.setId(String.valueOf(doc.get(ID)));
        user.setFirstName(String.valueOf(doc.get(FIRST_NAME)));
        user.setLastName(String.valueOf(doc.get(LAST_NAME)));
        user.setType(User.Role.getById(String.valueOf(doc.get(TYPE))));
        if (user.getType() == User.Role.STUDENT) {
            MongoGroupDao mgd = new MongoGroupDao();
            user.setGroup(mgd.getById(String.valueOf(doc.get(GROUP))));
        }
        MongoAccountDao mad = new MongoAccountDao();
        user.setAccount(mad.getById(String.valueOf(doc.get(ACCOUNT))));
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role type) throws PersistentException {
        return null;
    }

    @Override
    public List<User> getByGroup(String groupId) throws PersistentException {
        return null;
    }

    @Override
    public List<User> getAll(User.Role type) throws PersistentException {
        return getAll();
    }

    @Override
    public User getByToken(String token) throws PersistentException {
        return null;
    }

    @Override
    public User getByLogin(String login, String password) throws PersistentException {
        return null;
    }
}
