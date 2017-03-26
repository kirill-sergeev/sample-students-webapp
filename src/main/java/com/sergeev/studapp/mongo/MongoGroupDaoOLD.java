//package com.sergeev.studapp.mongo;
//
//import com.mongodb.Block;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import com.sergeev.studapp.dao.GroupDao;
//import com.sergeev.studapp.dao.PersistentException;
//import com.sergeev.studapp.model.Group;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.mongodb.client.model.Filters.eq;
//
//public class MongoGroupDaoOLD extends MongoGenericDao<Group> implements GroupDao {
//
//    protected static final String GROUP_ID = "_id";
//    protected static final String GROUP_TITLE = "title";
//    private MongoDatabase db = MongoDaoFactory.getConnection();
//    private MongoCollection<Document> collection = db.getCollection("groups");
//
//    @Override
//    public Group persist(Group object) throws PersistentException {
//        Document doc = new Document(GROUP_TITLE, object.getTitle());
//        collection.insertOne(doc);
//        ObjectId id = (ObjectId) doc.get("_id");
//        object.setId(String.valueOf(id));
//        return object;
//    }
//
//    @Override
//    public Group getById(String id) throws PersistentException {
//        ObjectId oid = new ObjectId(id);
//        Document doc = collection.find(eq(GROUP_ID, oid)).first();
//        Group group = new Group();
//        group.setId(String.valueOf(doc.get(GROUP_ID)));
//        group.setTitle(String.valueOf(doc.get(GROUP_TITLE)));
//        return group;
//    }
//
//    @Override
//    public void update(Group object) throws PersistentException {
//        String id = object.getId();
//        ObjectId oid = new ObjectId(id);
//        Document doc = new Document(GROUP_TITLE, object.getTitle());
//        UpdateResult updateResult = collection.updateOne(eq(GROUP_ID, oid), doc);
//        if (updateResult.getModifiedCount() == 0) {
//            throw new PersistentException("Nothing updated!");
//        }
//    }
//
//    @Override
//    public void delete(Group object) throws PersistentException {
//        String id = object.getId();
//        delete(id);
//    }
//
//    @Override
//    public void delete(String id) throws PersistentException {
//        ObjectId oid = new ObjectId(id);
//        DeleteResult deleteResult = collection.deleteOne(eq(GROUP_ID, oid));
//        if (deleteResult.getDeletedCount() == 0) {
//            throw new PersistentException("Nothing deleted!");
//        }
//    }
//
//    @Override
//    public List<Group> getAll() throws PersistentException {
//        List<Group> list = new ArrayList<>();
//        Block<Document> documents = new Block<Document>() {
//            @Override
//            public void apply(final Document doc) {
//                Group group = new Group();
//                group.setId(String.valueOf(doc.get(GROUP_ID)));
//                group.setTitle(String.valueOf(doc.get(GROUP_TITLE)));
//                list.add(group);
//            }
//        };
//        collection.find().forEach(documents);
//        return list;
//    }
//
//}
